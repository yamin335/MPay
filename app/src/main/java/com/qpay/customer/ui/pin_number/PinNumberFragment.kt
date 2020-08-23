package com.qpay.customer.ui.pin_number

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.PinNumberBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment

class PinNumberFragment : BaseFragment<PinNumberBinding, PinNumberViewModel>() {

    override val bindingVariable: Int
        get() =  BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pin_number

    override val viewModel: PinNumberViewModel by viewModels { viewModelFactory }


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
        viewDataBinding.btnSubmit.setOnClickListener {
            navController().navigate(PinNumberFragmentDirections.actionPinNumberFragmentToPermissionsFragment())
        }
    }

}