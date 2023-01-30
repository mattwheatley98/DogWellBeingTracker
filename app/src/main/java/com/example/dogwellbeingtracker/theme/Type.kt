package com.example.dogwellbeingtracker.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dogwellbeingtracker.R

val SofiaSans = FontFamily(
    Font(R.font.sofia_sans_regular),
    Font(R.font.sofia_sans_bold),
    Font(R.font.sofia_sans_italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SofiaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SofiaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)