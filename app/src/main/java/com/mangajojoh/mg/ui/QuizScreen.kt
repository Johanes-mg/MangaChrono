package com.mangajojoh.mg.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mangajojoh.mg.ui.theme.MangaBlueDark
import com.mangajojoh.mg.ui.theme.MangaBlueDeep
import com.mangajojoh.mg.ui.theme.MangaBlueMid
import com.mangajojoh.mg.ui.theme.MangaCyanNeon
import com.mangajojoh.mg.ui.theme.MangaRed
import com.mangajojoh.mg.ui.theme.MangaRedDark
import com.mangajojoh.mg.ui.theme.MangaWhite

private val CorrectGreen = Color(0xFF2E7D32)

@Composable
fun QuizScreen(
    state: QuizUiState,
    onAnswer: (Int) -> Unit,
    onBack: () -> Unit
) {
    val q = state.question ?: return

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MangaBlueDark, MangaBlueDeep)))
            .systemBarsPadding()
    ) {
        Column(
            Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onClick = onBack)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Niv.${state.level}  ${state.questionNumber}/${state.totalQuestions}",
                    color = MangaWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Vies: ${state.lives}",
                    color = MangaRed,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "${state.timeLeft}s",
                    color = if (state.timeLeft <= 3) MangaRed else MangaCyanNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            LinearProgressIndicator(
                progress = { state.timeLeft / 10f },
                color = if (state.timeLeft <= 3) MangaRed else MangaCyanNeon,
                trackColor = MangaBlueMid,
                modifier = Modifier.fillMaxWidth().height(6.dp)
            )

            Spacer(Modifier.height(8.dp))

            // Énoncé
            Card(
                colors = CardDefaults.cardColors(containerColor = MangaBlueMid),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    q.enonce,
                    color = MangaWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            // 4 réponses en bas, uniformes
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                q.choix.forEachIndexed { i, choix ->
                    val isSelected = state.selectedIndex == i
                    val isCorrect = state.answered && i == q.bonneReponse
                    val isWrong = state.answered && isSelected && i != q.bonneReponse

                    val bgColor = when {
                        isCorrect -> CorrectGreen
                        isWrong -> MangaRedDark
                        else -> MangaBlueMid
                    }

                    Button(
                        onClick = { onAnswer(i) },
                        enabled = !state.answered,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = bgColor,
                            disabledContainerColor = bgColor,
                            disabledContentColor = MangaWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 64.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Lettre à gauche (largeur fixe pour aligner tous les choix)
                            Text(
                                "${('A' + i)}.",
                                color = MangaWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.width(32.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            // Choix collé à droite, multi-lignes alignées à droite
                            Text(
                                choix,
                                color = MangaWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
