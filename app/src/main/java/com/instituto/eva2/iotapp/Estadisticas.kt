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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Estadisticas : AppCompatActivity() {

    private var usu: Usuario? = null
    private lateinit var rootRef: DatabaseReference
    private var statsListener: ValueEventListener? = null

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

        rootRef = try {
            FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/").reference
        } catch (_: Exception) {
            FirebaseDatabase.getInstance().reference
        }

        cargarEstadisticas()
        volver(btnvolest)
        desactivarFlecha()
    }

    fun cargarEstadisticas() {
        val tvtemest = findViewById<TextView>(R.id.tvtemest)
        val tvmaxest = findViewById<TextView>(R.id.tvmaxest)
        val tvadvest = findViewById<TextView>(R.id.tvadvest)
        val tvpelest = findViewById<TextView>(R.id.tvpelest)

        statsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || !snapshot.hasChild("temperaturaActual")) {
                    rootRef.child("admin").child("estadisticas").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(adminSnapshot: DataSnapshot) {
                            parseStats(adminSnapshot, tvtemest, tvmaxest, tvadvest, tvpelest)
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                    return
                }
                parseStats(snapshot, tvtemest, tvmaxest, tvadvest, tvpelest)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Estadisticas, "Error Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        rootRef.child("estadisticas").addValueEventListener(statsListener!!)
    }

    private fun parseStats(snapshot: DataSnapshot, tvtem: TextView, tvmax: TextView, tvadv: TextView, tvpel: TextView) {
        var tem = snapshot.child("temperaturaActual").getValue()?.toString() ?: "0"
        var max = snapshot.child("temperaturaMaxima").getValue()?.toString() ?: "0"
        var adv = snapshot.child("alertasAdvertencia").getValue()?.toString() ?: "0"
        var pel = snapshot.child("alertasPeligro").getValue()?.toString() ?: "0"

        if (tem == "null") tem = "0"
        if (max == "null") max = "0"
        if (adv == "null") adv = "0"
        if (pel == "null") pel = "0"

        tvtem.text = "$tem °C"
        tvmax.text = "$max °C"
        tvadv.text = adv
        tvpel.text = pel
    }

    override fun onDestroy() {
        super.onDestroy()
        statsListener?.let {
            rootRef.child("estadisticas").removeEventListener(it)
        }
    }

    fun volver(btnvolest: Button) {
        btnvolest.setOnClickListener {
            val intent = Intent(this@Estadisticas, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }
    }

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
        }
    }
}
