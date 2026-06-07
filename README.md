# 🚚 Ekspedisi Mas Roi (EMR)

> **Sistem Manajemen Logistik Terpadu Berbasis Bare-Metal Java**

Project ini dibuat untuk memenuhi kewajiban penugasan project Pemrograman Berorientasi Objek (PBO) - Mata Kuliah Teori
Dosen Pengampu: Bpk. Rudi Cahyadi S.Si., M.T.

### 👨‍💻 Tim Developer

* **Rio Meidi Ataurrahman** (123240175)
* **Erlan Rifqi** (123240173)
* **Gian Abi Firdaus** (123240017)

---

## 📑 Executive Summary

**Ekspedisi Mas Roi (EMR)** adalah sistem manajemen logistik terpadu yang dibangun dari nol menggunakan bahasa Java
murni (Java SE). Proyek ini menghindari penggunaan *framework* instan demi mempertahankan kontrol penuh pada arsitektur
*Model-View-Controller* (MVC) dan eksekusi memori secara *bare-metal*.

Sistem ini ditenagai oleh database MariaDB melalui koneksi JDBC yang dioptimalkan, serta mengimplementasikan keamanan
melalui *Role-Based Access Control* (RBAC) dengan pemisahan wewenang operasional yang sangat ketat.

## 🛠️ Stack Teknologi

* **Bahasa Utama:** Java (Murni / Tanpa Framework)
* **Arsitektur:** Model-View-Controller (MVC) & Data Access Object (DAO)
* **Database:** MariaDB (JDBC)
* **Struktur Data:** Data Transfer Object (DTO) / Java Record
* **Antarmuka (GUI):** Java Swing
* **Integrasi Perangkat:** Kamera Scanner Barcode/QR Code (ZXing & Webcam Capture OpenIMAJ)
* **Build Tool:** Gradle

## 🔐 Role-Based Access Control (RBAC)

Sistem ini terbagi menjadi 4 kasta akun dengan *privilege* yang terisolasi:

1. **A - Admin (Superuser):** Memegang kendali penuh atas *Create, Read, Update, Delete* (CRUD) seluruh data *User* (
   Pekerja Ekspedisi).
2. **L - Loket:** Pintu gerbang utama logistik. Bertugas menerima paket dari pelanggan, merekam data entitas paket, dan
   menerbitkan Nomor Resi.
3. **G - Gudang:** Pusat pemrosesan. Bertugas menerima muatan, menyortir, dan menyiapkan penugasan armada. Paket yang
   berada dalam penguasaan (state) Gudang dikunci mutlak.
4. **K - Kurir:** Pasukan tempur lapangan (*Long Distance Courier*). Memiliki wewenang mengambil paket dari gudang/loket
   dan mendistribusikannya hingga ke penerima akhir.

## 📦 Flow Logistik & SOP Sistem (*State Machine*)

Arsitektur pergerakan data dirancang agar arus waktu logistik hanya bisa bergerak maju:

* **Fase 1: Registrasi (Loket)** - Paket didaftarkan, dicetak identitas DTO-nya, serta di-generate Resi beserta *QR
  Code*. Status: `Diterima di Loket`.
* **Fase 2: Transit Awal** - Kurir memindai resi untuk mengambil paket dari loket menuju fasilitas gudang penyortiran.
* **Fase 3: Pemrosesan Terkunci (Gudang)** - Saat Gudang melakukan pemindaian, sistem memblokir akses Kurir & Loket.
  Gudang menyortir hingga paket siap diberangkatkan. Status: `Transit Gudang`.
* **Fase 4: Distribusi Lanjutan** - Kurir mengambil muatan dari gudang dan mengantarkannya. Status berakhir ketika kurir
  menyelesaikan pengiriman: `Paket Telah Diterima`.

## ⚙️ Cara Menjalankan Project (Setup)

1. **Clone Repository**
   ```bash
   git clone <repository-url>
   cd EkspedisiMasRoi
    ```

2. **Setup Database MariaDB**

* Pastikan service MariaDB (misal via XAMPP) berjalan.
* Buat database baru (misal: `ekspedisi_db_prak`).
* Import *schema* dan *dummy data* dari file `database/ekspedisi_db_prak.sql`.


3. **Konfigurasi Environment**

* *Copy* file `.env.example` dan ubah namanya menjadi `.env`.
* Sesuaikan *credentials* database di dalam file `.env`:

    ```env
    DB_HOST="localhost"
    DB_PORT="3306"
    DB_USER="root"
    DB_PASS=""
    DB_NAME="ekspedisi_db_prak"
    ```

4. **Build & Run Aplikasi**
   Aplikasi dapat langsung dijalankan menggunakan Gradle *wrapper* bawaan.

    ```bash
    ./gradlew run
    ```

---