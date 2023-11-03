package com.azimuton.gamewhiteball.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btGame.setOnClickListener {
            MAIN.navController.navigate(R.id.action_mainFragment_to_gameFragment)
        }
        binding.btGoogle.setOnClickListener {
           MAIN.navController.navigate(R.id.action_mainFragment_to_webFragment)
        }
        binding.btHistory.setOnClickListener {
            MAIN.navController.navigate(R.id.action_mainFragment_to_historyFragment)
        }
        binding.btClose.setOnClickListener { activity?.finishAffinity() }
    }
}