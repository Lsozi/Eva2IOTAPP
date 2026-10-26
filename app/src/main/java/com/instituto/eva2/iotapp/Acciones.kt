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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Acciones : AppCompatActivity() {

    // Objeto donde se recibirá el objeto "usu".
    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cabecera = findViewById<TextView>(R.id.tvusuacciones)
        val btnvolacc = findViewById<Button>(R.id.btnvolacc)
        usu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usu", Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("usu") as? Usuario
        }
        cabecera.setText("Bienvenido(a) : " + usu?.nombre)
        cargarAcciones()
        volver(btnvolacc)
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun cargarAcciones() {
        val tvlistaacc = findViewById<TextView>(R.id.tvlistaacc)
        val rootRef = try {
            FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
        } catch (_: Exception) {
            FirebaseDatabase.getInstance().reference
        }

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var texto = ""

                // Buscar nodos que contengan acciones en cualquier nivel (ej: admin/acciones)
                val accionesNodes = mutableListOf<DataSnapshot>()
                fun buscarAcciones(node: DataSnapshot) {
                    if (node.hasChild("accion") || node.hasChild("fecha")) {
                        accionesNodes.add(node)
                    } else {
                        for (child in node.children) {
                            buscarAcciones(child)
                        }
                    }
                }
                buscarAcciones(snapshot)

                for (x in accionesNodes) {
                    val fec = x.child("fecha").getValue()?.toString() ?: ""
                    val hor = x.child("hora").getValue()?.toString() ?: ""
                    val usr = x.child("usuario").getValue()?.toString() ?: ""
                    val acc = x.child("accion").getValue()?.toString() ?: ""
                    val tem = x.child("temperatura").getValue()?.toString() ?: ""
                    texto = "$fec $hor\n$usr : $acc ($tem °C)\n\n$texto"
                }

                if (texto.isEmpty()) {
                    texto = "Sin acciones registradas en Firebase"
                }
                tvlistaacc.text = texto
            } // Cierra el "onDataChange".

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Acciones, "Error Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            } // Cierra el "onCancelled".
        })
    } // Cierra la función "cargarAcciones".

    fun volver(btnvolacc: Button) {
        btnvolacc.setOnClickListener {
            var intent = Intent(this@Acciones, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "volver".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

} // Cierra la clase "Acciones".