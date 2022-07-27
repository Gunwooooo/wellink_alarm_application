package com.hanait.wellinkalarmapplication.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivitySearchDetailBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
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

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_like, menu)
        return true
    }

    //toolbar 클릭 리스너
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            //관심약으로 등록
            R.id.like_toolbar_like -> {
                val likeList = DatabaseManager.getInstance(applicationContext, "Alarms.db").selectLikeAll()
                for (i in 0 until likeList.size) {
                    if (likeList[i].itemSeq == searchData.itemSeq) {
                        Toast.makeText(applicationContext, "이미 등록된 약입니다.", Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                DatabaseManager.getInstance(applicationContext, "Alarms.db").insertLike(searchData)
                Toast.makeText(applicationContext, "관심 약/약물로 등록되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //&lt;sup&gt;  &lt;/sup&gt;
    private fun searchDataSplit() {
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;p&gt;", "")
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;/p&gt;", "")
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;sub&gt;", "")
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;/sub&gt;", "")
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;sup&gt;", "")
        searchData.intrcQesitm = searchData.intrcQesitm?.replace("&lt;/sup&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;p&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;/p&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;sub&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;/sub&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;sup&gt;", "")
        searchData.efcyQesitm = searchData.efcyQesitm?.replace("&lt;/sup&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;p&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;/p&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;sub&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;/sub&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;sup&gt;", "")
        searchData.useMethodQesitm = searchData.useMethodQesitm?.replace("&lt;/sup&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;p&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;/p&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;sub&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;/sub&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;sup&gt;", "")
        searchData.atpnQesitm = searchData.atpnQesitm?.replace("&lt;/sup&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;p&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;/p&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;sub&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;/sub&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;sup&gt;", "")
        searchData.seQesitm = searchData.seQesitm?.replace("&lt;/sup&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;p&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;/p&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;sub&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;/sub&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;sup&gt;", "")
        searchData.depositMethodQesitm = searchData.depositMethodQesitm?.replace("&lt;/sup&gt;", "")
    }
}