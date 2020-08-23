package com.qpay.customer.ui.otp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.OtpBinding
import com.qpay.customer.ui.common.BaseFragment

class OtpFragment  : BaseFragment<OtpBinding, OtpViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_otp
    override val viewModel: OtpViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnVerify.setOnClickListener {
            navController().navigate(OtpFragmentDirections.actionOtpToTou())
        }
    }
}