package com.hanait.wellinkalarmapplication.manual

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentManual1Binding
import com.hanait.wellinkalarmapplication.utils.BaseFragment


class ManualFragment1 : BaseFragment<FragmentManual1Binding>(FragmentManual1Binding::inflate) {
    private val glide by lazy { Glide.with(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glide.load(R.drawable.image_color_manual1).into(binding.manual1ImageView)
    }
}