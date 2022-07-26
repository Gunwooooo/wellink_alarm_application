package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeSearchBinding
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.model.SearchViewModel
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import javax.net.ssl.HandshakeCompletedEvent

class HomeSearchFragment : BaseFragment<FragmentHomeSearchBinding>(FragmentHomeSearchBinding::inflate) {

    lateinit var model: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter
    private var page = 1 //현재 페이지
    private var itemName = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this)[SearchViewModel::class.java]

        //1페이지 가져오기
        model.loadSearchDataAsPage(page)

        //notifyChange 및 데이터 삽입 설정
        setModelObserve()

        recyclerViewCreate()

        //검색 editText 키보드 검색 버튼 이벤트 구현
        binding.homeSearchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    itemName = binding.homeSearchEditText.text.toString()
                    page = 1

                    if(itemName == "")
                        Toast.makeText(context, "검색어를 초기화합니다.", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "${itemName}로 검색합니다.", Toast.LENGTH_SHORT).show()
                    model.loadSearchDataAsItemName(itemName, page)

                    recyclerViewCreate()

                    return true
                }
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setModelObserve() {
        model.getAll().observe(this.viewLifecycleOwner, Observer {
            searchAdapter.setList(it.item as MutableList<Item>)
            searchAdapter.notifyItemRangeInserted((page - 1) * 70, 70)
        })
    }

    // 리사이클러뷰 설정
    private fun recyclerViewCreate() {
        val searchView = binding.homeSearchRecyclerView
        searchAdapter = SearchAdapter(requireContext())
        val layoutManager = LinearLayoutManager(context)
        searchView.layoutManager = layoutManager
        searchView.adapter = searchAdapter
        searchView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                // 스크롤이 끝에 도달했는지 확인
                if (!searchView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && itemTotalCount >= 70 && lastVisibleItemPosition % 70 == 0) {
                    Log.d("로그", "HomeSearchFragment - onScrolled : 끝에 도달")
                    searchAdapter.deleteLoading()
                    if (itemName == "") model.loadSearchDataAsPage(++page)
                    else model.loadSearchDataAsItemName(itemName, ++page)
                }
            }
        })
    }
}