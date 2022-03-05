package com.presenti.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated


@Generated("jsonschema2pojo")
class UserDetails {
    @SerializedName("IsError")
    @Expose
    var isError: Boolean = true

    @SerializedName("ErrorMessage")
    @Expose
    var errorMessage: String? = null

    @SerializedName("Data")
    @Expose
    var data: Int? = null
}