package com.qpay.customer.api

import com.qpay.customer.api.Api.API_VERSION
import com.qpay.customer.api.Api.DIRECTORY
import com.qpay.customer.api.Api.REPO

object Api {
    const val PROTOCOL = "http"
    const val API_ROOT = "210.4.67.205:6107"
    const val API_ROOT_URL = "$PROTOCOL://$API_ROOT"
    const val REPO = "api"
    const val API_VERSION = "v1"
    const val DIRECTORY = "account"
}

object ApiEndPoint {
    /* Registration develop */
    const val REGISTRATION = "/$REPO/$API_VERSION/${DIRECTORY}registrationaccount/registration"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_TOKEN_EXPIRE = 401
    const val CODE_UNAUTHORIZED = 403
    const val CODE_VALIDATION_ERROR = 400
    const val CODE_DEVICE_CHANGE = 451
    const val CODE_FIRST_LOGIN = 426
}

object ApiCallStatus {
    const val LOADING = 0
    const val SUCCESS = 1
    const val ERROR = 2
    const val EMPTY = 3
}
