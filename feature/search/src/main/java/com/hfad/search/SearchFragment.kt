package com.hfad.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hfad.search.databinding.FragmentSearchBinding
import com.hfad.search.model.SearchBySearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")
    private val searchViewModel: SearchViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs<SearchFragmentArgs>()
    private var searchJob: Job? = null

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
        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            searchViewModel.searchResults.collect { result ->
                result?.fold(
                    onSuccess = { updateUI(it) },
                    onFailure = { showError(it) }
                )
                Log.d("myerror42", "setupobservers")
            }
        }
    }

    private fun setupListeners() {
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500)
                    val query = s.toString()
                    if (query.isNotEmpty()) {
                        searchViewModel.searchMediaWithTitle(query)
                        Log.d("280524", "setupListeners: Searching for $query")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateUI(result: SearchBySearch) {
        binding.linearLayout.removeAllViews()

        val searchItems = result.Search
        if (searchItems != null) {
            searchItems.forEach { searchItem ->
                val button = Button(requireContext()).apply {
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    setTextColor(android.graphics.Color.parseColor("#E0D9D9"))
                    text = "${searchItem.Title}\n${searchItem.Year}"
                    Log.d("280524", "updateUI: Adding button for ${searchItem.Title}")
                    setOnClickListener {
                        (activity as com.hfad.navigation.Navigator).navigateSearchToMediaDetailsWithId(
                            searchItem.imdbID
                        )
                    }
                }
                binding.linearLayout.addView(button)
            }
        } else {
            Log.e("SearchFragment", "updateUI: Search results are null")
        }
    }

    private fun showError(e: Throwable) {
        Log.e("getmovieError", "$e")
        binding.tvFullInfo.text = "Error occurred: ${e.message}"
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
                    modifier = Modifier.padding(vertical = 10.dp),
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
                        color = Color(0xFF2B3342)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color(0xFF233543),
                        backgroundColor = Color(0xFFE4FFFF),
                        cursorColor = Color(0xFFE0D9D9),
                        focusedIndicatorColor = Color(0xFFE0D9D9),
                        unfocusedIndicatorColor = Color(0xFFE0D9D9)
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchFragmentContent() {
//    MaterialTheme {
//        Surface {
//            SearchFragmentContent()
//        }
//    }
//}