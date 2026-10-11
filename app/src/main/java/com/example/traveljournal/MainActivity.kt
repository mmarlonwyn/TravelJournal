package com.example.traveljournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Bg = Color(0xFF111827)
private val Surface = Color(0xFF1F2937)
private val Outline = Color(0xFF374151)
private val Accent = Color(0xFF5EB6F0)
private val Muted = Color(0xFF9CA3AF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { TravelJournalScreen() }
        }
    }
}

@Composable
fun TravelJournalScreen() {
    var spot by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var satisfaction by remember { mutableStateOf("Luar Biasa ★") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Bg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val name = spot.ifBlank { "(belum diisi)" }
                    scope.launch {
                        snackbarHostState.showSnackbar("Jurnal tersimpan: $name")
                    }
                },
                containerColor = Accent,
                contentColor = Color(0xFF0B1220),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF3B6FC9))),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text("LOG PERJALANAN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBFD6FF))
                    Text("Tromsø, Norway ❄️", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Ekspedisi Aurora Borealis", fontSize = 12.sp, color = Color(0xFFD6E4FF))
                }
            }

            Spacer(Modifier.height(14.dp))

            JournalField(
                label = "SPOT WISATA FAVORIT",
                value = spot,
                onValueChange = { spot = it },
                placeholder = "Contoh: Fjellheisen Cable Car",
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))

            JournalField(
                label = "APA YG PALING KAMU NIKMATI?",
                value = experience,
                onValueChange = { experience = it },
                placeholder = "Ceritakan pengalamanmu...",
                singleLine = false,
                minLines = 3
            )
            Spacer(Modifier.height(12.dp))

            JournalField(
                label = "CATATAN TAMBAHAN",
                value = notes,
                onValueChange = { notes = it },
                placeholder = "(Ketik perlengkapan ekstra di sini.....)",
                singleLine = false
            )

            Spacer(Modifier.height(16.dp))
            Text("TINGKAT KEPUASAN:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Muted)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Biasa", "Seru", "Luar Biasa ★").forEach { level ->
                    val selected = satisfaction == level
                    Box(
                        modifier = Modifier
                            .background(if (selected) Accent else Bg, RoundedCornerShape(50))
                            .border(1.dp, if (selected) Accent else Outline, RoundedCornerShape(50))
                            .clickable { satisfaction = level }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            level, fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) Color(0xFF0B1220) else Muted
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                buildStatus(spot, experience, notes),
                fontSize = 13.sp, color = Muted
            )
            Spacer(Modifier.height(80.dp))
        }
    }
}

private fun buildStatus(spot: String, exp: String, notes: String): String =
    if (spot.isBlank() && exp.isBlank() && notes.isBlank()) "Status: Draft Kosong"
    else "Status: Draft Tersimpan"

@Composable
fun JournalField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    minLines: Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(12.dp))
            .border(1.dp, Outline, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Muted)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            placeholder = { Text(placeholder, color = Color(0xFF6B7280), fontSize = 14.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Accent
            )
        )
    }
}