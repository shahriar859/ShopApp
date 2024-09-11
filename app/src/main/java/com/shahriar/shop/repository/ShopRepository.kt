package com.shahriar.shop.repository

import android.util.Log
import com.shahriar.shop.api.Resource
import com.shahriar.shop.data.products.ProductsResponse
import com.shahriar.shop.service.ShopService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ShopRepository(private val api: ShopService) {
    suspend fun getProducts(
        limit: Number?,
        sort: String?
    ): Flow<Resource<ProductsResponse>> = flow {
        try {
            emit(Resource.Loading) // Emit loading state
            val response = api.getProducts(
                limit,
                sort
            ) // Make the network request
            Log.d("Repository", response.toString())

            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error")) // Emit error state
        }
    }.flowOn(Dispatchers.IO)



}