package com.hanait.wellinkalarmapplication.manual

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentManual2Binding
import com.hanait.wellinkalarmapplication.utils.BaseFragment

class ManualFragment2 : BaseFragment<FragmentManual2Binding>(FragmentManual2Binding::inflate) {
    private val glide by lazy { Glide.with(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //초기 글라이드로 이미지 불러오기
        glide.load(R.drawable.image_color_manual2).into(binding.manual2ImageView)

    }
}