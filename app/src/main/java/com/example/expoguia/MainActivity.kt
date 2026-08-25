package com.expoguia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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

data class Exposicion(
    val titulo: String,
    val descripcionCorta: String,
    val informacionExtensa: String,
    val horario: String,
    val imagenUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovilControl() {
    val exposiciones = listOf(
        Exposicion(
            titulo = "Sala 1: Arquitectura y Geometría",
            descripcionCorta = "Estudio visual de estructuras arquitectónicas y simetrías urbanas.",
            informacionExtensa = "Esta sala explora las líneas maestras de la arquitectura contemporánea y su interacción con la luz natural. A través de una perspectiva geométrica, se analizan los volúmenes, los espacios abiertos y la monumentalidad de las construcciones modernas.",
            horario = "10:00 AM - 5:00 PM",
            imagenUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800"
        ),
        Exposicion(
            titulo = "Sala 2: Minimalismo y Espacio",
            descripcionCorta = "La pureza de las formas simples y el manejo del espacio limpio.",
            informacionExtensa = "Una exhibición centrada en el concepto de que 'menos es más'. Las piezas y la distribución de la sala dialogan con el vacío, utilizando tonalidades neutras y estructuras depuradas para generar una experiencia de contemplación y serenidad visual.",
            horario = "10:00 AM - 5:00 PM",
            imagenUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800"
        ),
        Exposicion(
            titulo = "Sala 3: Arte Abstracto y Color",
            descripcionCorta = "Expresiones cromáticas y texturas aplicadas sobre gran formato.",
            informacionExtensa = "Un espacio vibrante que rompe con la representación figurativa tradicional para dar protagonismo absoluto al color, la textura y el gesto pictórico. Las obras invitan al espectador a interpretar las emociones a través de contrastes dinámicos.",
            horario = "11:00 AM - 6:00 PM",
            imagenUrl = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800"
        ),
        Exposicion(
            titulo = "Sala 4: Diseño Industrial y Objetos",
            descripcionCorta = "Innovación en mobiliario, ergonomía y materiales vanguardistas.",
            informacionExtensa = "Esta muestra analiza la evolución del diseño utilitario, destacando piezas donde la funcionalidad se fusiona con la estética escultórica. Se examinan el uso de maderas curvadas, metales y polímeros en la creación de objetos cotidianos.",
            horario = "10:00 AM - 4:00 PM",
            imagenUrl = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800"
        ),
        Exposicion(
            titulo = "Sala 5: Fotografía Contemporánea",
            descripcionCorta = "Exhibición visual de fotoperiodismo y paisajes urbanos.",
            informacionExtensa = "Una mirada a la lente moderna que captura las transformaciones sociales, la vida cotidiana en las grandes metrópolis y la naturaleza cambiante del entorno contemporáneo. Los autores utilizan la fotografía documental como un medio de reflexión crítica actual.",
            horario = "12:00 PM - 7:00 PM",
            imagenUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=800"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ExpoGuía - Salas Culturales", fontWeight = FontWeight.Bold) },
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
            items(exposiciones) { expo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    onClick = {
                        val database = FirebaseDatabase.getInstance()
                        val ref = database.getReference("salaSeleccionada")
                        ref.setValue("${expo.titulo}|${expo.informacionExtensa}|Horario: ${expo.horario}|${expo.imagenUrl}")
                    }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = expo.imagenUrl,
                            contentDescription = expo.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = expo.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = expo.descripcionCorta, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}