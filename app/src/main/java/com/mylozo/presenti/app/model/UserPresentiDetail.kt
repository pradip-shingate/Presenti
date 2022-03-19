package com.mylozo.presenti.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class UserPresentiDetail :Object(){
    @SerializedName("IsError")
    @Expose
    var isError: Boolean = true

    @SerializedName("ErrorMessage")
    @Expose
    var errorMessage: String? = null

    @SerializedName("Data")
    @Expose
    var data: PresentiData? = null
}