package com.github.orions29.ekspedisi.utils.business;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Mesin kalkulasi tarif logistik (Tarif Tunggal / Flat-Rate).
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 1 June 2026</td></tr>
 * </table>
 * <hr>
 */
public class PricingUtil {

    private static final double PEMBAGI_VOLUME = 6000.0;
    private static final double HARGA_PER_KG = 10000.0;

    /**
     *
     * <h3>Hitung Charge Berat Paket</h3>
     * <p>
     * Volume akan dibagi 6000 untuk mendapatkan berat volume per kilo, Ini secara imajiner ya.
     * Berat volume imajiner kemudian akan dibandingkan dengan berat asli paket.
     * Jika berat asli lebih besar maka akan diambil, jika tidak maka akan diambil berat volume.
     * <p>
     * ini logika bisnis dari internet entah asal dari mana.
     * </p>
     *
     * @param actualWeight - Berat Asli Paket
     * @param volumeCm3    - Volume Paket dalam cm3 / centimeter kubik
     * @return {@link double} - Hasil Perhitungan Charge
     * @author Orions29
     * @since 1 Jun 2026
     *
     */
    public static double hitungChargeBerat(double actualWeight, double volumeCm3) {
//        Volume dijadikan kilogram imajiner / estimasi
        double beratVolume = volumeCm3 / PEMBAGI_VOLUME;
//        Dibandingin sama kilogram aseli
        double chargeByWeight = Math.max(actualWeight, beratVolume);
// gweh orangnya kapitalis HAHAHAH BULATKAN KE TERBESARRRR HAHA DASAR MISKINNNNNNNNNNNNNN
        return Math.ceil(chargeByWeight);
    }

    /**
     *
     * <h3> Hitung Harga Akhir</h3>
     * <p>
     * Perhitungan harga asli. Dipisah biar bisa leluasa ganti logic bisnis hitung chargenya
     * </p>
     *
     * @param actualWeight - Berat Aseli paket
     * @param volumeCm3    - Dimensi Paket
     * @return {@link double} - Hasil Perhitungan Akhir (non Rupiah)
     * @author Orions29
     * @since 1 Jun 2026
     *
     */
    public static double hitungHarga(double actualWeight, double volumeCm3) {
        double chargeByWeight = hitungChargeBerat(actualWeight, volumeCm3);

        return chargeByWeight * HARGA_PER_KG;
    }

    /**
     *
     * <h3>Format Rupiah</h3>
     * <p>
     * Ngeformat Biar Rupiah ada
     * </p>
     *
     * note: Gweh gatau cara kerjanya gimana penting bisa.
     *
     * @param nominal Deskripsi fungsi parameter ini
     * @return {@link String}  Mengembalikan format rupiah tanpa Ex: '20.000,00'
     * @author Orions29
     * @since 1 Jun 2026
     *
     */
    public static String formatRupiah(double nominal) {
        Locale localeID = new Locale("id", "ID");
        NumberFormat formatter = NumberFormat.getNumberInstance(localeID);

        return formatter.format(nominal);
    }
}