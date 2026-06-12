package com.github.orions29.ekspedisi.utils.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Class untuk pembuatan ID baik itu resi ataupun userId
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 11:51</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class GeneratorId {
    /**
     *
     * <h3>Generate Resi ID</h3>
     * <p> Membuat Resi </p>
     *
     * @return {@link String} - hasil Resi ID
     * @author Orions29
     * @since 30 May 2026
     *
     */
    public static String generateResi() {
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        String waktuSlice = nowTime.format(dateFormatter);

        // Ambil 4 karakter acak dari UUID untuk mencegah bentrok di detik yang sama
        String uuidSlice = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

//        Dikembalikan dengan utuh
        return "RESI-" + waktuSlice + "-" + uuidSlice;
    }

    /**
     *
     * <h3>Pembuat user ID</h3>
     * <p>
     * Mencetak ID Pekerja berdasarkan Role.<br>
     * Format: [InisialRole]-[Random 4 Karakter]<br>
     * Contoh: K-XXXX (Kurir), L-1234 (Loket), G-abcd (Gudang)
     * </p>
     *
     * @param role Role user
     * @return {@link String} - UserId yang sudah dibuat
     * @author Orions29
     * @since 30 May 2026
     *
     */
    public static String generateUserId(String role) {
//        Semisal dia Null rolenya langsung dikasi U INI SAFETY NET YA HARUSNYA GAADA NANTINYA TITIK.
        String kodeRole = "U";
//        kalau Role ada maka berikan kode role
        if (role != null) {
            switch (role.toLowerCase()) {
                case "kurir":
                    kodeRole = "K";
                    break;
                case "loket":
                    kodeRole = "L";
                    break;
                case "gudang":
                    kodeRole = "G";
                    break;
                case "admin":
                    kodeRole = "A";
                    break;
            }
        }

//        Buat 4 buat string untuk akhiran UserID (nek perhitungan mas gemini bener ini 1jt kombinasi)
        String akhiranUserId = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return kodeRole + "-" + akhiranUserId;
    }
}
