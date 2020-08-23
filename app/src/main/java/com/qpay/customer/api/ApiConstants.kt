package com.qpay.customer.api


object ApiEndPoint {
    /*Login*/
    const val ENDPOINT_LOGIN = "accounts/login"
    const val ENDPOINT_REFRESH_TOKEN = "accounts/refresh"
    const val ENDPOINT_DEVICE_CHANGE = "accounts/device"

    /*Profile*/
    const val ENDPOINT_PROFILE = "profile"
    const val ENDPOINT_LOGOUT = "accounts/logout"
    const val ENDPOINT_PROFILE_WALLET_BALANCE = "profile/wallet-balance"
    const val ENDPOINT_PROFILE_BANK_ACCOUNT_BALANCE = "profile/account-balance"
    const val ENDPOINT_PROFILE_PHONE_VERIFY = "profile/{phone}/verify"
    const val ENDPOINT_PROFILE_PHONE_RESEND_VERIFICATION = "profile/{phone}/resend-verification"
    const val ENDPOINT_PROFILE_BANK_ACCOUNTS = "profile/bank-accounts"
}

object Params {
    const val CUS_IMAGE_FILE = "CustomerImageFile"
    const val SENDER_IMAGE_FILE = "SenderImageFile"
    const val USER_IMAGE_FILE = "UserImage"
    const val NID_IMAGE_FILE = "NationalIdImage"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_TOKEN_EXPIRE = 401
    const val CODE_UNAUTHORIZED = 403
    const val CODE_VALIDATION_ERROR = 400
    const val CODE_DEVICE_CHANGE = 451
    const val CODE_FIRST_LOGIN = 426
}
object UserRole{
    const val AGENT = "agent"
    const val CUSTOMER = "customer"
}


object ValidationError {
    const val ERROR_MEMBER_ID = "MemberId"
    const val ERROR_MOBILE_NUMBER = "PhoneNumber"
    const val ERROR_PIN = "Pin"
    const val ERROR_PASSWORD = "Password"
    const val ERROR_BANK_ACCOUNT_NUMBER = "BankAccountNumber"
}

const val STATEMENT_PAGE_SIZE = 10

/** Constants for pages used in NotificationDataSource  */
val PREVIOUS_PAGE_KEY_ONE = 1
val NEXT_PAGE_KEY_TWO = 2

const val AUTH_HEADER_NAME = "Authorization"
/*

const val API_URL = "${BuildConfig.BASE_URL}/api/v${BuildConfig.API_VERSION}/"

const val MEDIA_URL = BuildConfig.BASE_URL*/
