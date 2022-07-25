package com.hanait.wellinkalarmapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeSearchDetail3Binding
import com.hanait.wellinkalarmapplication.home.SearchDetailActivity.Companion.searchData
import com.hanait.wellinkalarmapplication.utils.BaseFragment


class HomeSearchDetail3Fragment : BaseFragment<FragmentHomeSearchDetail3Binding>(
    FragmentHomeSearchDetail3Binding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeSearchDetailTextViewSeQesitm.text = searchData.seQesitm
        binding.homeSearchDetailTextViewAtpnQesitm.text = searchData.atpnQesitm
    }
}