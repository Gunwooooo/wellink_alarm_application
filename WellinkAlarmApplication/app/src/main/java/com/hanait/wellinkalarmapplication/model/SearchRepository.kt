package com.hanait.wellinkalarmapplication.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hanait.wellinkalarmapplication.retrofit.RetrofitManager
import com.hanait.wellinkalarmapplication.utils.CompletionResponse

class SearchRepository {

    var _item = MutableLiveData<Items>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadSearchDataAsPage(page: Int) {
        //공공데이터 가져오기
        RetrofitManager.instance.loadSearchDataAsPage(page, completion = {
                completionResponse, s ->
            when(completionResponse) {
                CompletionResponse.OK -> {
                    _item.value = s?.body?.items
                }
                CompletionResponse.FAIL -> {
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadSearchDataAsItemName(itemName: String, page: Int) {
        //공공데이터 가져오기
        RetrofitManager.instance.loadSearchDataAsItemName(itemName, page, completion = {
                completionResponse, s ->
            when(completionResponse) {
                CompletionResponse.OK -> {
                    _item.value = s?.body?.items
                }
                CompletionResponse.FAIL -> {
                }
            }
        })
    }

}