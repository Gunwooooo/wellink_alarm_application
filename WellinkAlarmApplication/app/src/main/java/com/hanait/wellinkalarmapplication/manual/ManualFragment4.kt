package com.hanait.wellinkalarmapplication.manual

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentManual4Binding
import com.hanait.wellinkalarmapplication.utils.BaseFragment

class ManualFragment4 : BaseFragment<FragmentManual4Binding>(FragmentManual4Binding::inflate) {
    private val glide by lazy { Glide.with(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glide.load(R.drawable.image_color_manual4).into(binding.manual5ImageView)
    }}