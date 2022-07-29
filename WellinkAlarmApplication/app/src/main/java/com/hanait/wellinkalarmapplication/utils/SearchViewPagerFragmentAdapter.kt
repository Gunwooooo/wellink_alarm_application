package com.hanait.wellinkalarmapplication.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hanait.wellinkalarmapplication.home.HomeSearchDetail1Fragment
import com.hanait.wellinkalarmapplication.home.HomeSearchDetail2Fragment
import com.hanait.wellinkalarmapplication.home.HomeSearchDetail3Fragment

class SearchViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // 1. ViewPager2에 연결할 Fragment 들을 생성
    private val fragmentList = listOf(HomeSearchDetail1Fragment(), HomeSearchDetail2Fragment(), HomeSearchDetail3Fragment())

    // 2. ViesPager2에서 노출시킬 Fragment 의 갯수 설정
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // 3. ViewPager2의 각 페이지에서 노출할 Fragment 설정
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}