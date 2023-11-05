package com.azimuton.gamewhiteball.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.adapter.HistoryAdapter
import com.azimuton.gamewhiteball.database.AppRoomDatabase
import com.azimuton.gamewhiteball.database.History
import com.azimuton.gamewhiteball.databinding.FragmentHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var historyList: ArrayList<History>
    private lateinit var database : AppRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyList = ArrayList<History>()
        database = AppRoomDatabase.getDatabase(requireActivity())
        getData()
        adapter = HistoryAdapter(historyList)
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        binding.rvHistory.adapter = adapter
        adapter.notifyDataSetChanged()


        binding.btClearHistory.setOnClickListener {
            database.historyDao().deleteAll()
            adapter.notifyDataSetChanged()
            MAIN.navController.navigate(R.id.historyFragment)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MAIN.navController.navigate(R.id.action_historyFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }
    @SuppressLint("SuspiciousIndentation")
    private fun getData() {
            val historyFromDb: List<History> = database.historyDao().getAll()
                historyList.clear()
                historyList.addAll(historyFromDb)
    }
}