# XGBUGS GS — Android starter project (fixed build configuration)

Aplikasi Android native dengan antarmuka gelap, aksen merah, artwork/ikon dari gambar pengguna, dan tab PANEL, TOOLS, LOGS, serta SETTINGS.

## Build toolchain yang dikunci

- JDK: 17
- Android Gradle Plugin: 8.7.2
- Gradle: 8.9
- Kotlin Gradle Plugin: 2.1.20
- Compile SDK: 35
- Minimum Android: API 23

Target Java dan Kotlin sama-sama ditetapkan ke JVM 17 pada `app/build.gradle.kts`. Workflow memeriksa konfigurasi tersebut sebelum build untuk membantu mencegah error target 1.8 vs 17.

## Fitur

- Dashboard dengan tema hitam-merah cyber-horror.
- Artwork dari gambar yang diberikan untuk ikon dan header.
- Simulasi event lokal dan pemeriksaan versi/platform.
- Activity log lokal yang bisa dibersihkan dan penyimpanan log opsional.
- Pengaturan konfirmasi sebelum membuka WhatsApp, penyimpanan log, dan kontras aksen.
- Tombol untuk membuka aplikasi WhatsApp yang sudah terpasang atau halaman info aplikasi Android.

## Batas penggunaan

Aplikasi ini adalah panel diagnostik/simulasi lokal. Tidak ada exploit WhatsApp, crash payload, spam, pengiriman pesan otomatis, pengambilalihan akun, atau pengumpulan isi chat/kontak. Tombol WhatsApp hanya menjalankan aplikasi yang sudah dipasang setelah tindakan pengguna.

## Build melalui GitHub Actions

1. Buat repository kosong bernama `XGBUGS-GS` di GitHub (jika repo lama sudah ada, gunakan repo itu dan jangan membuat repo duplikat).
2. Unggah seluruh isi folder proyek ke branch `main`, bukan file ZIP-nya.
3. Buka tab **Actions**. Workflow akan berjalan otomatis pada push atau bisa dimulai lewat **Run workflow**.
4. Jika run berstatus hijau, unduh artifact `XGBUGS-GS-debug-apk`, ekstrak ZIP, lalu instal `app-debug.apk` untuk pengujian.

## Perbarui proyek yang sudah ada dari Termux

Setelah file ZIP ini berada di folder Download:

```bash
pkg update -y
pkg install unzip rsync git -y
cd ~/projects
unzip -o ~/storage/downloads/XGBUGS-GS-fixed.zip -d ~/projects
rsync -av --delete --exclude='.git/' ~/projects/XGBUGS-GS-FIXED/ ~/projects/XGBUGS-GS/
cd ~/projects/XGBUGS-GS
rm -f app/build.gradle.ktso
```

Perintah `rsync` menyamakan isi folder proyek dengan versi baru, sambil mempertahankan folder `.git` milik repository lama. Periksa dulu remote dan status sebelum mengirim:

```bash
git remote -v
git status
```

Pastikan remote menunjuk ke repository yang benar. Kemudian:

```bash
git add -A
git commit -m "Fix Android build toolchain and JVM targets"
git push origin main
```

Jika Git mengatakan `nothing to commit`, periksa apakah proses `rsync` berhasil dan `git diff -- app/build.gradle.kts build.gradle.kts .github/workflows/android.yml` menampilkan perubahan.

## Catatan

Paket ini sudah diperiksa konsistensi konfigurasi dan struktur file secara statis, tetapi build APK aktual tetap harus dikonfirmasi oleh GitHub Actions. APK yang dihasilkan workflow adalah APK debug untuk pengujian, bukan rilis produksi.
