package com.mangajojoh.mg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mangajojoh.mg.ui.LevelSelectScreen
import com.mangajojoh.mg.ui.MenuScreen
import com.mangajojoh.mg.ui.QuizScreen
import com.mangajojoh.mg.ui.QuizViewModel

sealed class Screen {
    data object Menu : Screen()
    data object LevelSelect : Screen()
    data class Quiz(val level: Int) : Screen()
}

@Composable
fun AppNavigation(vm: QuizViewModel = viewModel()) {
    var screen by remember { mutableStateOf<Screen>(Screen.Menu) }
    var refreshKey by remember { mutableStateOf(0) }

    when (val s = screen) {
        is Screen.Menu -> {
            MenuScreen(
                onPlay = { screen = Screen.LevelSelect }
            )
        }

        is Screen.LevelSelect -> {
            val unlocked = remember(refreshKey) { vm.getUnlocked() }
            LevelSelectScreen(
                unlockedLevel = unlocked,
                onLevelClick = { level ->
                    vm.setCurrentLevel(level)
                    screen = Screen.Quiz(level)
                },
                onBack = { screen = Screen.Menu }
            )
        }

        is Screen.Quiz -> {
            val state by vm.state.collectAsState()

            LaunchedEffect(s.level) {
                vm.startLevel(s.level)
            }

            DisposableEffect(s.level) {
                vm.onLevelFinished = { _ ->
                    refreshKey++
                    screen = Screen.LevelSelect
                }
                onDispose {
                    vm.onLevelFinished = null
                }
            }

            QuizScreen(
                state = state,
                onAnswer = vm::selectAnswer,
                onBack = {
                    refreshKey++
                    screen = Screen.LevelSelect
                }
            )
        }
    }
}
