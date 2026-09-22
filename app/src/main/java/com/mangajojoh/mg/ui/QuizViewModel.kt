package com.mangajojoh.mg.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mangajojoh.mg.data.ProgressRepository
import com.mangajojoh.mg.data.Question
import com.mangajojoh.mg.data.QuestionBank
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizUiState(
    val level: Int = 1,
    val questions: List<Question> = emptyList(),
    val questionIndex: Int = 0,
    val question: Question? = null,
    val selectedIndex: Int? = null,
    val answered: Boolean = false,
    val correct: Boolean? = null,
    val lives: Int = 3,
    val timeLeft: Int = 10,
    val finished: Boolean = false,   // niveau terminé (gagné ou perdu)
    val won: Boolean = false
) {
    val questionNumber: Int get() = questionIndex + 1
    val totalQuestions: Int get() = questions.size
}

class QuizViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ProgressRepository(app)

    private val _state = MutableStateFlow(QuizUiState())
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    /** Callback appelé quand le niveau entier est fini (won = true/false). */
    var onLevelFinished: ((Boolean) -> Unit)? = null

    private var timerJob: Job? = null
    private var nextJob: Job? = null

    fun getUnlocked(): Int = repo.getUnlockedLevel()
    fun getCurrent(): Int = repo.getCurrentLevel()
    fun setCurrentLevel(level: Int) = repo.setCurrentLevel(level)
    fun reset() = repo.reset()

    fun startLevel(level: Int) {
        cancelJobs()
        val qs = QuestionBank.getQuestionsForLevel(level)
            .map { QuestionBank.shuffleChoices(it) }
        _state.value = QuizUiState(
            level = level,
            questions = qs,
            questionIndex = 0,
            question = qs.firstOrNull(),
            lives = 3,
            timeLeft = 10
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.timeLeft > 0 && !_state.value.answered && !_state.value.finished) {
                delay(1000L)
                if (_state.value.answered || _state.value.finished) break
                _state.value = _state.value.copy(timeLeft = _state.value.timeLeft - 1)
            }
            if (_state.value.timeLeft <= 0 && !_state.value.answered && !_state.value.finished) {
                // Temps écoulé -> perdu sur cette question
                handleAnswer(selected = null, correct = false)
            }
        }
    }

    fun selectAnswer(index: Int) {
        val s = _state.value
        if (s.answered || s.finished || s.question == null) return
        val correct = index == s.question.bonneReponse
        handleAnswer(selected = index, correct = correct)
    }

    private fun handleAnswer(selected: Int?, correct: Boolean) {
        timerJob?.cancel()
        val s = _state.value
        val newLives = if (correct) s.lives else s.lives - 1

        _state.value = s.copy(
            selectedIndex = selected,
            answered = true,
            correct = correct,
            lives = newLives
        )

        // Pause 750 ms puis décision
        nextJob?.cancel()
        nextJob = viewModelScope.launch {
            delay(1500L)
            val st = _state.value
            when {
                // Plus de vies -> niveau perdu
                st.lives <= 0 -> finishLevel(won = false)
                // C'était la dernière question -> réussi
                st.questionIndex >= st.questions.size - 1 -> finishLevel(won = true)
                // Sinon -> question suivante
                else -> nextQuestion()
            }
        }
    }

    private fun nextQuestion() {
        val s = _state.value
        val newIdx = s.questionIndex + 1
        _state.value = s.copy(
            questionIndex = newIdx,
            question = s.questions.getOrNull(newIdx),
            selectedIndex = null,
            answered = false,
            correct = null,
            timeLeft = 10
        )
        startTimer()
    }

    private fun finishLevel(won: Boolean) {
        if (won) {
            repo.markLevelCompleted(_state.value.level)
        }
        _state.value = _state.value.copy(finished = true, won = won)
        onLevelFinished?.invoke(won)
    }

    private fun cancelJobs() {
        timerJob?.cancel()
        nextJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}
