package com.qpay.customer.ui.registration

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qpay.customer.api.*
import com.qpay.customer.models.RegistrationResponse
import com.qpay.customer.repo.RegistrationRepository
import com.qpay.customer.ui.common.BaseViewModel
import com.qpay.customer.util.asFile
import com.qpay.customer.util.asFilePart
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val application: Application, private val repository: RegistrationRepository) : BaseViewModel(application) {

}
