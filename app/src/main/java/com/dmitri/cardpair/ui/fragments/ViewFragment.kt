package com.dmitri.cardpair.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.dmitri.cardpair.R
import com.dmitri.cardpair.databinding.FragmentViewBinding


class ViewFragment : Fragment(R.layout.fragment_view) {
    private lateinit var binding : FragmentViewBinding
    private val args : ViewFragmentArgs.Companion = ViewFragmentArgs

    private var viewUrl : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewBinding.bind(view)

        getViewUrl()
        showView()

        requireActivity().onBackPressedDispatcher.
            addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.view.canGoBack()) {
                        binding.view.goBack();
                    }
                }
            })
    }

    private fun getViewUrl() {
        viewUrl = arguments?.let { args.fromBundle(it).viewUrl }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showView() {
        if (!viewUrl.isNullOrBlank()) {
            binding.view.settings.domStorageEnabled = true
            binding.view.settings.javaScriptEnabled = true
            binding.view.settings.javaScriptCanOpenWindowsAutomatically = true
            binding.view.settings.setSupportZoom(true)
            binding.view.settings.builtInZoomControls = false
            binding.view.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
            binding.view.settings.databaseEnabled = true
            binding.view.settings.allowFileAccess = true
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.view, true)
            binding.view.webChromeClient = WebChromeClient()
            binding.view.webViewClient = WebViewClient()
            viewUrl?.let { binding.view.loadUrl(it) }
        }
    }


}