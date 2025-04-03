package com.example.opbr_companion.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opbr_companion.R

@Composable
fun TopBar(
    title: String,
    onLeftIconClick: () -> Unit,
    onRightIconClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Left icon
        IconButton(
            onClick = onLeftIconClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.tune_icon),
                contentDescription = "Left Icon",
            )
        }

        // Dynamic title
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Right icon
        IconButton(
            onClick = onRightIconClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.info_icon),
                contentDescription = "Right Icon",
            )
        }
    }
}
