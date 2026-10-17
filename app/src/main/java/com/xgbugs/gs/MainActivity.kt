package com.xgbugs.gs

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var root: LinearLayout
    private val logs = mutableListOf<String>()
    private var currentTab = "PANEL"
    private var saveLogs = true
    private var confirmBeforeOpen = true
    private var highContrast = false
    private var demoCount = 0

    private val bg = Color.rgb(8, 9, 13)
    private val panel = Color.rgb(17, 19, 27)
    private val panelAlt = Color.rgb(24, 27, 37)
    private val white = Color.rgb(242, 243, 248)
    private val muted = Color.rgb(151, 155, 170)
    private val line = Color.rgb(43, 46, 60)
    private val green = Color.rgb(82, 230, 161)
    private val red: Int
        get() = if (highContrast) Color.rgb(255, 82, 103) else Color.rgb(255, 45, 69)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
            window.isStatusBarContrastEnforced = false
        }

        prefs = getSharedPreferences("xgbugs_gs_settings", MODE_PRIVATE)
        saveLogs = prefs.getBoolean("save_logs", true)
        confirmBeforeOpen = prefs.getBoolean("confirm_open", true)
        highContrast = prefs.getBoolean("high_contrast", false)
        if (saveLogs) {
            val stored = prefs.getString("logs", "[]") ?: "[]"
            runCatching {
                val array = JSONArray(stored)
                for (i in 0 until array.length()) logs.add(array.optString(i))
            }
        }
        render()
    }

    private fun render() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(16), dp(10), dp(16), dp(10))
        }

        root.addView(buildHeader(), LinearLayout.LayoutParams(-1, dp(82)))
        addGap(root, 10)
        root.addView(buildNavigation(), LinearLayout.LayoutParams(-1, dp(48)))
        addGap(root, 12)

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, dp(12))
        }
        when (currentTab) {
            "PANEL" -> buildPanel(content)
            "TOOLS" -> buildTools(content)
            "LOGS" -> buildLogs(content)
            "SETTINGS" -> buildSettings(content)
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        addGap(root, 8)
        val footer = label("XGBUGS GS  •  LOCAL SAFE MODE  •  v5.0", 10, muted, true).apply {
            gravity = Gravity.CENTER
            letterSpacing = 0.06f
            setPadding(0, dp(4), 0, dp(2))
        }
        root.addView(footer, LinearLayout.LayoutParams(-1, dp(20)))
        setContentView(root)
    }

    private fun buildHeader(): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val art = ImageView(this).apply {
            setImageResource(R.drawable.xgbugs_art)
            scaleType = ImageView.ScaleType.CENTER_CROP
            background = rounded(panelAlt, dp(14), line, dp(1))
            clipToOutline = true
            contentDescription = "Ikon XGBUGS GS"
        }
        row.addView(art, LinearLayout.LayoutParams(dp(68), dp(68)))
        addGapHorizontal(row, 12)

        val texts = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
        }
        texts.addView(label("XGBUGS", 25, white, true).apply {
            typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
            letterSpacing = 0.08f
        })
        texts.addView(label("GS  /  DIAGNOSTIC CONSOLE", 10, red, true).apply {
            letterSpacing = 0.08f
        })
        row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))

        val badge = label("LOCAL", 10, green, true).apply {
            gravity = Gravity.CENTER
            setPadding(dp(10), dp(7), dp(10), dp(7))
            background = rounded(Color.rgb(13, 38, 31), dp(20), Color.rgb(29, 87, 65), dp(1))
        }
        row.addView(badge)
        return row
    }

    private fun buildNavigation(): View {
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            background = rounded(panel, dp(14), line, dp(1))
            setPadding(dp(4), dp(4), dp(4), dp(4))
        }
        val tabs = listOf("PANEL", "TOOLS", "LOGS", "SETTINGS")
        tabs.forEach { tab ->
            val selected = tab == currentTab
            val item = label(tab, 10, if (selected) white else muted, true).apply {
                gravity = Gravity.CENTER
                background = rounded(if (selected) red else Color.TRANSPARENT, dp(10))
                setPadding(dp(3), dp(9), dp(3), dp(9))
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    currentTab = tab
                    render()
                }
            }
            nav.addView(item, LinearLayout.LayoutParams(0, -1, 1f))
        }
        return nav
    }

    private fun buildPanel(parent: LinearLayout) {
        parent.addView(label("CONTROL CENTER", 11, red, true).apply { letterSpacing = 0.15f })
        addGap(parent, 6)
        parent.addView(label("Panel aman untuk pemeriksaan lokal", 21, white, true))
        parent.addView(label("Tidak mengirim payload, pesan, atau permintaan ke WhatsApp.", 12, muted, false))
        addGap(parent, 16)

        val status = card()
        val statusTop = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val dot = label("●", 17, green, true)
        statusTop.addView(dot)
        addGapHorizontal(statusTop, 8)
        statusTop.addView(label("SAFE MODE AKTIF", 14, green, true), LinearLayout.LayoutParams(0, -2, 1f))
        statusTop.addView(label("NO NETWORK", 9, muted, true))
        status.addView(statusTop)
        addGap(status, 8)
        status.addView(label("Semua pemeriksaan berjalan di dalam aplikasi ini dan menggunakan data contoh.", 12, muted, false))
        parent.addView(status)
        addGap(parent, 12)

        val metrics = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        metrics.addView(metricCard("0", "TARGET LUAR"), LinearLayout.LayoutParams(0, dp(88), 1f))
        addGapHorizontal(metrics, 8)
        metrics.addView(metricCard("100%", "LOKAL"), LinearLayout.LayoutParams(0, dp(88), 1f))
        addGapHorizontal(metrics, 8)
        metrics.addView(metricCard("0", "PAYLOAD"), LinearLayout.LayoutParams(0, dp(88), 1f))
        parent.addView(metrics)
        addGap(parent, 16)

        parent.addView(actionCard(
            "▶  JALANKAN SIMULASI",
            "Membuat event dummy dan menambahkan catatan lokal.",
            red
        ) { runSimulation() })
        addGap(parent, 10)
        parent.addView(actionCard(
            "⌕  PERIKSA KONFIGURASI",
            "Memeriksa versi aplikasi ini dan versi Android.",
            panelAlt
        ) { runLocalCheck() })
        addGap(parent, 10)
        parent.addView(actionCard(
            "◉  BUKA WHATSAPP",
            "Hanya membuka aplikasi WhatsApp yang terpasang.",
            panelAlt
        ) { requestOpenWhatsApp() })
        addGap(parent, 16)
        parent.addView(noteCard("CATATAN KEAMANAN", "XGBUGS GS tidak memodifikasi WhatsApp, tidak membaca chat, dan tidak mengirim data ke server.", green))
    }

    private fun buildTools(parent: LinearLayout) {
        parent.addView(label("SAFE TOOLS", 11, red, true).apply { letterSpacing = 0.15f })
        addGap(parent, 6)
        parent.addView(label("Peralatan lokal", 22, white, true))
        parent.addView(label("Alat di sini hanya menguji komponen aplikasi XGBUGS GS.", 12, muted, false))
        addGap(parent, 14)

        parent.addView(actionCard("⌕  PEMERIKSAAN LOKAL", "Catat versi aplikasi dan platform tanpa menyentuh data pribadi.", panel) {
            runLocalCheck()
        })
        addGap(parent, 10)
        parent.addView(actionCard("⚡  BUAT EVENT DEMO", "Buat catatan uji dummy; tidak ada pengiriman jaringan.", panel) {
            runSimulation()
        })
        addGap(parent, 10)
        parent.addView(actionCard("▣  INFO APLIKASI", "Buka halaman info aplikasi ini di pengaturan Android.", panel) {
            openOwnAppSettings()
        })
        addGap(parent, 14)
        parent.addView(noteCard("BATASAN SIMULASI", "Tidak tersedia fitur crash, spam, eksploitasi, atau gangguan akun/perangkat WhatsApp. Data demo hanya untuk antarmuka dan log.", red))
    }

    private fun buildLogs(parent: LinearLayout) {
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val headings = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        headings.addView(label("ACTIVITY LOG", 11, red, true))
        headings.addView(label("Catatan aktivitas pada perangkat ini", 19, white, true))
        header.addView(headings, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(smallButton("BERSIHKAN", red) { clearLogs() })
        parent.addView(header)
        addGap(parent, 12)

        if (logs.isEmpty()) {
            parent.addView(noteCard("BELUM ADA LOG", "Jalankan simulasi atau pemeriksaan lokal untuk menampilkan catatan di sini.", muted))
        } else {
            logs.forEach { entry ->
                val logCard = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dp(12), dp(12), dp(12), dp(12))
                    background = rounded(panel, dp(12), line, dp(1))
                }
                logCard.addView(label("›  $entry", 12, white, false).apply { typeface = Typeface.MONOSPACE })
                parent.addView(logCard)
                addGap(parent, 7)
            }
        }
        addGap(parent, 8)
        parent.addView(label(if (saveLogs) "Penyimpanan log: aktif di perangkat" else "Penyimpanan log: hanya selama sesi ini", 11, muted, false))
    }

    private fun buildSettings(parent: LinearLayout) {
        parent.addView(label("CONFIGURATION", 11, red, true).apply { letterSpacing = 0.15f })
        addGap(parent, 6)
        parent.addView(label("Pengaturan", 22, white, true))
        parent.addView(label("Preferensi disimpan secara lokal di perangkat.", 12, muted, false))
        addGap(parent, 14)

        parent.addView(settingRow(
            "Simpan riwayat lokal",
            "Menyimpan hingga 50 baris log di perangkat.",
            saveLogs
        ) {
            saveLogs = !saveLogs
            prefs.edit().putBoolean("save_logs", saveLogs).apply()
            if (!saveLogs) prefs.edit().remove("logs").apply() else persistLogs()
            addLog(if (saveLogs) "Penyimpanan log lokal diaktifkan." else "Penyimpanan log lokal dinonaktifkan.")
            render()
        })
        addGap(parent, 9)
        parent.addView(settingRow(
            "Konfirmasi buka WhatsApp",
            "Tampilkan dialog sebelum berpindah ke WhatsApp.",
            confirmBeforeOpen
        ) {
            confirmBeforeOpen = !confirmBeforeOpen
            prefs.edit().putBoolean("confirm_open", confirmBeforeOpen).apply()
            addLog("Preferensi konfirmasi WhatsApp diubah.")
            render()
        })
        addGap(parent, 9)
        parent.addView(settingRow(
            "Kontras aksen tinggi",
            "Mencerahkan warna merah pada antarmuka.",
            highContrast
        ) {
            highContrast = !highContrast
            prefs.edit().putBoolean("high_contrast", highContrast).apply()
            addLog("Preferensi kontras tampilan diubah.")
            render()
        })
        addGap(parent, 16)
        parent.addView(noteCard("INFORMASI", "Nama: XGBUGS GS\nVersi: 5.0\nMode: lokal / simulasi\nIzin khusus: tidak ada", muted))
        addGap(parent, 10)
        parent.addView(actionCard("↻  RESET PREFERENSI", "Kembalikan pengaturan ke nilai awal.", panel) {
            AlertDialog.Builder(this)
                .setTitle("Reset pengaturan?")
                .setMessage("Preferensi akan dikembalikan ke nilai awal. Log sesi saat ini tidak dihapus.")
                .setNegativeButton("BATAL", null)
                .setPositiveButton("RESET") { _, _ ->
                    saveLogs = true
                    confirmBeforeOpen = true
                    highContrast = false
                    prefs.edit()
                        .putBoolean("save_logs", true)
                        .putBoolean("confirm_open", true)
                        .putBoolean("high_contrast", false)
                        .apply()
                    persistLogs()
                    addLog("Preferensi dikembalikan ke nilai awal.")
                    render()
                }
                .show()
        })
    }

    private fun metricCard(value: String, caption: String): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = rounded(panel, dp(12), line, dp(1))
        }
        box.addView(label(value, 20, red, true))
        box.addView(label(caption, 8, muted, true).apply { letterSpacing = 0.04f })
        return box
    }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(14), dp(14), dp(14), dp(14))
        background = rounded(panel, dp(14), line, dp(1))
    }

    private fun actionCard(title: String, subtitle: String, color: Int, action: () -> Unit): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            background = rounded(color, dp(14), if (color == red) red else line, dp(1))
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
        }
        box.addView(label(title, 14, white, true))
        addGap(box, 4)
        box.addView(label(subtitle, 11, if (color == red) Color.WHITE else muted, false))
        return box
    }

    private fun settingRow(title: String, subtitle: String, enabled: Boolean, action: () -> Unit): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(13), dp(13), dp(13), dp(13))
            background = rounded(panel, dp(13), line, dp(1))
        }
        val textGroup = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        textGroup.addView(label(title, 13, white, true))
        addGap(textGroup, 3)
        textGroup.addView(label(subtitle, 10, muted, false))
        row.addView(textGroup, LinearLayout.LayoutParams(0, -2, 1f))
        addGapHorizontal(row, 8)
        row.addView(smallButton(if (enabled) "ON" else "OFF", if (enabled) red else panelAlt, action))
        row.isClickable = true
        row.setOnClickListener { action() }
        return row
    }

    private fun noteCard(title: String, body: String, accent: Int): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(13), dp(13), dp(13), dp(13))
            background = rounded(panel, dp(12), line, dp(1))
        }
        box.addView(label(title, 10, accent, true).apply { letterSpacing = 0.08f })
        addGap(box, 6)
        box.addView(label(body, 11, muted, false))
        return box
    }

    private fun smallButton(text: String, color: Int, action: () -> Unit): View {
        return TextView(this).apply {
            this.text = text
            textSize = 9f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(white)
            gravity = Gravity.CENTER
            setPadding(dp(10), dp(10), dp(10), dp(10))
            background = rounded(color, dp(8), if (color == red) red else line, dp(1))
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
        }
    }

    private fun runSimulation() {
        demoCount += 1
        addLog("Simulasi #$demoCount selesai: event dummy lokal dibuat; tidak ada data dikirim.")
        Toast.makeText(this, "Simulasi lokal selesai — tidak ada payload dikirim", Toast.LENGTH_SHORT).show()
    }

    private fun runLocalCheck() {
        val version = runCatching {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        }.getOrDefault("unknown")
        addLog("Pemeriksaan lokal: XGBUGS GS $version / Android API ${Build.VERSION.SDK_INT}.")
        Toast.makeText(this, "Pemeriksaan lokal selesai", Toast.LENGTH_SHORT).show()
    }

    private fun requestOpenWhatsApp() {
        if (confirmBeforeOpen) {
            AlertDialog.Builder(this)
                .setTitle("Buka WhatsApp?")
                .setMessage("Aplikasi hanya akan membuka WhatsApp. XGBUGS GS tidak mengirim pesan atau menjalankan tes terhadap WhatsApp.")
                .setNegativeButton("BATAL", null)
                .setPositiveButton("BUKA") { _, _ -> openWhatsAppNow() }
                .show()
        } else {
            openWhatsAppNow()
        }
    }

    private fun openWhatsAppNow() {
        val intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
            ?: packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
        if (intent != null) {
            addLog("WhatsApp dibuka melalui tindakan pengguna.")
            startActivity(intent)
        } else {
            addLog("WhatsApp tidak ditemukan pada perangkat ini.")
            Toast.makeText(this, "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openOwnAppSettings() {
        addLog("Halaman info aplikasi dibuka.")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")))
    }

    private fun clearLogs() {
        AlertDialog.Builder(this)
            .setTitle("Hapus log?")
            .setMessage("Semua catatan aktivitas XGBUGS GS akan dihapus.")
            .setNegativeButton("BATAL", null)
            .setPositiveButton("HAPUS") { _, _ ->
                logs.clear()
                prefs.edit().remove("logs").apply()
                render()
                Toast.makeText(this, "Log dibersihkan", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun addLog(message: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        logs.add(0, "$time  $message")
        while (logs.size > 50) logs.removeAt(logs.lastIndex)
        persistLogs()
    }

    private fun persistLogs() {
        if (!::prefs.isInitialized) return
        if (saveLogs) {
            val array = JSONArray()
            logs.forEach { array.put(it) }
            prefs.edit().putString("logs", array.toString()).apply()
        } else {
            prefs.edit().remove("logs").apply()
        }
    }

    private fun label(text: String, size: Int, color: Int, bold: Boolean): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            includeFontPadding = true
            setLineSpacing(dp(1).toFloat(), 1f)
        }
    }

    private fun rounded(fill: Int, radius: Int, strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0): GradientDrawable {
        return GradientDrawable().apply {
            setColor(fill)
            cornerRadius = radius.toFloat()
            if (strokeWidth > 0) setStroke(strokeWidth, strokeColor)
        }
    }

    private fun addGap(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun addGapHorizontal(parent: LinearLayout, width: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(dp(width), 1))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()
}
