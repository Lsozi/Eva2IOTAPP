package com.instituto.eva2.iotapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class CrearCuenta : AppCompatActivity() {

    lateinit var txtrut: EditText
    lateinit var txtnom: EditText
    lateinit var txtape: EditText
    lateinit var txtema: EditText
    lateinit var spncargo: Spinner
    lateinit var txtcon: EditText
    lateinit var btnregistrar: Button
    lateinit var btnvolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtrut = findViewById(R.id.txtrut)
        txtnom = findViewById(R.id.txtnom)
        txtape = findViewById(R.id.txtape)
        txtema = findViewById(R.id.txtema)
        spncargo = findViewById(R.id.spncargo)
        txtcon = findViewById(R.id.txtcon)
        btnregistrar = findViewById(R.id.btnregistrar)
        btnvolver = findViewById(R.id.btnvolver)

        // Opciones estrictas para el Spinner de Cargo / Rol
        val opcionesRoles = arrayOf("admin", "supervisor", "invitado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesRoles)
        spncargo.adapter = adapter

        btnregistrar.setOnClickListener {
            registrarUsuario()
        }

        btnvolver.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {
        val rut = txtrut.text.toString().trim()
        val nom = txtnom.text.toString().trim()
        val ape = txtape.text.toString().trim()
        val ema = txtema.text.toString().trim()
        val car = spncargo.selectedItem.toString()
        val con = txtcon.text.toString().trim()

        if (rut.isEmpty() || nom.isEmpty() || ape.isEmpty() || ema.isEmpty() || con.isEmpty()) {
            Toast.makeText(this, "¡ Complete todos los campos !", Toast.LENGTH_LONG).show()
            return
        }

        val rootRef = try {
            FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
        } catch (_: Exception) {
            FirebaseDatabase.getInstance().reference
        }

        // Crear mapa con todos los atributos requeridos
        val usuarioData = HashMap<String, Any>()
        usuarioData["rut"] = rut
        usuarioData["nombre"] = nom
        usuarioData["apellido"] = ape
        usuarioData["email"] = ema
        usuarioData["cargo"] = car
        usuarioData["tipo"] = car
        usuarioData["contraseña"] = con

        // Guardar bajo /Usuario/{rut} y /admin/Usuario/{rut}
        val userKey = if (rut.contains(".")) rut.replace(".", "_") else rut

        rootRef.child("Usuario").child(userKey).setValue(usuarioData)
        rootRef.child("admin").child("Usuario").child(userKey).setValue(usuarioData)
            .addOnSuccessListener {
                Toast.makeText(this, "¡ Usuario registrado exitosamente !", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al registrar: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }
}