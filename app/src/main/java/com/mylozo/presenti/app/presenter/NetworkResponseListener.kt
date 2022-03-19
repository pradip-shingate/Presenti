package com.mylozo.presenti.app.presenter

interface NetworkResponseListener {
    fun onNetworkSuccess(o:Object?)
    fun onNetworkFailure(o: Object?)
}