package com.expoguia.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TVMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212)), // Fondo oscuro elegante para TV
                contentAlignment = Alignment.Center
            ) {
                PantallaSmartTV()
            }
        }
    }
}

@Composable
fun PantallaSmartTV() {
    var salaActual by remember { mutableStateOf("Esperando selección desde el dispositivo móvil...") }

    // Sincronización en tiempo real con Firebase
    DisposableEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("salaSeleccionada")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valor = snapshot.getValue(String::class.java)
                if (valor != null) {
                    salaActual = valor
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listener)

        onDispose {
            ref.removeEventListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏛️ EXPO-GUÍA CULTURAL",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFB74D)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(280.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = salaActual,
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
        }
    }
}