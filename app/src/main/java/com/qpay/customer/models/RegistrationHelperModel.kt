package com.qpay.customer.models

import java.io.Serializable

data class RegistrationHelperModel(var mobile: String = "", var otp: String = "", var pinNumber: String = ""): Serializable