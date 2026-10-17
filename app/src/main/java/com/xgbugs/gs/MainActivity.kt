package com.xgbugs.gs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF08090D)
private val Panel = Color(0xFF14161D)
private val Red = Color(0xFFE53945)
private val Muted = Color(0xFF9DA3B4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { XgBugsApp() }
    }
}

@Composable
fun XgBugsApp() {
    var selected by remember { mutableStateOf("Dashboard") }
    var safeMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(false) }
    var animations by remember { mutableStateOf(true) }
    var testCount by remember { mutableIntStateOf(0) }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Red,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    val tabs = listOf("Dashboard", "Tools", "Settings")
                    val icons = listOf(Icons.Default.Dashboard, Icons.Default.Bolt, Icons.Default.Settings)
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = selected == tab,
                            onClick = { selected = tab },
                            icon = { Icon(icons[index], contentDescription = tab) },
                            label = { Text(tab, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Red, selectedTextColor = Red,
                                indicatorColor = Red.copy(alpha = .16f),
                                unselectedIconColor = Muted, unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                    .background(Bg).padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("XGBUGS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                        Text("GS  •  SAFE CONTROL PANEL", color = Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(color = Red.copy(alpha = .15f), shape = RoundedCornerShape(12.dp)) {
                        Row(Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, null, tint = Red, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(5.dp))
                            Text("SAFE", color = Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                when (selected) {
                    "Dashboard" -> {
                        Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
                            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("SYSTEM OVERVIEW", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Panel siap digunakan", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                                Text("Mode demonstrasi lokal aktif. Tidak mengirim bug, spam, atau gangguan ke WhatsApp.", color = Muted, fontSize = 13.sp)
                                HorizontalDivider(color = Color.White.copy(alpha = .08f))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Metric("STATUS", "ONLINE")
                                    Metric("MODE", "SIMULASI")
                                    Metric("TEST", testCount.toString())
                                }
                            }
                        }
                        SectionTitle("QUICK ACCESS")
                        FeatureRow(Icons.Default.Terminal, "Local diagnostics", "Periksa status panel lokal", "READY")
                        FeatureRow(Icons.Default.Security, "Safety guard", "Proteksi tindakan berisiko", if (safeMode) "ON" else "OFF")
                        FeatureRow(Icons.Default.History, "Activity log", "Tes lokal dijalankan: $testCount", "LOCAL")
                    }
                    "Tools" -> {
                        SectionTitle("LOCAL TEST TOOLS")
                        Text("Semua tombol di halaman ini hanya menjalankan simulasi di aplikasi, bukan mengirim serangan ke WhatsApp atau perangkat lain.", color = Muted, fontSize = 13.sp)
                        Surface(color = Panel, shape = RoundedCornerShape(16.dp) ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Panel pengujian", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Button(onClick = { testCount++ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Red)) {
                                    Icon(Icons.Default.PlayArrow, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Jalankan simulasi lokal")
                                }
                                OutlinedButton(onClick = { testCount = 0 }, modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Default.DeleteSweep, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Reset hitungan simulasi")
                                }
                                Text("Hasil simulasi: $testCount", color = Muted)
                            }
                        }
                    }
                    else -> {
                        SectionTitle("PREFERENCES")
                        SettingRow("Safe mode", "Mencegah tindakan berbahaya") { Switch(safeMode, { safeMode = it }, colors = SwitchDefaults.colors(checkedThumbColor = Red)) }
                        SettingRow("Notifications", "Notifikasi panel lokal") { Switch(notifications, { notifications = it }, colors = SwitchDefaults.colors(checkedThumbColor = Red)) }
                        SettingRow("UI animations", "Animasi antarmuka") { Switch(animations, { animations = it }, colors = SwitchDefaults.colors(checkedThumbColor = Red)) }
                        Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("APP INFORMATION", color = Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("XGBUGS GS  •  Version 1.0", fontWeight = FontWeight.SemiBold)
                                Text("Android dashboard demo • Offline local tools", color = Muted, fontSize = 12.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("XGBUGS GS  /  BUILD 1.0", color = Color(0xFF515665), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun Metric(label: String, value: String) {
    Column {
        Text(label, color = Muted, fontSize = 10.sp)
        Text(value, color = Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, color = Red, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
}

@Composable
fun FeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, status: String) {
    Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Red.copy(alpha = .14f), shape = RoundedCornerShape(10.dp)) {
                Icon(icon, null, tint = Red, modifier = Modifier.padding(10.dp).size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            Text(status, color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingRow(title: String, subtitle: String, control: @Composable () -> Unit) {
    Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            control()
        }
    }
}
