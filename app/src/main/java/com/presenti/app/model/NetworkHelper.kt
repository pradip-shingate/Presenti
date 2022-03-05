package com.presenti.app.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.presenti.app.presenter.NetworkResponseListener
import com.squareup.okhttp.*
import java.io.IOException
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkHelper {
    private val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")

    fun getRequest(url: String, networkResponseListener: NetworkResponseListener) {
        val okHttpClient: OkHttpClient? = getOkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.d("Failed", "Success but failed.${e?.message}")
                networkResponseListener.onNetworkFailure()
            }

            override fun onResponse(response: Response?) {

                if (response?.isSuccessful == true) {
                    val type: Type = object : TypeToken<UserDetails?>() {}.type
                    val user: UserDetails? = Gson().fromJson(response?.body()!!.string(), type)
                    if (user?.isError == false) {
                        networkResponseListener.onNetworkSuccess()
                    }
                }
            }
        })
    }

    fun postRequest(url: String, json: String, networkResponseListener: NetworkResponseListener) {
        val okHttpClient: OkHttpClient? = getOkHttpClient()

        var body: RequestBody? = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()


        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.d("Failed", "Success but failed.${e?.message}")
                networkResponseListener.onNetworkFailure()
            }

            override fun onResponse(response: Response?) {

                if (response?.isSuccessful == true) {
                    val type: Type = object : TypeToken<UserDetails?>() {}.type
                    val user: UserDetails? = Gson().fromJson(response?.body()!!.string(), type)
                    if (user?.isError == false) {
                        networkResponseListener.onNetworkSuccess()
                    }
                }
            }
        })
    }

    private fun getOkHttpClient(): OkHttpClient? {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val client = OkHttpClient()
            client.sslSocketFactory = sslSocketFactory
            client.setHostnameVerifier { hostname, session -> true }
            return client
        } catch (e: java.lang.Exception) {
        }
        return null
    }
}