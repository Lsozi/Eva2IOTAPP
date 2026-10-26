package com.instituto.eva2.iotapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Principal : AppCompatActivity() {

    lateinit var txtusu: EditText
    lateinit var txtcon: EditText
    lateinit var btnini: Button
    lateinit var btncrearcuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtusu = findViewById(R.id.txtusu)
        txtcon = findViewById(R.id.txtcon)
        btnini = findViewById(R.id.btnini)
        btncrearcuenta = findViewById(R.id.btncrearcuenta)

        btncrearcuenta.setOnClickListener {
            startActivity(Intent(this@Principal, CrearCuenta::class.java))
        }

        botonLogin()
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun botonLogin() {
        btnini.setOnClickListener {
            val usuTxt = txtusu.text.toString().trim()
            val conTxt = txtcon.text.toString().trim()

            if (usuTxt.isEmpty() || conTxt.isEmpty()) {
                Toast.makeText(this@Principal, "¡ Complete Los Campos Faltantes !", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val nom = usuTxt.uppercase()

            val rootRef = try {
                FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
            } catch (_: Exception) {
                FirebaseDatabase.getInstance().reference
            }

            rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        Toast.makeText(
                            this@Principal,
                            "La base de datos en Firebase está vacía.",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    val userNodes = mutableListOf<DataSnapshot>()
                    fun buscarUsuarios(node: DataSnapshot) {
                        if (node.hasChild("nombre") || node.hasChild("usuario") || node.hasChild("user") || node.hasChild("rut")) {
                            userNodes.add(node)
                        } else {
                            for (child in node.children) {
                                buscarUsuarios(child)
                            }
                        }
                    }
                    buscarUsuarios(snapshot)

                    if (userNodes.isEmpty()) {
                        Toast.makeText(this@Principal, "No se encontraron registros de usuarios en Firebase.", Toast.LENGTH_LONG).show()
                        return
                    }

                    var estado = false
                    for (x in userNodes) {
                        val dbRut = x.child("rut").getValue()?.toString() ?: ""
                        val dbNombre = x.child("nombre").getValue()?.toString()
                            ?: x.child("usuario").getValue()?.toString()
                            ?: x.child("user").getValue()?.toString() ?: ""
                        val dbApellido = x.child("apellido").getValue()?.toString() ?: ""
                        val dbEma = x.child("email").getValue()?.toString() ?: ""
                        val dbCargo = x.child("cargo").getValue()?.toString() ?: x.child("tipo").getValue()?.toString() ?: ""
                        val dbTipo = x.child("tipo").getValue()?.toString() ?: dbCargo
                        val dbPass = x.child("contraseña").getValue()?.toString()
                            ?: x.child("contrasena").getValue()?.toString()
                            ?: x.child("password").getValue()?.toString() ?: ""

                        if ((nom.equals(dbNombre, ignoreCase = true) || nom.equals(dbRut, ignoreCase = true)) && conTxt == dbPass) {
                            estado = true
                            val usuObj = Usuario(
                                rut = if (dbRut.isNotEmpty()) dbRut else dbNombre,
                                nombre = dbNombre,
                                apellido = dbApellido,
                                email = dbEma,
                                cargo = dbCargo,
                                tipo = dbTipo,
                                contraseña = dbPass
                            )
                            val intent = Intent(this@Principal, Menu::class.java)
                            intent.putExtra("usu", usuObj)
                            startActivity(intent)
                            break
                        }
                    } // Cierra el ciclo for.

                    if (!estado) {
                        txtusu.setText("")
                        txtcon.setText("")
                        ocultarTeclado()
                        Toast.makeText(this@Principal, "¡ Error de Usuario y/o Contraseña !", Toast.LENGTH_SHORT).show()
                    }
                } // Cierra el "onDataChange".

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Principal, "Error Firebase: ${error.message}", Toast.LENGTH_LONG).show()
                } // Cierra el "onCancelled".
            })
        }
    } // Cierra la función "botonLogin".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

    private fun ocultarTeclado() {
        val imm = getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    } // Cierra la función "ocultarTeclado".

} // Cierra la clase "Principal".