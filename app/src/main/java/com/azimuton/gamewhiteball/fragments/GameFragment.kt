package com.azimuton.gamewhiteball.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private lateinit var binding : FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MAIN.navController.navigate(R.id.action_gameFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

}