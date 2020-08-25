package com.qpay.customer.ui.on_boarding.tou

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.TouBinding
import com.qpay.customer.ui.common.BaseFragment

class TouFragment  : BaseFragment<TouBinding, TouViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_tou
    override val viewModel: TouViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.btnAgree.setOnClickListener {
            navController.navigate(TouFragmentDirections.actionTouToSetup())
        }
    }
}