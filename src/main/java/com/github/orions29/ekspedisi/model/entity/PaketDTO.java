package com.github.orions29.ekspedisi.model.entity;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.model
 * <p>
 * Deskripsi fungsional dari file ini.
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 06 June 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 20:09</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public record PaketDTO(String resi, String status, String namaPenerima, String tujuan) {
}