package com.qpay.customer.ui.setup_complete

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.SetupCompleteBinding
import com.qpay.customer.ui.common.BaseFragment

class SetupCompleteFragment  : BaseFragment<SetupCompleteBinding, SetupCompleteViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_setup_complete
    override val viewModel: SetupCompleteViewModel by viewModels { viewModelFactory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnContinue.setOnClickListener {
            navController.navigate(SetupCompleteFragmentDirections.actionSetupCompleteToHome())
        }
    }

}