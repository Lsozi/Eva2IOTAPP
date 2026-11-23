package com.instituto.eva2.iotapp

import java.security.MessageDigest
import java.security.SecureRandom

object Seguridad {

    // Genera una sal aleatoria de 16 bytes, en texto hexadecimal (32 caracteres).
    fun generarSal(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    } // Cierra la función "generarSal".

    // Calcula SHA-256 de (sal + clave) y lo entrega en hexadecimal (64 caracteres).
    fun hashClave(clave: String, sal: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest((sal + clave).toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    } // Cierra la función "hashClave".

} // Cierra el objeto "Seguridad".
