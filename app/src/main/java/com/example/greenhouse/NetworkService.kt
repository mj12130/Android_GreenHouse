package com.example.greenhouse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrSearch?serviceKey=인증키&st=1&sw=장미&numOfRows=10&pageNo=1

interface NetworkService {
    @GET("plntIlstrSearch")
    fun getXmlList(
        @Query("serviceKey") apiKey:String,
        @Query("st") st:Int,
        @Query("sw") sw:String,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int
    ) : Call<XmlResponse>
}