package com.qpay.customer.ui.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import com.daimajia.slider.library.SliderLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.LayoutOperatorSelectionBinding
import com.qpay.customer.databinding.SignInBinding
import com.qpay.customer.databinding.ViewPagerBinding
import com.qpay.customer.ui.NavigationHost
import com.qpay.customer.ui.common.BaseFragment

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


        viewDataBinding.btnProceed.isEnabled = false
        viewDataBinding.btnProceed.setBackgroundColor(Color.parseColor("#808080"))

        viewDataBinding.etMobileNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewDataBinding.etMobileNo.text.toString().length == 11) {
                    viewDataBinding.btnProceed.setBackgroundColor(Color.parseColor("#70DB44"))
                    viewDataBinding.btnProceed.isEnabled = true
                } else {
                    viewDataBinding.btnProceed.isEnabled = false
                    viewDataBinding.btnProceed.setBackgroundColor(Color.parseColor("#808080"))
                }
            }

        })





        viewDataBinding.btnProceed.setOnClickListener {

            openOperatorSelectionDialog()

            // navController().navigate(SignInFragmentDirections.actionSignInFragmentToOtpSignInFragment())
        }
    }

    private fun openOperatorSelectionDialog() {
        val bottomsheetDialog = BottomSheetDialog(requireActivity())
        val binding = DataBindingUtil.inflate<LayoutOperatorSelectionBinding>(
            layoutInflater,
            R.layout.layout_operator_selection,
            null,
            false
        )
        bottomsheetDialog.setContentView(binding.root)


        binding.btnBanglalink.setOnClickListener {
            redirectToTermsAndConditionsScreen(bottomsheetDialog)
        }

        binding.btnGrameenphone.setOnClickListener {
            redirectToTermsAndConditionsScreen(bottomsheetDialog)
        }

        binding.btnRobi.setOnClickListener {
            redirectToTermsAndConditionsScreen(bottomsheetDialog)
        }

        binding.btnTeletalk.setOnClickListener {
            redirectToTermsAndConditionsScreen(bottomsheetDialog)
        }
        bottomsheetDialog.show()
    }

    private fun redirectToTermsAndConditionsScreen(dialog: BottomSheetDialog) {
        dialog.dismiss()
        navController().navigate(SignInFragmentDirections.actionSignInFragmentToTermsFragment())
    }

}