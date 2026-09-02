package com.guide.opbr_companion.ui.theme

import androidx.compose.ui.graphics.Color

val ColumnBackgroundLight = Color(0xFFE5E5E9)
val ColumnBackgroundDark = Color(0xFF2C2C2E)

// Accent used for links and card titles. The app previously used a single 0xFF007FFF
// for both themes, which fell below the WCAG AA 4.5:1 contrast minimum on each card
// background (3.05:1 on light, 3.62:1 on dark). These two keep the same blue identity
// while clearing AA: 4.76:1 on the light card, 5.30:1 on the dark one.
val AccentBlueLight = Color(0xFF0060CC)
val AccentBlueDark = Color(0xFF4DA3FF)
