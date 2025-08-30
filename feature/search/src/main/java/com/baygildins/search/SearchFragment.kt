package com.baygildins.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.baygildins.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")
    private val searchViewModel: SearchViewModel by viewModels()

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
                                    (activity as com.baygildins.navigation.Navigator).navigateSearchToMediaDetailsWithId(
                                        imdbID
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        val root = binding.root
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



