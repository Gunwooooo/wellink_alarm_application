package com.hanait.wellinkalarmapplication.manual

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentManual3Binding
import com.hanait.wellinkalarmapplication.utils.BaseFragment

class ManualFragment3 : BaseFragment<FragmentManual3Binding>(FragmentManual3Binding::inflate) {
    private val glide by lazy { Glide.with(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //초기 글라이드로 이미지 불러오기
        glide.load(R.drawable.manual3).into(binding.manual3ImageView)
    }
}