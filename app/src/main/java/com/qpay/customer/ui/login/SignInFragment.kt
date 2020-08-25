package com.qpay.customer.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.daimajia.slider.library.SliderLayout
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.SignInBinding
import com.qpay.customer.databinding.ViewPagerBinding
import com.qpay.customer.models.RegistrationHelperModel
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment
import com.qpay.customer.util.showErrorToast

class SignInFragment : BaseFragment<SignInBinding, SignInViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_sign_in
    override val viewModel: SignInViewModel by viewModels {
        viewModelFactory
    }

    var navigationHost: NavigationHost? = null

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
        viewDataBinding.btnProceed.setOnClickListener {
            val mobileNumber = viewDataBinding.mobile.text.toString()
            if (mobileNumber.isBlank()) {
                viewDataBinding.mobile.requestFocus()
                showErrorToast(requireContext(), "Please enter a valid mobile number!")
            } else {
                val action = SignInFragmentDirections.actionSignInFragmentToOtpSignInFragment(
                    RegistrationHelperModel(mobileNumber)
                )
                navController().navigate(action)
            }
        }
    }
}