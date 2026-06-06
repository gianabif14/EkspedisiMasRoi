package com.github.orions29.ekspedisi.model.dao;

import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;

import java.util.List;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.model.dao
 * <p>
 * Interface DAO untuk Tracking
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 10:29</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public interface TrackingDAO {

    /**
     *
     * <h3>Nambah log pada paket</h3>
     * <p> </p>
     *
     * @param log $END$ - Deskripsi fungsi parameter ini
     * @return {@link boolean} - Penjelasan mengenai data yang dikembalikan
     * @author Orions29
     * @since 30 May 2026
     *
     */
    boolean insertLog(ShipmentLog log);

    /**
     *
     * <h3>Get Shipment Log by Resi</h3>
     * <p> </p>
     *
     * @param resiId - Resi ID Paket
     * @return {@link List<ShipmentLog>} - Seluruh List Shipment Log
     * @author Orions29
     * @since 30 May 2026
     *
     */
    List<ShipmentLog> getShipmentLogByResi(String resiId);

    /**
     *
     * <h3>Mengambil Paket yang Masih Dipegang oleh User ID, (Multiple Filter)</h3>
     * <p> </p>
     *
     * @param targetStatuses $END$ - Deskripsi fungsi parameter ini
     * @param userId         $END$ - Deskripsi fungsi parameter ini
     * @return {@link List<String>} - Penjelasan mengenai data yang dikembalikan
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    List<PaketDTO> getResiByMultipleLatestStatuses(List<String> targetStatuses, String userId);

    /**
     *
     * <h3>Mengambil Paket yang Masih Dipegang oleh User ID, (Single Filter)</h3>
     * <p> </p>
     *
     * @param targetStatus - Target Status yang ingin dicari
     * @param userId       - paket yang masih dipegang oleh user siapa
     * @return {@link List<PaketDTO>} - Dikembalikan dalam bentuk List Paket DTO
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    List<PaketDTO> getAllPaketByStatus(String targetStatus, String userId);
}
