package com.presenti.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class PresentiData {
    @SerializedName("PresentiLogId")
    @Expose
    var presentiLogId: Int? = null

    @SerializedName("EmpAutoId")
    @Expose
    var empAutoId: Int? = null

    @SerializedName("EmpId")
    @Expose
    var empId: Any? = null

    @SerializedName("EmpName")
    @Expose
    var empName: Any? = null

    @SerializedName("EmpMobileNo")
    @Expose
    var empMobileNo: Any? = null

    @SerializedName("PresentiDate")
    @Expose
    var presentiDate: Any? = null

    @SerializedName("BusinessId")
    @Expose
    var businessId: Int? = null

    @SerializedName("InTime")
    @Expose
    var inTime: String? = null

    @SerializedName("OutTime")
    @Expose
    var outTime: String? = null

    @SerializedName("IsGeoLocationRequired")
    @Expose
    var isGeoLocationRequired: Boolean? = null

    @SerializedName("IsBusinessLocationDetect")
    @Expose
    var isBusinessLocationDetect: Boolean? = null

    @SerializedName("LocationRangeLimit")
    @Expose
    var locationRangeLimit: Any? = null

    @SerializedName("EmpLatitude")
    @Expose
    var empLatitude: Any? = null

    @SerializedName("EmpLongitude")
    @Expose
    var empLongitude: Any? = null

    @SerializedName("BusinessLatitude")
    @Expose
    var businessLatitude: Any? = null

    @SerializedName("BusinessLongitude")
    @Expose
    var businessLongitude: Any? = null

    @SerializedName("CreatedBy")
    @Expose
    var createdBy: Any? = null

    @SerializedName("CreatedOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("UpdatedBy")
    @Expose
    var updatedBy: Any? = null

    @SerializedName("UpdatedOn")
    @Expose
    var updatedOn: String? = null

    @SerializedName("InTimeStr")
    @Expose
    var inTimeStr: Any? = null

    @SerializedName("OutTimeStr")
    @Expose
    var outTimeStr: Any? = null

    @SerializedName("TotalHours")
    @Expose
    var totalHours: Any? = null

    @SerializedName("TotalMinutes")
    @Expose
    var totalMinutes: Any? = null

    @SerializedName("TotalSeconds")
    @Expose
    var totalSeconds: Any? = null

    @SerializedName("ListInTimeStr")
    @Expose
    var listInTimeStr: Any? = null

    @SerializedName("ListOutTimeStr")
    @Expose
    var listOutTimeStr: Any? = null

    @SerializedName("EmpInTimeLongitude")
    @Expose
    var empInTimeLongitude: Any? = null

    @SerializedName("EmpInTimeLatitude")
    @Expose
    var empInTimeLatitude: Any? = null

    @SerializedName("EmpOutTimeLongitude")
    @Expose
    var empOutTimeLongitude: Any? = null

    @SerializedName("EmpOutTimeLatitude")
    @Expose
    var empOutTimeLatitude: Any? = null
}