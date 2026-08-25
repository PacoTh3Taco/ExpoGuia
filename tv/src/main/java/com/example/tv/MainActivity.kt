package com.expoguia.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
                    .background(Color(0xFF0F0F0F)),
                contentAlignment = Alignment.Center
            ) {
                PantallaSmartTV()
            }
        }
    }
}

@Composable
fun PantallaSmartTV() {
    var titulo by remember { mutableStateOf("Bienvenido a ExpoGuía") }
    var informacionExtensa by remember { mutableStateOf("Seleccione una sala desde su dispositivo móvil para proyectar la información detallada y la cédula museográfica en esta pantalla.") }
    var horario by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("https://picsum.photos/seed/museo/800/600") }

    DisposableEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("salaSeleccionada")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valor = snapshot.getValue(String::class.java)
                if (valor != null) {
                    val partes = valor.split("|")
                    if (partes.size >= 4) {
                        titulo = partes[0]
                        informacionExtensa = partes[1]
                        horario = partes[2]
                        imagenUrl = partes[3]
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listener)

        onDispose {
            ref.removeEventListener(listener)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Imagen grande de la sala
        AsyncImage(
            model = imagenUrl,
            contentDescription = "Imagen de la sala",
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth(0.45f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        // Panel de información detallada con scroll para textos largos
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🏛️ CÉDULA MUSEOGRÁFICA DIGITAL",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFB74D)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = titulo,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = informacionExtensa,
                fontSize = 18.sp,
                color = Color.LightGray,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (horario.isNotEmpty()) {
                Text(
                    text = horario,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}