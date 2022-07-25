package com.hanait.wellinkalarmapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeSearchDetail2Binding
import com.hanait.wellinkalarmapplication.home.SearchDetailActivity.Companion.searchData
import com.hanait.wellinkalarmapplication.utils.BaseFragment

class HomeSearchDetail2Fragment : BaseFragment<FragmentHomeSearchDetail2Binding>(
    FragmentHomeSearchDetail2Binding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeSearchDetailTextViewDeposiMethodQesitm.text = searchData.depositMethodQesitm
        binding.homeSearchDetailTextViewUseMethodQesitm.text = searchData.useMethodQesitm
    }
}