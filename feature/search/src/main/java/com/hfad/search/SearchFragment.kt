package com.hfad.search
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hfad.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var omdbApi: OmdbApi
    private var _binding: FragmentSearchBinding? = null
    private val args: SearchFragmentArgs by navArgs<SearchFragmentArgs>()

    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setMovieTitle(s.toString())
                viewModel.searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.btnSearch.setOnClickListener {
            lifecycleScope.launch {
                try { //в репозиторий положить
                    val result = omdbApi.searchBySearch(viewModel.searchText)
                    binding.linearLayout.removeAllViews()
                    result.Search.forEach { searchItem ->
                        val button = Button(requireContext())
                        button.setBackgroundColor(Color.TRANSPARENT)

                        button.setTextColor(Color.parseColor("#E0D9D9"))
                        button.text = """${searchItem.Title}
                            |${searchItem.Year}""".trimMargin()
                        button.setOnClickListener {
                            (activity as com.hfad.navigation.Navigator).navigateSearchToMovieDetailsWithId(
                                searchItem.imdbID
                            )}
                        binding.linearLayout.addView(button)
                    }
                } catch (e: Exception) {
                    binding.tvFullInfo.text = "Error occurred: ${e.message}"
                }
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}