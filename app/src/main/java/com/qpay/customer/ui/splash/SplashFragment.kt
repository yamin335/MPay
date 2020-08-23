package com.qpay.customer.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.SplashBinding
import com.qpay.customer.ui.common.BaseFragment

class SplashFragment : BaseFragment<SplashBinding, SplashViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_splash

    override val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAuthenticationState().observe(viewLifecycleOwner, Observer {
            it?.let { authenticateState ->
                when (authenticateState) {
                    SplashViewModel.AuthenticationState.AUTHENTICATED -> {
                        navController().navigate(R.id.action_splash_to_main)
                    }
                    SplashViewModel.AuthenticationState.UNAUTHENTICATED -> {
                        navController().navigate(R.id.action_splash_to_auth)
                        //redirectToNextScreen()
                    }
                }
            }
        })
    }
}