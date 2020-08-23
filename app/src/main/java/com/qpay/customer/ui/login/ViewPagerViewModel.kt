package com.qpay.customer.ui.login

import androidx.lifecycle.ViewModel
import com.qpay.customer.R
import com.qpay.customer.prefs.PreferencesHelper
import javax.inject.Inject

class ViewPagerViewModel @Inject constructor(private val preferencesHelper: PreferencesHelper) :
    ViewModel() {
    val slideDataList = listOf<SlideData>(
        SlideData(R.drawable.slider_image_1, "Transfer your Money", "Easy, Fast and Secure Way"),
        SlideData(R.drawable.slider_image_1, "Transfer your Money", "Easy, Fast and Secure Way"),
        SlideData(R.drawable.slider_image_1, "Transfer your Money", "Easy, Fast and Secure Way"),
        SlideData(R.drawable.slider_image_1, "Transfer your Money", "Easy, Fast and Secure Way"),
        SlideData(R.drawable.slider_image_1, "Transfer your Money", "Easy, Fast and Secure Way")
    )

    inner class SlideData(
        var slideImage: Int,
        var textTitle: String,
        var descText: String
    )
}