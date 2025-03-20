package com.example.opbr_companion.ui.screen



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
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
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .padding(24.dp)
            .background(color = Color.Gray)
    ) {
        Image(
            painter = rememberImagePainter(support.supportImg),
            contentDescription = "Support Image",
            modifier = Modifier.fillMaxWidth().height(150.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = support.supportTags.joinToString(", "),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    // Create sample data
    val sampleSupports = listOf(
        Support(
            id = 1,
            supportImg = "https://bwpgdqzcvqroakybcgjd.supabase.co/storage/v1/object/public/Supports//IMG_2148.jpeg",
            supportColor = "Blue",
            supportTags = listOf("Attacker", "Straw Hat Pirates", "Zoan", "New World")
        ),
        Support(
            id = 2,
            supportImg = "https://bwpgdqzcvqroakybcgjd.supabase.co/storage/v1/object/public/Supports//IMG_2148.jpeg",
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