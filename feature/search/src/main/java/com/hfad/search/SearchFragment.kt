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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.hfad.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var omdbApi: OmdbApi
    private var _binding: FragmentSearchBinding? = null
    private val searchJob: Job? = null
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
                viewModel.setMovieTitle(s.toString())
                // Этот кобек onTextChanged вызывается при вводе каждого нового символа. Тут явно слишком часто вызывается
                // метод setMovieTitle
                viewModel.searchText = s.toString()
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
                val result = omdbApi.searchBySearch(title)
                binding.linearLayout.removeAllViews()
                result.Search.forEach { searchItem ->
                    val button = Button(requireContext())
                    button.setBackgroundColor(Color.TRANSPARENT)

                    button.setTextColor(Color.parseColor("#E0D9D9"))
                    button.text = """${searchItem.Title}
                                |${searchItem.Year}""".trimMargin()
                    button.setOnClickListener {
                        (activity as com.hfad.navigation.Navigator).navigateToMoveDetailsWithId(
                            searchItem.imdbID
                        )
                        /*viewModel.setMovieTitle(searchItem.imdbID)

                            val request = NavDeepLinkRequest.Builder
                                .fromUri("android-app://com.hfad.movie_details/movieDetailsFragmen=${id}t".toUri())
                                .build()
                            findNavController().navigate(request)
                            Log.e("getmovieError","listenerOfBtnList")*/
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