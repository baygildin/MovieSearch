package com.hfad.search

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(Color.parseColor("#E0D9D9"))
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