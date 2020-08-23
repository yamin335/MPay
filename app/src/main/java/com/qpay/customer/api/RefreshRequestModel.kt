package com.qpay.customer.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class RefreshRequestModel(
    @SerializedName("Token")
    @Expose
    var token: String? = null
) : Serializable
