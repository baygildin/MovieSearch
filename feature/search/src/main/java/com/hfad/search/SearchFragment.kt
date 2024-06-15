package com.hfad.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hfad.search.databinding.FragmentSearchBinding
import com.hfad.search.model.SearchBySearch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")
    private val searchViewModel: SearchViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs<SearchFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                MaterialTheme {
                    Surface {
                        view?.let {
                            SearchFragmentContent(
                                searchViewModel = searchViewModel,
                                navigateToDetails = { imdbID ->
                                    (activity as com.hfad.navigation.Navigator).navigateSearchToMediaDetailsWithId(
                                        imdbID
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@Composable
fun SearchFragmentContent(searchViewModel: SearchViewModel, navigateToDetails: (String) -> Unit) {
    val query = remember { mutableStateOf("") }
    val searchResults = searchViewModel.searchResults.collectAsState().value

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Color(0xFF1D1C25),
            onBackground = Color(0xFFFAEFD9),
            primary = Color(0xFF1D1C25),
            onPrimary = Color(0xFF1D1C25)
        )
    ) {
        Surface(
            color = Color(0xFF1D1C25),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Movie Search",
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = Color(0xFFE0D9D9),
                    fontSize = 20.sp,
                )
                TextField(
                    value = query.value,
                    onValueChange = {
                        query.value = it
                        searchViewModel.searchMediaWithTitle(it)
                    },
                    placeholder = { Text("Введите название",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF8A8F99)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = TextStyle(
                        color = Color(0xFF233543),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color(0xFF233543),
                        backgroundColor = Color(0xFFE4FFFF),
                        cursorColor = Color(0xFFE0D9D9),
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
}
@Composable
fun ResultsList(result: SearchBySearch, navigateToDetails: (String) -> Unit) {
    Column {
        result.Search?.forEach { item ->
            Button(
                onClick = {
                    navigateToDetails(item.imdbID)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color(0xFFE0D9D9)
                )
            ) {
                Text(
                    text = "${item.Title} (${item.Year})",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                )
            }
        }
    }
}