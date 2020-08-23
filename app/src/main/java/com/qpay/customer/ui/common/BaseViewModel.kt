package com.qpay.idoctorchamber.ui.common

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout

abstract class BaseViewModel : ViewModel() {

    var navController: NavController? = null

    fun afterTextChanged(til: TextInputLayout) {
        til.error = null
        til.isErrorEnabled = false
    }
}
