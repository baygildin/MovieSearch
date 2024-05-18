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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hfad.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {
    @Inject
    lateinit var omdbApi: OmdbApi
    private var _binding: FragmentSearchBinding? = null
    private val searchJob: Job? = null
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
                searchJob?.cancel()
                lifecycleScope.launch {
                    delay(700)
                    searchMediaWithTitle(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        return view
    }
    private fun searchMediaWithTitle(title: String) {
        // TODO Распилить этот огромный метод, который делает кучу всего
        lifecycleScope.launch {
            try {
                // TODO  Избавиться  от использования api в фрагменте
                val result = omdbApi.searchByTitle(title)
                binding.linearLayout.removeAllViews()
                result.Search.forEach { searchItem ->
                    val button = Button(requireContext())
                    button.setBackgroundColor(Color.TRANSPARENT)

                    button.setTextColor(Color.parseColor("#E0D9D9"))
                    button.text = """${searchItem.Title}
                                |${searchItem.Year}""".trimMargin()
                    button.setOnClickListener {
                        (activity as com.hfad.navigation.Navigator).navigateSearchToMediaDetailsWithId(
                            searchItem.imdbID
                        )
                    }
                    binding.linearLayout.addView(button)
                }
            } catch (e: Exception) {
                Log.e("getmovieError", "$e в строчке val result = omdbApi.searchByTitle")
                // В случае ошибки отображаем сообщение об ошибке
                binding.tvFullInfo.text = "Error occurred: ${e.message}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//        binding.btnSearch.setOnClickListener {
//            lifecycleScope.launch {
//                try { //в репозиторий положить
//                    val result = omdbApi.searchByTitle(viewModel.searchText)
//                    binding.linearLayout.removeAllViews()
//                    result.Search.forEach { searchItem ->
//                        val button = Button(requireContext())
//                        button.setBackgroundColor(Color.TRANSPARENT)
//
//                        button.setTextColor(Color.parseColor("#E0D9D9"))
//                        button.text = """${searchItem.Title}
//                            |${searchItem.Year}""".trimMargin()
//                        button.setOnClickListener {
//                            (activity as com.hfad.navigation.Navigator).navigateSearchToMovieDetailsWithId(
//                                searchItem.imdbID
//                            )}
//                        binding.linearLayout.addView(button)
//                    }
//                } catch (e: Exception) {
//                    binding.tvFullInfo.text = "Error occurred: ${e.message}"
//                }
//            }
//        }
//        return view
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}