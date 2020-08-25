package com.qpay.customer.api

import com.qpay.customer.models.RegistrationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * REST API access points
 */
interface ApiService {

    @Multipart
    @POST(ApiEndPoint.REGISTRATION)
    suspend fun registration(@Part("MobileNumber") mobileNumber: RequestBody,
                             @Part("Otp") otp: RequestBody,
                             @Part("Password") password: RequestBody,
                             @Part("FullName") fullName: RequestBody,
                             @Part("MobileOperator") mobileOperator: RequestBody,
                             @Part("NationalIdNumber") nidNumber: RequestBody,
                             @Part nidFrontImage: MultipartBody.Part?,
                             @Part nidBackImage: MultipartBody.Part?,
                             @Part userImage: MultipartBody.Part?): Response<RegistrationResponse>
}
