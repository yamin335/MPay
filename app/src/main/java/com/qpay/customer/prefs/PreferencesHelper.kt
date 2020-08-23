package com.qpay.customer.prefs

import com.qpay.customer.api.ProfileInfo
import com.qpay.customer.api.TokenInformation


interface PreferencesHelper {

    var isLoggedIn: Boolean

    var accessToken: String?

    var refreshToken: String?

    var phoneNumber: String?

    var userId: Int

    var userRole: String?

    var accessTokenExpiresIn: Long

    val isAccessTokenExpired: Boolean

    fun getAccessTokenHeader(): String

    fun getAuthHeader(token: String?): String

    fun logoutUser()

    fun saveToken(tokenInformation: TokenInformation)

    fun saveUserProfile(profile: ProfileInfo)

    fun getToken(): TokenInformation

    var validityLimiterMap: MutableMap<String, Long>?

    var language: String?
}
