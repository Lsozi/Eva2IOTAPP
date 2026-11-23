package com.instituto.eva2.iotapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class IotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            val db = FirebaseDatabase.getInstance("https://appfirebasehola-default-rtdb.firebaseio.com/")
            db.setPersistenceEnabled(true)
            db.reference.child("estadisticas").keepSynced(true)
            db.reference.child("acciones").keepSynced(true)
            db.reference.child("alarma").keepSynced(true)
        } catch (_: Exception) {
            try {
                val db = FirebaseDatabase.getInstance()
                db.setPersistenceEnabled(true)
                db.reference.child("estadisticas").keepSynced(true)
                db.reference.child("acciones").keepSynced(true)
                db.reference.child("alarma").keepSynced(true)
            } catch (_: Exception) {
            }
        }
    }
}
