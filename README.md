# XGBUGS GS V5 — Android project

Aplikasi Android native bertema cyber-horror hitam-merah. Paket ini adalah V5 dengan ikon/artwork dari gambar yang diberikan, dashboard, tools lokal, activity logs, settings, dan workflow untuk build APK debug melalui GitHub Actions.

## Fitur V5

- Dashboard/status panel bertema gelap dengan aksen merah.
- Ikon aplikasi dan artwork menggunakan gambar yang diberikan.
- Tab PANEL, TOOLS, LOGS, dan SETTINGS.
- Simulasi event lokal (tidak mengirim payload atau lalu lintas jaringan).
- Pemeriksaan lokal untuk versi aplikasi dan versi Android.
- Activity log lokal, maksimal 50 baris, dapat dibersihkan.
- Pengaturan penyimpanan log, konfirmasi saat membuka WhatsApp, dan kontras aksen.
- Tombol membuka WhatsApp biasa/WhatsApp Business yang terpasang, dengan konfirmasi opsional.
- Tombol menuju halaman info aplikasi Android.
- Tidak meminta izin khusus dan tidak mengumpulkan isi chat atau kontak.

Aplikasi ini bukan sistem operasi Android baru, melainkan aplikasi Android yang berjalan di atas sistem Android perangkat. Panel hanya menjalankan diagnostik dan simulasi lokal; tidak berisi exploit, crash payload, spam, auto-message, pencurian akun, atau fungsi untuk mengganggu WhatsApp/perangkat orang lain.

## Build toolchain yang dikunci

- JDK 17
- Android Gradle Plugin 8.7.2
- Gradle 8.9
- Kotlin Gradle Plugin 2.1.20
- Compile SDK 35 / Build Tools 35.0.0
- `sourceCompatibility`, `targetCompatibility`, dan Kotlin `jvmTarget` semuanya 17

## Cara pakai Termux + GitHub

Sebelum memulai, unduh ZIP ini ke folder **Download** Android. Pastikan repository GitHub `AniblimaxDeveloper/XGBUGS-GS` sudah ada dan kamu punya akses push.

### A. Ekstrak versi V5 ke samping proyek lama

```bash
pkg update -y
pkg install unzip rsync git -y
termux-setup-storage
cd ~/projects
unzip -o ~/storage/downloads/XGBUGS-GS-V5.zip -d ~/projects
```

### B. Ganti file proyek lama tanpa menghapus riwayat Git

```bash
rsync -av --delete --exclude='.git/' ~/projects/XGBUGS-GS-V5/ ~/projects/XGBUGS-GS/
cd ~/projects/XGBUGS-GS
rm -f app/build.gradle.ktso
```

Periksa remote dan perubahan:

```bash
git remote -v
git status
git diff -- app/build.gradle.kts build.gradle.kts .github/workflows/android.yml
```

Pastikan `origin` mengarah ke `https://github.com/AniblimaxDeveloper/XGBUGS-GS.git`. Jika berbeda:

```bash
git remote set-url origin https://github.com/AniblimaxDeveloper/XGBUGS-GS.git
```

Lalu commit dan push:

```bash
git add -A
git commit -m "Build XGBUGS GS V5"
git push origin main
```

Jika Git meminta identitas, set satu kali (pakai email GitHub milikmu):

```bash
git config --global user.name "AniblimaxDeveloper"
git config --global user.email "EMAIL_GITHUB_KAMU"
```

Ganti `EMAIL_GITHUB_KAMU` sebelum menjalankan perintah email.

### C. Unduh APK hasil build

1. Buka `https://github.com/AniblimaxDeveloper/XGBUGS-GS/actions`.
2. Buka run terbaru untuk workflow **Build XGBUGS GS V5 APK**.
3. Tunggu sampai run berstatus hijau.
4. Unduh artifact **XGBUGS-GS-V5-debug-apk**.
5. Ekstrak ZIP artifact, lalu instal `app-debug.apk` di ponsel untuk pengujian.

## Troubleshooting

- Jika workflow tidak muncul, periksa bahwa folder `.github/workflows` ikut terkirim ke repository.
- Jika muncul `nothing to commit`, pastikan `rsync` selesai dan periksa `git diff`.
- Jika target JVM gagal, pastikan `app/build.gradle.kts` memuat ketiga pengaturan JVM 17.
- Jika build gagal lagi, kirim bagian log mulai `What went wrong` sampai akhir. APK belum dianggap berhasil dibuat sebelum Actions berstatus hijau.
