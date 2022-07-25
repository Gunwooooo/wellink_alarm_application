package com.hanait.wellinkalarmapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeSearchDetail1Binding
import com.hanait.wellinkalarmapplication.home.SearchDetailActivity.Companion.searchData
import com.hanait.wellinkalarmapplication.utils.BaseFragment

class HomeSearchDetail1Fragment : BaseFragment<FragmentHomeSearchDetail1Binding>(
    FragmentHomeSearchDetail1Binding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeSearchDetailTextViewEfcyQusitm.text = searchData.efcyQesitm
        binding.homeSearchDetailTextViewAtpnWarnQesitm.text = searchData.atpnWarnQesitm
        binding.homeSearchDetailTextViewIntrcQesitm.text = searchData.intrcQesitm
    }
}