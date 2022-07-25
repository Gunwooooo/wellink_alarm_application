package com.hanait.wellinkalarmapplication.retrofit

import android.util.Log
import com.hanait.wellinkalarmapplication.model.Items

import com.hanait.wellinkalarmapplication.model.SearchData
import com.hanait.wellinkalarmapplication.utils.API.BASE_URL
import com.hanait.wellinkalarmapplication.utils.CompletionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class RetrofitManager {
    companion object{
        const val SERVICE_KEY = "l558qWekocntBnplWRwy%2F8g9EL8DBpOGTnea7BP%2BCaX6mOa8U4WbDUY%2BjmYa2MeTkk6a5uAEUAgQsMPtS41xKg%3D%3D"
        val instance = RetrofitManager()
    }
    private val iRetrofit: IRetrofit? = RetrofitClient.getClient(BASE_URL)?.create(IRetrofit::class.java)

    //페이지 별 데이터 가져오기
    fun loadSearchDataAsPage(pageNo: Int, completion: (CompletionResponse, SearchData?) -> Unit){
        val call = iRetrofit?.loadSearchDataAsPage(SERVICE_KEY, pageNo) ?: return

        call.enqueue(object: Callback<SearchData>{

            override fun onFailure(call: Call<SearchData>, t: Throwable) {
                Log.d("로그", "RetrofitManager - onFailure : onFailure")
                completion(CompletionResponse.FAIL, null)
            }
            override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
                if(response.code() != 200) {
                    completion(CompletionResponse.FAIL, null)
                }else {
                    val searchData: SearchData? = response.body()
                    completion(CompletionResponse.OK, searchData)
                }
            }
        })
    }

    //키워드 검색 데이터 가져오기
    fun loadSearchDataAsItemName(itemName: String, page: Int, completion: (CompletionResponse, SearchData?) -> Unit){
        val call = iRetrofit?.loadSearchDataAsItemName(SERVICE_KEY, itemName, page) ?: return

        call.enqueue(object: Callback<SearchData>{

            override fun onFailure(call: Call<SearchData>, t: Throwable) {
                Log.d("로그", "RetrofitManager - onFailure : onFailure")
                completion(CompletionResponse.FAIL, null)
            }
            override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
//                Log.d("로그", "RetrofitManager - onResponse : onResponse")
                if(response.code() != 200) {
                    completion(CompletionResponse.FAIL, null)
                }else {
                    val searchData: SearchData? = response.body()
//                    Log.d("로그", "RetrofitManager - onResponse : ${response.body()}")
                    completion(CompletionResponse.OK, searchData)
                }
            }
        })
    }
}

data class User(var uid: String, var password: String, var name: String, var protector_check: Boolean, var protector_id: String)