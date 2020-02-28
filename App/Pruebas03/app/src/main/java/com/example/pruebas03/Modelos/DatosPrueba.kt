package com.example.pruebas03.Modelos

import com.google.gson.annotations.SerializedName
import com.example.pruebas03.ManejoAPI.ConvertirJSON

data class DatosPrueba(
    @SerializedName("dato") val dato:String,
    @SerializedName("estado") val estado:String
):ConvertirJSON