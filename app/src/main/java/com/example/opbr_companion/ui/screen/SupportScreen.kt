package com.example.opbr_companion.ui.screen



import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.opbr_companion.R
import com.example.opbr_companion.model.Support
import com.example.opbr_companion.viewmodel.SupportViewModel


@Composable
fun SupportScreen(viewModel: SupportViewModel = viewModel() ) {
    val supportList by viewModel.supportList.collectAsState()

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .clip(RoundedCornerShape(10.dp))
    ){
        Text(
            text = "Support",
            fontSize = 24.sp
        )

        // Convert to explicit List<Support> type
        val supports = supportList.toList()

        // Using item lambda without type inference
        LazyColumn {
            items(supports.size) { index ->
                SupportItem(supports[index])
            }
        }
    }
}

@Composable
fun SupportItem(support: Support) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = rememberImagePainter(support.supportImg),
                contentDescription = "Support Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.FillBounds
            )
        }
        // Support tags and colour
        Row(
            modifier = Modifier.fillMaxWidth().padding(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .padding(12.dp)
                    .shadow(10.dp, shape = RoundedCornerShape(10.dp), clip = false)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
                    ,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.tag_icon),
                        contentDescription = "this will be a tag",
                        tint = Color(0xFFf48420)
                    )
                    Text(
                        text = "Tags:  ${support.supportTags.joinToString(", ")}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.circle_solid_icon),
                        contentDescription = "Color Icon",
                        tint = getColorFromName(support.supportColor),
                        modifier = Modifier.padding(start = 4.dp).size(16.dp)
                    )
                    Text(
                        text = "Color: ${support.supportColor}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    )
                }
            }
        }
    }
}


fun getColorFromName(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color.Green
        "yellow" -> Color.Yellow
        "orange" -> Color(0xFFFFA500)
        "purple" -> Color(0xFF800080)
        "black" -> Color.Black
        "white" -> Color.White
        "gray", "grey" -> Color.Gray
        else -> Color.Black// default dark gray

    }
}

@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    // Sample data
    val sampleSupports = listOf(
        Support(
            id = 1,
            supportImg = "",
            supportColor = "Blue",
            supportTags = listOf("Attacker", "Straw Hat Pirates", "Zoan", "New World")
        ),
        Support(
            id = 2,
            supportImg = "",
            supportColor = "Red",
            supportTags = listOf("Runner", "Straw Hat Pirates", "Paramecia", "New World")
        )
    )

    // Create a simplified version of your screen using the sample data
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        Text(
            text = "Support",
            fontSize = 24.sp
        )

        LazyColumn {
            items(sampleSupports.size) { index ->
                SupportItem(sampleSupports[index])
            }
        }
    }
}