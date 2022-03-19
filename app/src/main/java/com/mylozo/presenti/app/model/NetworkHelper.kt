package com.mylozo.presenti.app.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mylozo.presenti.app.presenter.NetworkResponseListener
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

    fun validateUser(url: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<UserDetails?>() {}.type
                        val user: UserDetails? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (e: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    fun getUserDetails(url: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<EmployeeDetails?>() {}.type
                        val user: EmployeeDetails? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (e: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    fun getUserPresentiDetails(url: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<UserPresentiDetail?>() {}.type
                        val user: UserPresentiDetail? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (e: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    fun getBusinessDetails(url: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<BusinessDetails?>() {}.type
                        val user: BusinessDetails? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (e: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    fun insertInOutLogs(url: String, json: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()

            var body: RequestBody? = RequestBody.create(JSON, json)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()


            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<UserDetails?>() {}.type
                        val user: UserDetails? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (E: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    fun insertNote(url: String, json: String, networkResponseListener: NetworkResponseListener) {
        try {
            val okHttpClient: OkHttpClient? = getOkHttpClient()

            var body: RequestBody? = RequestBody.create(JSON, json)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()


            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("Failed", "Success but failed.${e?.message}")
                    networkResponseListener.onNetworkFailure(null)
                }

                override fun onResponse(response: Response?) {

                    if (response?.isSuccessful == true) {
                        val type: Type = object : TypeToken<UserDetails?>() {}.type
                        val user: UserDetails? = Gson().fromJson(response?.body()!!.string(), type)
                        networkResponseListener.onNetworkSuccess(user)
                    }
                }
            })
        } catch (E: Exception) {
            networkResponseListener.onNetworkFailure(null)
        }
    }

    @Throws(Exception::class)
    private fun getOkHttpClient(): OkHttpClient? {
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
        return null
    }
}