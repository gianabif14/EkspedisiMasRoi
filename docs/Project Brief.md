# Ekspedisi Mas Roi

## Project Besar Erlan, Roi, and Gian

Project ini dibuat untuk memenuhi tugas mata kuliah Pemrograman Berbasis Objek Teori yang diampu oleh Pak Rudy.

## Deskripsi Program

Program ini dibuat dengan arsitektur MVC dan Berbasis Java tanpa menggunakan framework. Program ini memiliki Fitur yang
berbasis Role / Role Based Access Control. Program ini Mengambil Tema Ekspedisi Yang memiliki 4 akun yaitu Admin, Kurir,
Gudang, dan Loket.

dimana Paket pertama tama akan didaftarkan di Loket untuk mendapatkan Resi Paket. Kemudian dari Loket bisa langsung
menuju Gudang atau Diambil Kurir. Loket yang bersama gudang benar benar ada di dunia nyata makannya paket bisa langsung
diambil gudang. Namun jika loket benar benar soliter maka Paket akan diambil oleh kurir. Kurir selanjutnya akan menuju
Gudang untuk mengantarkan Paket. Kurir disini adalah kurir Long Distance (Tidak mengantar sampai tujuan). Kurir hanya
boleh mengantarkan paket ke Gudang atau mengambil paket dari loket. Gudang berfungsi untuk menerima,menyortir ,dan
tempat penugasan kurir menuju fasilitas ekspedisi berikutnya. paket yang masih diproses gudang tidak boleh untuk diambil oleh rolse siapapun. Paket dari gudang hanya boleh diambil oleh kurir yang kemudian diinputkan manual kedalam akun kurir tanpa penugasan eksplisit dari gudang. Jadi ceritanya Gudang itu menyerahkan keranjang paket ke kurir untuk diinputkan dan dikirimkan ke fasilitas berikutnya.

### Penjelasan Role:

- Admin: Memiliki Akses untuk CRUD User
- Loket: Menerima Paket dan Membuat Resi Paket
- Kurir: Menerima Paket dan Mengantarkan Paket Ke Gudang / Fasilitas Lainnya
- Gudang: Menerima Paket dan Memproses Paket.
