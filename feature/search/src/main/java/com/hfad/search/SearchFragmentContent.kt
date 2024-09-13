package com.hfad.search

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SearchFragmentContent(searchViewModel: SearchViewModel, navigateToDetails: (String) -> Unit) {
    val query = remember { mutableStateOf("") }
    val searchResults = searchViewModel.searchResults.collectAsState().value
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = colorResource(id = R.color.background_color),
            onBackground = colorResource(id = R.color.on_background_color),
            primary = colorResource(id = R.color.primary_color),
            onPrimary = colorResource(id = R.color.on_primary_color)
        )
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background_color))
                .verticalScroll(state = scrollState)
                .fillMaxSize()
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.txt_app_title),
                modifier = Modifier.padding(vertical = 20.dp),
                color = colorResource(id = R.color.text_color),//Color(0xFFE0D9D9)
                fontSize = 20.sp,
            )
            TextField(
                value = query.value,
                onValueChange = { newQuery ->
                    if (newQuery.matches(Regex("^[a-zA-Z0-9 ':!?&.-]*$"))) {
                        query.value = newQuery
                        searchJob?.cancel()
                        searchJob = coroutineScope.launch {
                            delay(700)
                            searchViewModel.searchMediaWithTitle(newQuery)
                        }
                    } else {
                        showToast(context, context.getString(R.string.toast_only_eng_chars))
                    }

                },
                placeholder = {
                    Text(
                        stringResource(id = R.string.message_hint),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(textAlign = TextAlign.Center),
                        color = colorResource(id = R.color.placeholder)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                textStyle = TextStyle(
                    color = colorResource(id = R.color.text_field_text_color),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorResource(id = R.color.text_field_text_color),
                    backgroundColor = colorResource(id = R.color.text_field_background_color),
                    cursorColor = colorResource(id = R.color.cursor_color),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            searchResults?.let {
                it.fold(
                    onSuccess = { results ->
                        ResultsList(results, navigateToDetails)
                    },
                    onFailure = { error ->
                        Text("Error: ${error.message}", color = Color.Red)
                    }
                )
            }
        }

    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}