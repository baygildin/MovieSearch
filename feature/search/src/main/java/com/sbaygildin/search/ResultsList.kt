package com.sbaygildin.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbaygildin.search.model.SearchByTitle

@Composable
fun ResultsList(result: SearchByTitle, navigateToDetails: (String) -> Unit) {
    Column {
        result.search?.forEach { item ->
            Button(
                onClick = { navigateToDetails(item.imdbID) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = colorResource(id = R.color.cursor_color)
                )
            ) {
                Text(
                    text = "${item.title} (${item.year})",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                )
            }
        }
    }
}