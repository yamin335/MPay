package com.qpay.customer.ui.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.RegistrationBinding
import com.qpay.customer.ui.common.BaseFragment

class RegistrationFragment  : BaseFragment<RegistrationBinding, RegistrationViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.btnGetOtp.setOnClickListener {
            navController().navigate(RegistrationFragmentDirections.actionRegistrationToOtp())
        }
    }
}