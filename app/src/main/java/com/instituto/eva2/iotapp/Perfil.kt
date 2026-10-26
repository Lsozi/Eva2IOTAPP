package com.instituto.eva2.iotapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Perfil : AppCompatActivity() {

    // Objeto donde se recibirá el objeto "usu".
    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cabecera = findViewById<TextView>(R.id.tvusuperfil)
        val tvrutper = findViewById<TextView>(R.id.tvrutper)
        val tvnomper = findViewById<TextView>(R.id.tvnomper)
        val tvapeper = findViewById<TextView>(R.id.tvapeper)
        val tvemaper = findViewById<TextView>(R.id.tvemaper)
        val tvcarper = findViewById<TextView>(R.id.tvcarper)
        val tvconper = findViewById<TextView>(R.id.tvconper)
        val btnvolper = findViewById<Button>(R.id.btnvolper)

        usu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usu", Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("usu") as? Usuario
        }

        cabecera.text = "Bienvenido(a) : " + usu?.nombre
        tvrutper.text = if (usu?.rut.isNullOrEmpty()) usu?.nombre else usu?.rut
        tvnomper.text = usu?.nombre
        tvapeper.text = if (usu?.apellido.isNullOrEmpty()) "-" else usu?.apellido
        tvemaper.text = if (usu?.email.isNullOrEmpty()) "-" else usu?.email
        tvcarper.text = if (!usu?.cargo.isNullOrEmpty()) usu?.cargo else if (!usu?.tipo.isNullOrEmpty()) usu?.tipo else "-"
        tvconper.text = "********"

        volver(btnvolper)
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun volver(btnvolper: Button) {
        btnvolper.setOnClickListener {
            val intent = Intent(this@Perfil, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "volver".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

} // Cierra la clase "Perfil".