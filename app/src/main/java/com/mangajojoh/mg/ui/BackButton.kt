package com.mangajojoh.mg.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mangajojoh.mg.R

/**
 * Bouton retour en haut à gauche, position fixe.
 * Utilise back.png.
 */
@Composable
fun BackButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Retour",
        modifier = Modifier
            .padding(4.dp)
            .size(24.dp)   // <-- réduit de 40 à 24 dp
            .clickable { onClick() }
    )
}
