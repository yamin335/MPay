package com.qpay.customer.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.qpay.customer.R
import com.qpay.customer.prefs.PreferencesHelper
import com.qpay.customer.ui.common.BaseViewModel
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val application: Application, private val preferencesHelper: PreferencesHelper) :
    BaseViewModel(application) {

}