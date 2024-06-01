package com.example.greenhouse

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

// http://openapi.nature.go.kr/openapi/service/rest/PlantService/

class RetrofitConnection{

    companion object {
        private const val BASE_URL = " http://openapi.nature.go.kr/openapi/service/rest/PlantService/"

        var xmlNetworkService : NetworkService
        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        val xmlRetrofit : Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(parser))
                .build()

        init{
            xmlNetworkService = xmlRetrofit.create(NetworkService::class.java)
        }
    }
}