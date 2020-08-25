package com.qpay.customer.ui.pin_number

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.PinNumberBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment
import com.qpay.customer.ui.otp_signin.OtpSignInFragmentArgs
import com.qpay.customer.util.showErrorToast

class PinNumberFragment : BaseFragment<PinNumberBinding, PinNumberViewModel>() {

    override val bindingVariable: Int
        get() =  BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pin_number

    override val viewModel: PinNumberViewModel by viewModels { viewModelFactory }


    var navigationHost: NavigationHost? = null

    val args: PinNumberFragmentArgs by navArgs()

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
            val pin = viewDataBinding.pin.text.toString()
            val rePin = viewDataBinding.rePin.text.toString()
            when {
                pin.isBlank() -> {
                    viewDataBinding.pin.requestFocus()
                    showErrorToast(requireContext(), "Please enter a valid pin!")
                }
                rePin.isBlank() -> {
                    viewDataBinding.rePin.requestFocus()
                    showErrorToast(requireContext(), "Please enter your pin again!")
                }
                pin != rePin -> {
                    viewDataBinding.rePin.requestFocus()
                    showErrorToast(requireContext(), "Pin does not match")
                }
                pin.isNotBlank() && rePin.isNotBlank() && pin == rePin -> {
                    val helper = args.registrationHelper
                    helper.pinNumber = pin
                    val action = PinNumberFragmentDirections.actionPinNumberFragmentToPermissionsFragment(helper)
                    navController().navigate(action)
                }
            }
        }
    }

}