package com.example.systurno_pruebas01

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

interface JSONConvertable {
    fun toJSON(): String = Gson().toJson(this)
}
inline fun <reified T: JSONConvertable> String.toObject(): T = Gson().fromJson(this, T::class.java)

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("authentication_token") val authenticationToken: String) : JSONConvertable

data class Feature(
    @SerializedName("id") var id: Int = -1,
    @SerializedName("name") var name: String = "",
    @SerializedName("icon") var icon: String = "") : JSONConvertable

data class DatoServidor(
    @SerializedName("dato")var dato:String):JSONConvertable
//From JSON
//val json = "..."
//val object = json.toObject<User>()

// To JSON
//val json = object.toJSON()