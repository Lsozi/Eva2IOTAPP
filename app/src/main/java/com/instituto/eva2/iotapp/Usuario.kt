package com.instituto.eva2.iotapp

import java.io.Serializable

data class Usuario(
    val rut: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val cargo: String = "",
    val tipo: String = "",
    val contraseña: String = ""
) : Serializable