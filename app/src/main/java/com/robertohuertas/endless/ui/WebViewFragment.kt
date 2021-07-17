package com.robertohuertas.endless.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.robertohuertas.endless.R
import com.robertohuertas.endless.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    lateinit var binding: FragmentWebViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWebViewBinding.inflate(inflater, container,false)
        binding.webview.loadUrl("https://selfregistration.cowin.gov.in/")

        // Enable Javascript
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        binding.webview.webViewClient = WebViewClient()
        return binding.root
    }
}