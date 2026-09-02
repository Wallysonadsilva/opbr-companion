package com.guide.opbr_companion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Popup listing a character's alternative medal sets.
 *
 * Built on the same [AlertDialog] shell as [DialogAlertInfo] - 16dp corners and the
 * orange confirm button - so it reads as part of the same app rather than a new style.
 *
 * Each set is a labelled group: the label sits above a row of that set's medals, so the
 * icons read as belonging to the heading directly over them. Dismissal works three ways -
 * the button, the system back gesture, and a tap on the dimmed scrim - the latter two
 * coming from AlertDialog's own behaviour.
 */
@Composable
fun AlternativeSetsDialog(
    altSets: List<List<String>>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Alternative Sets",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                altSets.forEachIndexed { index, set ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // A single alternative needs no heading beyond the dialog title, so the
                    // label only appears when there are several sets to tell apart.
                    if (altSets.size > 1) {
                        Text(
                            text = "Option ${index + 1}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )
                    }

                    // weight() rather than a fixed dp size so three medals always fit the
                    // dialog width on small screens instead of overflowing its edges.
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        set.forEach { url ->
                            Box(modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = rememberCrossfadePainter(url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                )
                            }
                        }
                        // Keeps a short final row left-aligned with the rows above it.
                        repeat(3 - set.size.coerceAtMost(3)) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        },
        confirmButton = {
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
                    Text("Back", fontSize = 18.sp)
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
