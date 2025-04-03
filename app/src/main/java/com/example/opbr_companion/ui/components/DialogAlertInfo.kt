package com.example.opbr_companion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogAlertInfo(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFf48420),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "OK",
                             fontSize = 18.sp
                        )
                    }
                }
            }
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    )
}



@Preview(showBackground = true)
@Composable
fun InfoDialogPreview() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        DialogAlertInfo(
            message = "Support percentages vary between users. Use these examples to guide your character choices and configurations.\n\nNote: More supports will be added in the future.",
            onDismiss = { showDialog = false }
        )
    }
}
