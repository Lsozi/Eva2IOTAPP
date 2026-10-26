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

class Estadisticas : AppCompatActivity() {

    // Objeto donde se recibirá el objeto "usu".
    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estadisticas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cabecera = findViewById<TextView>(R.id.tvusuestadisticas)
        val btnvolest = findViewById<Button>(R.id.btnvolest)
        usu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usu", Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("usu") as? Usuario
        }
        cabecera.setText("Bienvenido(a) : " + usu?.nombre)
        cargarEstadisticas()
        volver(btnvolest)
        desactivarFlecha()
    } // Cierra la función "onCreate".

    fun cargarEstadisticas() {
        val tvtemest = findViewById<TextView>(R.id.tvtemest)
        val tvmaxest = findViewById<TextView>(R.id.tvmaxest)
        val tvadvest = findViewById<TextView>(R.id.tvadvest)
        val tvpelest = findViewById<TextView>(R.id.tvpelest)

        val rootRef = try {
            FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
        } catch (_: Exception) {
            FirebaseDatabase.getInstance().reference
        }

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Buscar el nodo que contenga 'temperaturaActual' o 'temperaturaMaxima' en cualquier nivel (ej: admin/estadisticas)
                var statNode: DataSnapshot? = null
                fun buscarEstadisticas(node: DataSnapshot) {
                    if (node.hasChild("temperaturaActual") || node.hasChild("temperaturaMaxima")) {
                        statNode = node
                    } else {
                        for (child in node.children) {
                            if (statNode != null) break
                            buscarEstadisticas(child)
                        }
                    }
                }
                buscarEstadisticas(snapshot)

                val target = statNode ?: snapshot
                var tem = target.child("temperaturaActual").getValue()?.toString() ?: "0"
                var max = target.child("temperaturaMaxima").getValue()?.toString() ?: "0"
                var adv = target.child("alertasAdvertencia").getValue()?.toString() ?: "0"
                var pel = target.child("alertasPeligro").getValue()?.toString() ?: "0"

                if (tem == "null") tem = "0"
                if (max == "null") max = "0"
                if (adv == "null") adv = "0"
                if (pel == "null") pel = "0"

                tvtemest.text = "$tem °C"
                tvmaxest.text = "$max °C"
                tvadvest.text = adv
                tvpelest.text = pel
            } // Cierra el "onDataChange".

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Estadisticas, "Error Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            } // Cierra el "onCancelled".
        })
    } // Cierra la función "cargarEstadisticas".

    fun volver(btnvolest: Button) {
        btnvolest.setOnClickListener {
            var intent = Intent(this@Estadisticas, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    } // Cierra la función "volver".

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // finish()
        }
    } // Cierra la función "desactivarFlecha".

} // Cierra la clase "Estadisticas".