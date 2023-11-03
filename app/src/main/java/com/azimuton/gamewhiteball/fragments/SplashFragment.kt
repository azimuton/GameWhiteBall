package com.azimuton.gamewhiteball.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.databinding.FragmentSplashBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutineSplash : Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity()).asGif().load(R.drawable.ball).into(binding.imageView2)
         coroutineSplash = coroutineScope.launch {
            delay(1000)
            MAIN.navController.navigate(R.id.action_splashFragment_to_mainFragment)
            coroutineSplash?.cancel()
        }

    }
}