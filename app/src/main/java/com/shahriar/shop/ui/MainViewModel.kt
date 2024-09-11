package com.shahriar.shop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahriar.shop.api.ApiClient
import com.shahriar.shop.api.Resource
import com.shahriar.shop.data.products.ProductsResponse
import com.shahriar.shop.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    // Saving only NewsResponse instead of the Resource wrapper
    val productsResponse = MutableStateFlow<ProductsResponse?>(null)

    // Separate variables to handle loading and error states
    val isLoading = MutableStateFlow(true)

    // Separate variables to handle loading and error states
    val errorMessage = MutableStateFlow<String?>(null)

    var isViewed = false

    private val repository = ShopRepository(ApiClient.api)


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            repository.getProducts(
                limit = 100,
                sort =null
            ).collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        isLoading.value = true // Set loading state
                    }

                    is Resource.Success -> {
                        isLoading.value = false // Stop loading
                        productsResponse.value = resource.data // Set news data
                    }

                    is Resource.Error -> {
                        isLoading.value = false // Stop loading
                        errorMessage.value = resource.message // Set error message
                    }
                }
            }
        }
    }
}