package com.presenti.app.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class EmployeePrefs {

    companion object {
        private const val PREF_ID = "EMPLOYEE_DETAILS"
        private const val KEY_EMPLOYEE = "KEY_EMPLOYEE"

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

        fun deleteEmployeeDetails(context: Context) {
            try {
                val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
                    PREF_ID,
                    Context.MODE_PRIVATE
                )
                sharedPreferences?.edit()?.clear()?.commit()
            } catch (e: Exception) {
            }
        }
    }
}