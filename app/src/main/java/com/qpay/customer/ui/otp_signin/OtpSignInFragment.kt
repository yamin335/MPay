package com.qpay.customer.ui.otp_signin

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.OtpSignInBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment

class OtpSignInFragment : BaseFragment<OtpSignInBinding, OtpSignInViewModel>(), PermissionListener {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_otp_sign_in

    override val viewModel: OtpSignInViewModel by viewModels { viewModelFactory }

    var navigationHost: NavigationHost? = null

    var START_TIME_IN_MILLI_SECONDS = 180000L
    lateinit var countdown_timer: CountDownTimer
    var repeater = 0
    override fun onPermissionGranted() {
        navController().navigate(OtpSignInFragmentDirections.actionOtpSignInFragmentToPinNumberFragment())
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStatusBarBackgroundColor("#1E4356")
        val host = navigationHost ?: return
        viewDataBinding.toolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
        viewDataBinding.btnSubmit.setOnClickListener {
            TedPermission.with(requireContext())
                .setPermissionListener(this)
                .setDeniedMessage(getString(R.string.if_you_reject_these_permission_the_app_wont_work_perfectly))
                .setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                ).check()
        }

        viewDataBinding.btnResend.setOnClickListener {
            startTimer(START_TIME_IN_MILLI_SECONDS)
        }

        viewDataBinding.btnSubmit.isEnabled = false
        viewDataBinding.btnSubmit.setBackgroundColor(Color.parseColor("#808080"))
        viewDataBinding.etOtpCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewDataBinding.etOtpCode.text.toString().length == 6) {
                    viewDataBinding.btnSubmit.isEnabled = true
                    viewDataBinding.btnSubmit.setBackgroundColor(Color.parseColor("#70DB44"))
                } else {
                    viewDataBinding.btnSubmit.isEnabled = false
                    viewDataBinding.btnSubmit.setBackgroundColor(Color.parseColor("#808080"))
                }
            }

        })


    }

//    private fun pauseTimer() {
//
//        button.text = "Start"
//        countdown_timer.cancel()
//        isRunning = false
//        reset.visibility = View.VISIBLE
//    }

    private fun startTimer(time_in_milli_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_milli_seconds, 1000) {
            override fun onFinish() {
                viewDataBinding.btnResend.isEnabled = ++repeater < 3
            }

            override fun onTick(time_in_milli_seconds: Long) {
                updateTextUI(time_in_milli_seconds)
            }
        }
        countdown_timer.start()

        viewDataBinding.btnResend.isEnabled = false
    }

//    private fun resetTimer() {
//        time_in_milli_seconds = START_MILLI_SECONDS
//        updateTextUI()
//        reset.visibility = View.INVISIBLE
//    }

    private fun updateTextUI(time_in_milli_seconds: Long) {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60
        viewDataBinding.minuteView.text = "$minute"
        viewDataBinding.secondView.text = "$seconds"
    }

}