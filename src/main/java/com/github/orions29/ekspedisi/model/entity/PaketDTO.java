package com.github.orions29.ekspedisi.model.entity;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.model.entity
 * <p>
 * Template DTO untuk Paket.
 * </p>
 *
 * <hr>
 * <b>Author</b>: Orions29
 * <b>Date</b>: 06 June 2026
 * <b>Time</b>: 12:34
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public record PaketDTO(String resi, String status, String senderName, String destinationCity) {
}