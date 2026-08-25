package com.expoguia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                PantallaMovilControl()
            }
        }
    }
}

data class Exposicion(val titulo: String, val descripcion: String, val horario: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovilControl() {
    // Lista ampliada con más elementos culturales
    val exposiciones = listOf(
        Exposicion("Sala 1: Arte Prehispánico", "Piezas arqueológicas de Mesoamérica y el occidente de México.", "10:00 AM - 5:00 PM"),
        Exposicion("Sala 2: Pintura Virreinal", "Obras sacras y retratos de la época colonial en México.", "10:00 AM - 5:00 PM"),
        Exposicion("Sala 3: Muralismo Mexicano", "Reproducciones y estudios de los grandes maestros del siglo XX.", "11:00 AM - 6:00 PM"),
        Exposicion("Sala 4: Arte Popular e Indígena", "Textiles, alfarería y máscaras tradicionales de diversas regiones.", "10:00 AM - 4:00 PM"),
        Exposicion("Sala 5: Fotografía Contemporánea", "Exhibición visual de fotoperiodismo y paisajes urbanos.", "12:00 PM - 7:00 PM")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ExpoGuía - Control de Salas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Selecciona una sala para proyectarla en la Smart TV:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            items(exposiciones) { expo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = {
                        // Enviamos la información completa a Firebase
                        val database = FirebaseDatabase.getInstance()
                        val ref = database.getReference("salaSeleccionada")
                        ref.setValue("${expo.titulo}\n\n${expo.descripcion}\nHorario: ${expo.horario}")
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = expo.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = expo.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Horario: ${expo.horario}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}