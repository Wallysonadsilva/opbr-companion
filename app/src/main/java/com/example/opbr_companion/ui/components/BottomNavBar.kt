package com.example.opbr_companion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opbr_companion.R

@Preview(showBackground = true)
@Composable
fun BottomNavBar() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Characters", "Support", "Medal Sets")
    val icons = listOf(
        R.drawable.group_icons,
        R.drawable.group_icons_2,
        R.drawable.star_icon
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEachIndexed { index, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { selectedItem = index }
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = item,
                    tint = if (selectedItem == index) Color.Blue else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item,
                    fontSize = 12.sp,
                    color = if (selectedItem == index) Color.Blue else Color.Gray
                )
            }
        }
    }
}


