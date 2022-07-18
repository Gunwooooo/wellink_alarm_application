package com.hanait.wellinkalarmapplication.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeSearchBinding
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.SearchData
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmNameFragment
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants

class HomeSearchFragment : BaseFragment<FragmentHomeSearchBinding>(FragmentHomeSearchBinding::inflate) {

    private var mSearchList: ArrayList<SearchData> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCreate()

    }


    private fun recyclerViewCreate() {
        val searchView = binding.homeSearchRecyclerView
        val searchAdapter =
            context?.let { SearchAdapter(it, mSearchList) }
        searchAdapter?.setOnItemClickListener(
            object : SearchAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    //알람 데이터 수정하기
                    Log.d("로그", "HomeAlarmFragment - onItemClick : ${mSearchList[pos - 1]}")

                    val intent = Intent(context, SetAlarmActivity::class.java)
                    startActivity(intent)
                }

                override fun onLikeClick(v: View, pos: Int) {
                    //즐겨찾기 추가 눌렀을 때
                }
            })
        val layoutManager = LinearLayoutManager(context)
        searchView.layoutManager = layoutManager
        searchView.adapter = searchAdapter
    }
}