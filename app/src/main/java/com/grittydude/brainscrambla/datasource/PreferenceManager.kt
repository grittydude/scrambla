package com.grittydude.brainscrambla.datasource
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {

    private var sharedPreferences : SharedPreferences? = null

    fun preferenceManager(context: Context){
        sharedPreferences = context.getSharedPreferences("Splash", Context.MODE_PRIVATE)
    }

    fun putString(key:String, value:String){
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key: String) : String {
        return sharedPreferences!!.getString(key, null).toString()
    }



}