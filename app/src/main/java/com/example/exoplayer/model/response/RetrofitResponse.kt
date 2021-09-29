package com.example.exoplayer.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RetrofitResponse(
    @SerializedName("categories")
    @Expose
    val categoryResponses: MutableList<CategoryResponse>
)
