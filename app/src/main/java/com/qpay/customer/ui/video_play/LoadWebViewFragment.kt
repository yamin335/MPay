package com.qpay.customer.ui.video_play

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.qpay.customer.BR
import com.qpay.customer.R
import com.qpay.customer.databinding.WebViewBinding
import com.qpay.customer.ui.common.BaseFragment


class LoadWebViewFragment: BaseFragment<WebViewBinding, LoadWebViewViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_load_web_view
    override val viewModel by viewModels<LoadWebViewViewModel>{
        viewModelFactory
    }


    private val url by lazy {
        //arguments?.let { LoadWebViewFragmentArgs.fromBundle(it).url }
        "file:///android_asset/math_8_4_1_q_1_ka/MATH8_4.1Q1KA_player.html"
    }
    private val title by lazy {
        //arguments?.let { LoadWebViewFragmentArgs.fromBundle(it).title }
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //activity?.title = title

        val webSettings = viewDataBinding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowContentAccess = true
        webSettings.domStorageEnabled = true
        webSettings.pluginState = WebSettings.PluginState.ON_DEMAND;
        //viewDataBinding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.allowFileAccessFromFileURLs = true
            webSettings.allowUniversalAccessFromFileURLs = true
        }

        webSettings.builtInZoomControls = true

        url.let {
            viewDataBinding.webView.loadUrl(it)
        }

        viewDataBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                try {
                    viewDataBinding.progressBar.progress = newProgress
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        }


        viewDataBinding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                viewDataBinding.progressBar.visibility = View.VISIBLE

            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                var isHandled = false
                try {
                    if (url != null) {
                        Log.i("LoadWebViewFragment", url)
                        when {

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return isHandled
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewDataBinding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        viewDataBinding.webView.webViewClient = null
        super.onDestroyView()
    }
}