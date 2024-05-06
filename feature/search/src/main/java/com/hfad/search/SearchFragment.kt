package com.hfad.search
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var viewModel: SearchViewModel
    @Inject
    lateinit var omdbApi: OmdbApi
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //viewModel с провайдером потому что это начальная страница, и класс не будет имееть параметров. Наверное, в других будут параметры, поэтому юзай фактори
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root


        with(binding)
        {

            btnStartSearch.setOnClickListener {
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.movie_details/movieDetailsFragment".toUri())
                    .build()
                findNavController().navigate(request)}
            btnEpisodesList.setOnClickListener{
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.show_episodes/ShowEpisodesFragment".toUri())
                    .build()
                findNavController().navigate(request)
            }
        }
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnFullInfo.setOnClickListener {
            binding.tvFullInfo.text = binding.etMessage.toString()
            binding.tvFullInfo.text = viewModel.searchText
            lifecycleScope.launch {
                try {
                    val result = omdbApi.searchByTitle(viewModel.searchText).toString()
                    binding.searchTextView.text = result
                    binding.tvFullInfo.text = result
                } catch (e: Exception) {
                    Log.e("getmovieError", "$e в строчке val result = omdbApi.searchByTitle")
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