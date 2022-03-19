package com.mylozo.presenti.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class BusinessData {
    @SerializedName("BusinessId")
    @Expose
    var businessId: Int? = null

    @SerializedName("BusinessName")
    @Expose
    var businessName: String? = null

    @SerializedName("BusinessTypeId")
    @Expose
    var businessTypeId: Int? = null

    @SerializedName("BusinessType")
    @Expose
    var businessType: Any? = null

    @SerializedName("BusinessSubTypeId")
    @Expose
    var businessSubTypeId: Int? = null

    @SerializedName("BusinessSubType")
    @Expose
    var businessSubType: Any? = null

    @SerializedName("Address")
    @Expose
    var address: String? = null

    @SerializedName("City")
    @Expose
    var city: String? = null

    @SerializedName("State")
    @Expose
    var state: String? = null

    @SerializedName("Country")
    @Expose
    var country: String? = null

    @SerializedName("Pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("PrimaryContactName")
    @Expose
    var primaryContactName: String? = null

    @SerializedName("PrimaryContactNumber")
    @Expose
    var primaryContactNumber: String? = null

    @SerializedName("BusinessContactNumber")
    @Expose
    var businessContactNumber: String? = null

    @SerializedName("BillingPackageId")
    @Expose
    var billingPackageId: Int? = null

    @SerializedName("BillingPackage")
    @Expose
    var billingPackage: Any? = null

    @SerializedName("AuthenticationTypeId")
    @Expose
    var authenticationTypeId: Int? = null

    @SerializedName("AuthenticationType")
    @Expose
    var authenticationType: Any? = null

    @SerializedName("Email")
    @Expose
    var email: String? = null

    @SerializedName("AttendanceLogTypeId")
    @Expose
    var attendanceLogTypeId: Int? = null

    @SerializedName("AttendanceLogType")
    @Expose
    var attendanceLogType: Any? = null

    @SerializedName("BusinessLogoFile")
    @Expose
    var businessLogoFile: Any? = null

    @SerializedName("BusinessLogoPath")
    @Expose
    var businessLogoPath: String? = null

    @SerializedName("BusinessLogo")
    @Expose
    var businessLogo: String? = null

    @SerializedName("BusinessLogoFileName")
    @Expose
    var businessLogoFileName: Any? = null

    @SerializedName("Base64BusinessLogo")
    @Expose
    var base64BusinessLogo: Any? = null

    @SerializedName("BusinessWebsite")
    @Expose
    var businessWebsite: String? = null

    @SerializedName("WorkingDays")
    @Expose
    var workingDays: Any? = null

    @SerializedName("WorkingTimings")
    @Expose
    var workingTimings: String? = null

    @SerializedName("WorkingDaysArray")
    @Expose
    var workingDaysArray: Any? = null

    @SerializedName("WorkingStartTime")
    @Expose
    var workingStartTime: Any? = null

    @SerializedName("WorkingEndTime")
    @Expose
    var workingEndTime: Any? = null

    @SerializedName("FacebookLink")
    @Expose
    var facebookLink: String? = null

    @SerializedName("InstagramLink")
    @Expose
    var instagramLink: String? = null

    @SerializedName("IsActive")
    @Expose
    var isActive: Boolean? = null

    @SerializedName("IsEmpConfigure")
    @Expose
    var isEmpConfigure: Boolean? = null

    @SerializedName("IsEmpName")
    @Expose
    var isEmpName: Boolean? = null

    @SerializedName("IsEmpEmailId")
    @Expose
    var isEmpEmailId: Boolean? = null

    @SerializedName("IsEmpId")
    @Expose
    var isEmpId: Boolean? = null

    @SerializedName("IsEmpAadharCardNo")
    @Expose
    var isEmpAadharCardNo: Boolean? = null

    @SerializedName("IsEmpPanCardNo")
    @Expose
    var isEmpPanCardNo: Boolean? = null

    @SerializedName("IsEmpAddress")
    @Expose
    var isEmpAddress: Boolean? = null

    @SerializedName("IsEmpBloodGroup")
    @Expose
    var isEmpBloodGroup: Boolean? = null

    @SerializedName("IsEmpMobileNo")
    @Expose
    var isEmpMobileNo: Boolean? = null

    @SerializedName("IsEmpOther1")
    @Expose
    var isEmpOther1: Boolean? = null

    @SerializedName("EmpOther1Field")
    @Expose
    var empOther1Field: String? = null

    @SerializedName("IsEmpOther2")
    @Expose
    var isEmpOther2: Boolean? = null

    @SerializedName("EmpOther2Field")
    @Expose
    var empOther2Field: String? = null

    @SerializedName("QRcodeId")
    @Expose
    var qRcodeId: Int? = null

    @SerializedName("AdminId")
    @Expose
    var adminId: Int? = null

    @SerializedName("LanguageId")
    @Expose
    var languageId: Int? = null

    @SerializedName("Language")
    @Expose
    var language: Any? = null

    @SerializedName("BusinessLatitude")
    @Expose
    var businessLatitude: Double = 0.0

    @SerializedName("BusinessLongitude")
    @Expose
    var businessLongitude: Double = 0.0

    @SerializedName("TwitterLink")
    @Expose
    var twitterLink: String? = null

    @SerializedName("GoogleRateLink")
    @Expose
    var googleRateLink: String? = null

    @SerializedName("ZomatoLink")
    @Expose
    var zomatoLink: String? = null

    @SerializedName("IsBusinessLocationDetect")
    @Expose
    var isBusinessLocationDetect: Int? = null

    @SerializedName("IsBusinessThresholdTime")
    @Expose
    var isBusinessThresholdTime: Int? = null

    @SerializedName("ThresholdTime")
    @Expose
    var thresholdTime: String? = null

    @SerializedName("LocationRangeLimit")
    @Expose
    var locationRangeLimit: Int = 200

    @SerializedName("IsLocationDetectReport")
    @Expose
    var isLocationDetectReport: Int? = null

    @SerializedName("IsRedirectToVisitor")
    @Expose
    var isRedirectToVisitor: Boolean? = null

    @SerializedName("AuthType")
    @Expose
    var authType: String? = null

    @SerializedName("TempPassword")
    @Expose
    var tempPassword: Any? = null

    @SerializedName("DeleteFlag")
    @Expose
    var deleteFlag: Int? = null

    @SerializedName("CreatedBy")
    @Expose
    var createdBy: Int? = null

    @SerializedName("CreatedOn")
    @Expose
    var createdOn: Any? = null

    @SerializedName("UpdatedBy")
    @Expose
    var updatedBy: Int? = null

    @SerializedName("UpdatedOn")
    @Expose
    var updatedOn: Any? = null

    @SerializedName("CurrentPage")
    @Expose
    var currentPage: Int? = null

    @SerializedName("NumberOfRecords")
    @Expose
    var numberOfRecords: Int? = null

    @SerializedName("OrderBy")
    @Expose
    var orderBy: Any? = null

    @SerializedName("TotalCount")
    @Expose
    var totalCount: Int? = null

    @SerializedName("UniqueID")
    @Expose
    var uniqueID: Any? = null
}