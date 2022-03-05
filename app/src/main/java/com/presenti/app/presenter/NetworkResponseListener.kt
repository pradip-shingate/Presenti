package com.presenti.app.presenter

interface NetworkResponseListener {
    fun onNetworkSuccess()
    fun onNetworkFailure()
}