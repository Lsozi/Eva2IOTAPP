package com.instituto.eva2.iotapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ManipularESP32 : AppCompatActivity() {

    private var usu: Usuario? = null

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
    }

    fun botonApagarAlarma(btnoff: Button) {
        btnoff.setOnClickListener {
            val rootRef = try {
                FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
            } catch (_: Exception) {
                FirebaseDatabase.getInstance().reference
            }

            rootRef.child("alarma").setValue(false)
            rootRef.child("admin").child("alarma").setValue(false)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val currentTime = timeFormat.format(Date())
            val username = usu?.nombre ?: "Usuario"

            val actionMap = mapOf(
                "fecha" to currentDate,
                "hora" to currentTime,
                "usuario" to username,
                "accion" to "Alarma Apagada Manualmente",
                "temperatura" to ""
            )
            rootRef.child("acciones").push().setValue(actionMap)

            Toast.makeText(
                this@ManipularESP32,
                "Orden de apagado enviada (se sincronizará automáticamente si no hay conexión)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun volver(btnvolman: Button) {
        btnvolman.setOnClickListener {
            val intent = Intent(this@ManipularESP32, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    }

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
        }
    }
}
