package com.qpay.customer.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.daimajia.slider.library.SliderLayout
import com.qpay.customer.R
import com.qpay.customer.BR
import com.qpay.customer.databinding.ViewPagerBinding
import com.qpay.customer.ui.common.BaseFragment

class ViewPagerFragment : BaseFragment<ViewPagerBinding, ViewPagerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_view_pager
    override val viewModel: ViewPagerViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.slideDataList.forEach { slideData ->
            val slide = SliderView(requireContext())
            slide.sliderTextTitle = slideData.textTitle
            slide.sliderTextDescription = slideData.descText
            slide.sliderImage(slideData.slideImage)
            viewDataBinding.sliderLayout.addSlider(slide)
        }

        // set Slider Transition Animation
        viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default)
        viewDataBinding.sliderLayout.stopAutoCycle()

        // set Slider Transition Animation
        //viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default)
        //viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion)
        //viewDataBinding.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)


        viewDataBinding.btnLogin.setOnClickListener {
            navController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToSignInFragment())
        }

        updateStatusBarBackgroundColor("#1E4356")
    }
}