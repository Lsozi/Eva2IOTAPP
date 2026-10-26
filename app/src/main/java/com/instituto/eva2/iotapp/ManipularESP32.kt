package com.instituto.eva2.iotapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class ManipularESP32 : AppCompatActivity() {

    // Objeto donde se recibirá el objeto "usu".
    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manipular_esp32)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cabecera = findViewById<TextView>(R.id.tvusumanipular)
        val btnoff = findViewById<Button>(R.id.btnoff)
        val btnvolman = findViewById<Button>(R.id.btnvolman)

        usu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usu", Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("usu") as? Usuario
        }

        cabecera.text = "Bienvenido(a) : " + usu?.nombre
        botonApagarAlarma(btnoff)
        volver(btnvolman)
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun botonApagarAlarma(btnoff: Button) {
        btnoff.setOnClickListener {
            val rootRef = try {
                FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
            } catch (_: Exception) {
                FirebaseDatabase.getInstance().reference
            }

            // Actualiza la alarma a false en Firebase
            rootRef.child("alarma").setValue(false)
            rootRef.child("admin").child("alarma").setValue(false)

            Toast.makeText(this@ManipularESP32, "Alarma del ESP32 APAGADA", Toast.LENGTH_SHORT).show()
        }
    } // Cierra la función "botonApagarAlarma".

    fun volver(btnvolman: Button) {
        btnvolman.setOnClickListener {
            val intent = Intent(this@ManipularESP32, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "volver".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

} // Cierra la clase "ManipularESP32".