package com.mangajojoh.mg.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mangajojoh.mg.ui.theme.MangaBlueDark
import com.mangajojoh.mg.ui.theme.MangaBlueDeep
import com.mangajojoh.mg.ui.theme.MangaBlueMid
import com.mangajojoh.mg.ui.theme.MangaBlue
import com.mangajojoh.mg.ui.theme.MangaGrayText
import com.mangajojoh.mg.ui.theme.MangaWhite

@Composable
fun LevelSelectScreen(
    unlockedLevel: Int,
    onLevelClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MangaBlueDark, MangaBlueDeep)))
            .systemBarsPadding()
    ) {
        Column(Modifier.fillMaxSize()) {
            // Header : back à gauche + titre centré
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onClick = onBack)

                Spacer(Modifier.width(8.dp))

                Text(
                    "Niveaux",
                    color = MangaWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items((1..100).toList()) { level ->
                    val unlocked = level <= unlockedLevel
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (unlocked) MangaBlue else MangaBlueMid
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable(enabled = unlocked) { onLevelClick(level) }
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$level",
                                color = if (unlocked) MangaWhite else MangaGrayText,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
