package com.hanait.wellinkalarmapplication.retrofit

import com.hanait.wellinkalarmapplication.model.SearchData
import com.hanait.wellinkalarmapplication.utils.API
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @GET(API.DUPLICATE_CHECK_USER)
    fun duplicateCheckUser(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("entpName", encoded = true) entpName: String,
        @Query("pageNo") pageNo: Int,
        @Query("startPage") startPage: Int,
        @Query("numOfRows") numOfRows: Int,
    ): Call<SearchData>

}