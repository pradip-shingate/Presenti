package com.presenti.app.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Api {

    companion object {
        private var retrofit: Retrofit? = null

        fun getClient(): ApiInterface {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://api.presenti.lo-yo.in/api")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!.create(ApiInterface::class.java)
        }
    }
}