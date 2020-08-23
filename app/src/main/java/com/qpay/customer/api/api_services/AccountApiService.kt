/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qpay.customer.api.api_services

import androidx.lifecycle.LiveData
import com.qpay.customer.api.ApiEndPoint
import com.qpay.customer.api.ApiResponse
import com.qpay.customer.api.TokenInformation
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * REST API access points which need no authentication header
 */
interface AccountApiService {

    @FormUrlEncoded
    @POST(ApiEndPoint.ENDPOINT_LOGIN)
    fun loginUser(@FieldMap map: Map<String, String>): LiveData<ApiResponse<TokenInformation>>

    @FormUrlEncoded
    @POST(ApiEndPoint.ENDPOINT_REFRESH_TOKEN)
    fun refresh(@FieldMap map: Map<String, String>): Call<TokenInformation>
}
