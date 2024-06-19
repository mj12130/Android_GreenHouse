package com.example.greenhouse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrSearch?serviceKey=인증키&st=1&sw=장미&numOfRows=10&pageNo=1

// http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrInfo?serviceKey=인증키&q1=32222

interface NetworkService {

    // 도감 목록
    @GET("plntIlstrSearch")
    fun getXmlList(
        @Query("serviceKey") apiKey:String,
        @Query("st") st:Int,
        @Query("sw") sw:String,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int
    ) : Call<XmlResponse>

    // 상세정보
    @GET("plntIlstrInfo")
    fun getXmlInfo(
        @Query("serviceKey") apiKey:String,
        @Query("q1") q1:Int
    ) : Call<XmlResponse2>
}