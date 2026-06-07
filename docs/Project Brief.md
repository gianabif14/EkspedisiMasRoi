# Ekspedisi Mas Roi

**Project Besar PBO: Erlan, Roi, dan Gian**

*Mata Kuliah Pemrograman Berbasis Objek Teori Dosen Pengampu: Bpk. Rudi Cahyadi S.Si., M.T*

---

## Executive Summary

**Ekspedisi Mas Roi (EMR)** adalah sistem manajemen logistik terpadu yang dibangun dari nol menggunakan bahasa Java
murni (Java SE). Proyek ini menolak penggunaan *framework* instan demi mempertahankan kontrol penuh pada arsitektur
*Model-View-Controller* (MVC) dan eksekusi memori secara *bare-metal*.

Sistem ini ditenagai oleh relasi *database* MariaDB melalui koneksi JDBC yang dioptimalkan, serta mengimplementasikan
keamanan tingkat tinggi melalui *Role-Based Access Control* (RBAC) dengan pemisahan wewenang operasional yang sangat
ketat.

## Stack Teknologi

* **Bahasa Utama:** Java (Murni / Tanpa Framework)
* **Arsitektur:** Model-View-Controller (MVC) & Data Access Object (DAO)
* **Database:** MariaDB (JDBC)
* **Struktur Data:** Data Transfer Object (DTO) / Java Record
* **Antarmuka (GUI):** Java Swing dengan integrasi utilitas *Camera Scanner* (ZXing)

## Role-Based Access Control (RBAC)

Sistem ini terbagi menjadi 4 kasta akun dengan Privilege yang terisolasi:

1. **A- Admin:** Superuser. Memegang kendali penuh atas *Create, Read, Update, Delete* (CRUD) seluruh data *User* (
   Pekerja Ekspedisi).
2. **L- Loket:** Pintu gerbang utama logistik. Bertugas menerima paket dari pelanggan, merekam data entitas paket, dan
   menerbitkan Nomor Resi.
3. **G- Gudang:** Pusat pemrosesan. Bertugas menerima muatan, menyortir, dan menyiapkan penugasan armada. Paket yang
   berada dalam penguasaan (state) Gudang dikunci mutlak dan tidak bisa dimanipulasi oleh *role* lain.
4. **K- Kurir:** Pasukan tempur lapangan (*Long Distance Courier*). Bertugas sebagai jembatan logistik antar-fasilitas (
   bukan pengantar ke *end-user* secara langsung). Hanya memiliki wewenang memindahkan paket masuk/keluar dari Gudang.

## Flow Logistik & SOP Sistem

Arsitektur pergerakan data dirancang agar arus waktu logistik hanya bisa bergerak maju (State Machine). Berikut adalah
SOP operasionalnya:

* **Fase 1: Registrasi (Loket)**
    * Setiap paket wajib didaftarkan di Loket untuk mendapatkan identitas DTO dan Resi.
* **Fase 2: Transit Awal**
    * *Skenario A (Terpusat):* Jika Loket menyatu dengan Gudang, muatan langsung dikuasai oleh Gudang.
    * *Skenario B (Soliter):* Jika Loket berdiri sendiri, Kurir harus menjemput paket dari Loket untuk dibawa ke Gudang.


* **Fase 3: Pemrosesan Terkunci (Gudang)**
    * Saat Gudang melakukan *scan* dan mengamankan paket, sistem secara otomatis akan memblokir *scanner* Kurir maupun
      Loket. Gudang melakukan penyortiran hingga paket bersiap untuk diberangkatkan.
* **Fase 4: Serah Terima & Distribusi Lanjutan (Gudang => Kurir)**
    * Gudang secara fisik menyerahkan keranjang paket ke Kurir. Kurir diwajibkan melakukan *scan* input manual melalui
      akunnya sendiri sebagai bukti serah terima (Penerimaan Mandiri) untuk kemudian didistribusikan ke fasilitas
      ekspedisi
      berikutnya.

---