package com.pro.quantnetworktesttask

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.pro.quantnetworktesttask.data.UnsplashPhoto
import com.pro.quantnetworktesttask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    private val photosAdapter by lazy(LazyThreadSafetyMode.NONE) { PhotoAdapter(diffUtil) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.mainRecyclerView.adapter = photosAdapter
    }

    override fun onStart() {
        super.onStart()

        photosAdapter.setOnItemClickListener { photo ->
            // new activity with full photo_url
            val intent = Intent(this, PhotoActivity::class.java)
            intent.putExtra("url", photo.urls.full)
            startActivity(intent)
        }

        lifecycleScope.launch {
            viewModel.photos.collectLatest { pagingData ->
                // update list
                photosAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            photosAdapter.loadStateFlow.collectLatest { loadStates ->
                when(val currentState = loadStates.refresh){
                    is LoadState.Error -> {
                        Timber.d(currentState.error.message)
                        // show error view if page not load
                        currentState.error.message?.let { showErrorView(it) }
                        binding.loader.isVisible = false
                    }
                    is LoadState.Loading -> {
                        binding.loader.isVisible = true
                        // clear error message
                        viewModel.updateErrorMessage()
                    }
                    is LoadState.NotLoading -> {
                        binding.loader.isVisible = false
                    }
                }

            }
        }

        binding.errorView.retryButton.setOnClickListener {
            hideErrorView()
            Timber.d("retryButton")
            photosAdapter.refresh()
        }
    }

    private fun hideErrorView() {
        binding.mainRecyclerView.isVisible = true
        binding.errorView.root.isVisible = false
    }

    private fun showErrorView(message: String) {
        binding.errorView.errorMessage.text = message
        binding.errorView.root.isVisible = true
        binding.mainRecyclerView.isVisible = false
    }

    companion object {

        val diffUtil = object: DiffUtil.ItemCallback<UnsplashPhoto>() {

            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
                return oldItem == newItem
            }

        }
    }

}