package com.shahriar.shop.data.payloads

data class CreateProductPayload(
    val category: String?,
    val description: String?,
    val image: String?,
    val price: Double?,
    val title: String?
)