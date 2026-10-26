package com.instituto.eva2.iotapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {

    // Objeto donde se recibirá el objeto "usu".
    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cabecera = findViewById<TextView>(R.id.tvusumenu)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn5 = findViewById<Button>(R.id.btn5)
        usu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usu", Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("usu") as? Usuario
        }
        cabecera.setText("Bienvenido(a) : " + usu?.nombre)
        boton1(btn1)
        boton2(btn2)
        boton3(btn3)
        boton4(btn4)
        boton5(btn5)
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun boton1(btn1: Button) {
        btn1.setOnClickListener {
            var intent = Intent(this@Menu, ManipularESP32::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "boton1".

    fun boton2(btn2: Button) {
        btn2.setOnClickListener {
            var intent = Intent(this@Menu, Acciones::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "boton2".

    fun boton3(btn3: Button) {
        btn3.setOnClickListener {
            var intent = Intent(this@Menu, Perfil::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "boton3".

    fun boton4(btn4: Button) {
        btn4.setOnClickListener {
            var a = AlertDialog.Builder(this@Menu)
            a.setTitle("Pregunta")
            a.setMessage("¿Desea Cerrar La Sesión?")
            a.setNegativeButton("Cancelar") { dialog, wich -> dialog.dismiss() }
            a.setPositiveButton("Aceptar") { dialog, wich -> startActivity(Intent(this@Menu, Principal::class.java)) }
            a.setIcon(R.drawable.pregunta)
            a.setCancelable(false)
            a.show()
        }
    } // Cierra la función "boton4".

    fun boton5(btn5: Button) {
        btn5.setOnClickListener {
            var intent = Intent(this@Menu, Estadisticas::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "boton5".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

} // Cierra la clase "Menu".