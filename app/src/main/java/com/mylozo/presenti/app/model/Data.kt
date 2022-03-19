package com.mylozo.presenti.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated


@Generated("jsonschema2pojo")
class Data {
    @SerializedName("EmpAutoId")
    @Expose
    var empAutoId: Int = -1

    @SerializedName("BusinessId")
    @Expose
    var businessId: Int? = null

    @SerializedName("EmpName")
    @Expose
    var empName: String? = null

    @SerializedName("EmpMobileNo")
    @Expose
    var empMobileNo: String = ""

    @SerializedName("EmailId")
    @Expose
    var emailId: String? = null

    @SerializedName("EmpId")
    @Expose
    var empId: String? = null

    @SerializedName("EmpAddress")
    @Expose
    var empAddress: Any? = null

    @SerializedName("EmpAadharCardNo")
    @Expose
    var empAadharCardNo: Any? = null

    @SerializedName("EmpPanCardNo")
    @Expose
    var empPanCardNo: Any? = null

    @SerializedName("EmpBloodGroup")
    @Expose
    var empBloodGroup: Any? = null

    @SerializedName("EmpOther1Field")
    @Expose
    var empOther1Field: Any? = null

    @SerializedName("EmpOther2Field")
    @Expose
    var empOther2Field: Any? = null

    @SerializedName("IsActive")
    @Expose
    var isActive: Boolean = false

    @SerializedName("CreatedBy")
    @Expose
    var createdBy: Int? = null

    @SerializedName("CreatedOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("ModifiedBy")
    @Expose
    var modifiedBy: Int? = null

    @SerializedName("ModifiedOn")
    @Expose
    var modifiedOn: String? = null
}