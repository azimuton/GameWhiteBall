package com.azimuton.gamewhiteball.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.databinding.FragmentWebBinding

class WebFragment : Fragment() {
    private lateinit var binding : FragmentWebBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wvGoogle.loadUrl("https://google.com/")
        binding.wvGoogle.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                displayZoomControls = false
                builtInZoomControls = false
                loadsImagesAutomatically = true
                cacheMode = WebSettings.LOAD_NO_CACHE
                binding.wvGoogle.webViewClient = WebViewClient()
                binding.wvGoogle.webChromeClient = object : WebChromeClient(){
                    override fun onProgressChanged(
                        view: WebView,
                        newProgress: Int
                    ) {
                        super.onProgressChanged(view, newProgress)
                        binding.pbLoading.progress = newProgress
                        if (newProgress < 100 && binding.pbLoading.visibility == ProgressBar.INVISIBLE) {
                            binding.pbLoading.visibility = ProgressBar.VISIBLE
                            binding.tvLoading.visibility = ProgressBar.VISIBLE
                        }
                        if (newProgress == 100) {
                            binding.pbLoading.visibility = ProgressBar.INVISIBLE
                            binding.tvLoading.visibility = ProgressBar.INVISIBLE
                        }
                    }
                }
            }
        }.canGoBack()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MAIN.navController.navigate(R.id.action_webFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        binding.wvGoogle.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK
                && event.action == MotionEvent.ACTION_UP
                && binding.wvGoogle.canGoBack()) {
                binding.wvGoogle.goBack()
                return@OnKeyListener true
            }
            false
        })
    }
}