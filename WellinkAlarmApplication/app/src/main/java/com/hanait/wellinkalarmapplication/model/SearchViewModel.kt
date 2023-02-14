package com.hanait.wellinkalarmapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    private val searchRepository = SearchRepository()
    private val item: LiveData<Items>
        get() = searchRepository._item

    fun loadSearchDataAsPage(page: Int){
        searchRepository.loadSearchDataAsPage(page)
    }

    fun loadSearchDataAsItemName(keyword: String, page: Int) {
        searchRepository.loadSearchDataAsItemName(keyword, page)
    }

    fun getAll(): LiveData<Items> {
        return item
    }
}