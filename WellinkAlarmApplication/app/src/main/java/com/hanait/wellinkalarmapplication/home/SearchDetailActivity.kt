package com.hanait.wellinkalarmapplication.home

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivitySearchDetailBinding
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.utils.ViewPagerFragmentAdapter


class SearchDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySearchDetailBinding

    companion object {
        lateinit var searchData : Item
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolbar 표시
        setSupportActionBar(binding.homeSearchDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        //데이터 가져오기
        val intent = intent
        searchData = intent.getSerializableExtra("SearchData") as Item
        searchDataSplit()
        Log.d("로그", "SearchDetailActivity - onCreate : $searchData")
        //약 이미지 넣기
        if(searchData.itemImage == "")
            binding.homeSearchDetailImageView.setImageResource(R.drawable.no_image)
        else Glide.with(this).load(searchData.itemImage).into(binding.homeSearchDetailImageView)

        //약 이름 및 제조업체 명 넣기
        binding.homeSearchDetailTextViewItemName.text = searchData.itemName
        binding.homeSearchDetailTextViewEntpName.text = searchData.entpName

        binding.homeSearchDetailViewPager.adapter = ViewPagerFragmentAdapter(this)

        val tabTitles = listOf("복약", "용법", "주의")
        TabLayoutMediator(binding.homeSearchDetailTabLayout, binding.homeSearchDetailViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onClick(v: View?) {
    }

    //toolbar 클릭 리스너
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //&lt;sup&gt;  &lt;/sup&gt;
    private fun searchDataSplit() {
        searchData.entpName?.replace("&lt;p&gt;", "")
        searchData.entpName?.replace("&lt;/p&gt;", "")
        searchData.itemName?.replace("&lt;p&gt;", "")
        searchData.itemName?.replace("&lt;/p&gt;", "")
        searchData.efcyQesitm?.replace("&lt;p&gt;", "")
        searchData.efcyQesitm?.replace("&lt;/p&gt;", "")
        searchData.useMethodQesitm?.replace("&lt;p&gt;", "")
        searchData.useMethodQesitm?.replace("&lt;/p&gt;", "")
        searchData.atpnQesitm?.replace("&lt;p&gt;", "")
        searchData.atpnQesitm?.replace("&lt;/p&gt;", "")
        searchData.seQesitm?.replace("&lt;p&gt;", "")
        searchData.seQesitm?.replace("&lt;/p&gt;", "")
        searchData.depositMethodQesitm?.replace("&lt;p&gt;", "")
        searchData.depositMethodQesitm?.replace("&lt;/p&gt;", "")
    }
}