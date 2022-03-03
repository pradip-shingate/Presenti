package com.presenti.app.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/Business/SendOtpToMobileNumber?")
    fun sendOTP(@Query("MobileNo") number: String?, @Query("Otp") otp: Int): Callback<ResponseBody>
}