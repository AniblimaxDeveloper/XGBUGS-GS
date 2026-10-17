# XGBUGS GS — Android starter

Starter aplikasi Android bertema gelap/merah dengan dashboard, tab Tools, Settings, serta simulasi lokal. Gambar yang kamu unggah dipakai sebagai ikon launcher.

**Batas fitur:** proyek ini tidak berisi bug WhatsApp, crash payload, spam, atau fitur yang mengganggu akun/perangkat lain. Tombol Tools hanya menjalankan hitungan simulasi lokal.

## Build APK lewat GitHub Actions (pakai Termux)

1. Buat repository baru di GitHub bernama `XGBUGS-GS`.
2. Di Termux, jalankan:
   ```sh
   pkg update -y
   pkg install git -y
   termux-setup-storage
   cd ~/storage/downloads
   unzip XGBUGS-GS-starter.zip
   cd XGBUGS-GS
   git init
   git branch -M main
   git add .
   git commit -m "Initial XGBUGS GS app"
   git remote add origin https://github.com/USERNAME/XGBUGS-GS.git
   git push -u origin main
   ```
   Ganti `USERNAME` dengan username GitHub kamu. Saat diminta login, gunakan alur autentikasi GitHub yang tersedia; password akun biasa tidak diterima untuk Git HTTPS.
3. Buka repository di GitHub → **Actions** → workflow **Build XGBUGS GS APK**.
4. Setelah selesai, buka run terbaru dan unduh artifact **XGBUGS-GS-debug-apk**.
5. Ekstrak ZIP artifact dan install `app-debug.apk` di ponsel untuk pengujian.

## Catatan
- Package: `com.xgbugs.gs`
- Minimum Android: Android 8.0 (API 26)
- Build: JDK 17 + Gradle 8.13 + Android Gradle Plugin 8.13.2
- Ini starter awal; workflow akan memverifikasi build sebenarnya di GitHub Actions.
