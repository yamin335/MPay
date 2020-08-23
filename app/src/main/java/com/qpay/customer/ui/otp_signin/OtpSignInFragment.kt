package com.qpay.customer.ui.otp_signin

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.OtpSignInBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment

class OtpSignInFragment : BaseFragment<OtpSignInBinding, OtpSignInViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_otp_sign_in

    override val viewModel: OtpSignInViewModel by viewModels { viewModelFactory }

    var navigationHost: NavigationHost? = null

    var START_TIME_IN_MILLI_SECONDS = 180000L
    lateinit var countdown_timer: CountDownTimer
    var repeater = 0

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
            navController().navigate(OtpSignInFragmentDirections.actionOtpSignInFragmentToPinNumberFragment())
        }

        viewDataBinding.btnResend.setOnClickListener {
            startTimer(START_TIME_IN_MILLI_SECONDS)
        }
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