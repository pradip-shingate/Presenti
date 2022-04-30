package com.mylozo.presenti.app.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class EmployeePrefs {

    companion object {
        private const val PREF_ID = "EMPLOYEE_DETAILS"
        private const val KEY_VALID = "KEY_VALID"
        private const val KEY_EMPLOYEE = "KEY_EMPLOYEE"
        private const val KEY_BUSINESS = "KEY_BUSINESS"
        private const val KEY_LANGUAGE = "KEY_LANGUAGE"
        private const val KEY_INFO_OVERLAY = "KEY_INFO_OVERLAY"

        fun setValid(context: Context, isValid: Boolean) {
            val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putBoolean(KEY_VALID, isValid)
            editor.commit()
        }

        fun getValid(context: Context): Boolean {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                val isValid =
                    sharedPreferences?.getBoolean(KEY_VALID, false)
                return isValid == true
            } catch (e: Exception) {
            }
            return false
        }

        fun saveEmployeeDetails(
            context: Context,
            employeeDetails: EmployeeDetails
        ) {
            val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            val strEmployeeJson: String = Gson().toJson(employeeDetails)
            editor.putString(KEY_EMPLOYEE, strEmployeeJson)
            editor.commit()
        }

        fun getEmployeeDetails(context: Context): EmployeeDetails {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                val strGeofencesJson =
                    sharedPreferences?.getString(KEY_EMPLOYEE, "")
                val type: Type = object : TypeToken<EmployeeDetails?>() {}.type
                return Gson().fromJson(strGeofencesJson, type)
            } catch (e: Exception) {
            }
            return EmployeeDetails()
        }

        fun saveBusinessDetails(
            context: Context,
            businessDetails: BusinessDetails
        ) {
            val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            val strEmployeeJson: String = Gson().toJson(businessDetails)
            editor.putString(KEY_BUSINESS, strEmployeeJson)
            editor.commit()
        }

        fun getBusinessDetails(context: Context): BusinessDetails {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                val strGeofencesJson =
                    sharedPreferences?.getString(KEY_BUSINESS, "")
                val type: Type = object : TypeToken<BusinessDetails?>() {}.type
                return Gson().fromJson(strGeofencesJson, type)
            } catch (e: Exception) {
            }
            return BusinessDetails()
        }

        fun deleteAllDetails(context: Context) {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                sharedPreferences?.edit()?.remove(KEY_EMPLOYEE)?.commit()
                sharedPreferences?.edit()?.remove(KEY_BUSINESS)?.commit()
                sharedPreferences?.edit()?.remove(KEY_VALID)?.commit()
            } catch (e: Exception) {
            }
        }

        fun setLanguage(context: Context, lang: String) {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(KEY_LANGUAGE, lang)
                editor.commit()
            } catch (e: Exception) {
            }
        }

        fun getLanguage(context: Context): String? {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                return sharedPreferences?.getString(KEY_LANGUAGE, "en")
            } catch (e: Exception) {
            }
            return "en"
        }

        fun setInfoOverlay(context: Context, isDone: Boolean) {
            val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putBoolean(KEY_INFO_OVERLAY, isDone)
            editor.commit()
        }

        fun getInfoOverlay(context: Context): Boolean {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                val isValid =
                    sharedPreferences?.getBoolean(KEY_INFO_OVERLAY,false)
                return isValid == true
            } catch (e: Exception) {
            }
            return false
        }
    }
}