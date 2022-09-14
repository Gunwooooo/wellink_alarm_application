package com.hanait.wellinkalarmapplication.retrofit

import com.hanait.wellinkalarmapplication.model.SearchData
import com.hanait.wellinkalarmapplication.utils.API
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    @GET(API.LOAD_SEARCH_DATA)
    fun loadSearchDataAsPage(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
    ): Call<SearchData>

    @GET(API.LOAD_SEARCH_DATA)
    fun loadSearchDataAsItemName(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("itemName") itemName: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
    ): Call<SearchData>
}