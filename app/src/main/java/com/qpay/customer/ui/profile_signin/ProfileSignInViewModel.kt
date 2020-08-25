package com.qpay.customer.ui.profile_signin

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.customer.api.*
import com.qpay.customer.models.RegistrationResponse
import com.qpay.customer.prefs.PreferencesHelper
import com.qpay.customer.repo.RegistrationRepository
import com.qpay.customer.ui.common.BaseViewModel
import com.qpay.customer.util.asFile
import com.qpay.customer.util.asFilePart
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileSignInViewModel @Inject constructor(private val application: Application, private val repository: RegistrationRepository) : BaseViewModel(application) {
    fun registerNewUser(mobileNumber: String, otp: String, password: String, fullName: String,
                        mobileOperator: String, nidNumber: String, nidFrontImage: Uri?,
                        nidBackImage: Uri?, userImage: Uri?): LiveData<RegistrationResponse> {
        val response: MutableLiveData<RegistrationResponse> = MutableLiveData()
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue("Can not connect to server! Please try again later.")
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {

                val frontImagePart = nidFrontImage?.asFile(application)?.asFilePart("NationalIdImageFront")
                val backImagePart = nidBackImage?.asFile(application)?.asFilePart("NationalIdImageBack")
                val userImagePart = userImage?.asFile(application)?.asFilePart("UserImage")

                when (val apiResponse = ApiResponse.create(repository.registrationRepo(mobileNumber, otp, password, fullName, mobileOperator, nidNumber, frontImagePart, backImagePart, userImagePart))) {
                    is ApiSuccessResponse -> {
                        response.postValue(apiResponse.body)
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }

        return response
    }
}