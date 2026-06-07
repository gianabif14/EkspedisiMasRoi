package com.github.orions29.ekspedisi.model.dao;

import com.github.orions29.ekspedisi.model.DatabaseConfig;
import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.model.dao
 * <p>
 * Implementasi bare-metal DAO untuk riwayat Tracking.
 * Menghajar port 3306 untuk mencatat setiap detak pergerakan paket.
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class TrackingDAOMariaDb implements TrackingDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrackingDAOMariaDb.class);

    @Override
    public boolean insertLog(ShipmentLog log) {
        // Timestamp diurus langsung oleh DEFAULT CURRENT_TIMESTAMP di MariaDB
        String querySql = "INSERT INTO tracking_logs (resi_id, user_id, status, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySql)) {

            stmt.setString(1, log.getResiId());
            stmt.setString(2, log.getUserId());
            stmt.setString(3, log.getStatus());
            stmt.setString(4, log.getLocation());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("[TRACKING] Jejak baru dicatat! Resi [{}] -> Status: {} di {}",
                        log.getResiId(), log.getStatus(), log.getLocation());
                return true;
            }

        } catch (SQLException e) {

            // Error handling spesifik untuk Foreign Key constraint (jika resi_id tidak ada di tabel paket)
            if (e.getErrorCode() == 1452) {
                logger.error("[INSERT FAILED] - Resi [{}] fiktif! Tidak ditemukan di tabel induk 'paket'.", log.getResiId());
            } else {
                logger.error("[QUERY ERROR] - Gagal mencatat log resi [{}]: {}", log.getResiId(), e.getMessage());
            }
        }
        return false;
    }

    @Override
    public List<ShipmentLog> getShipmentLogByResi(String resiId) {
        String querySql = "SELECT log_id, resi_id, user_id, status, location, timestamp FROM tracking_logs WHERE resi_id = ? ORDER BY timestamp ASC";

        List<ShipmentLog> logHistory = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySql)) {

            stmt.setString(1, resiId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ShipmentLog log = new ShipmentLog(
                            rs.getInt("log_id"),
                            rs.getString("resi_id"),
                            rs.getString("location"),
                            rs.getString("status"),
                            rs.getTimestamp("timestamp").toLocalDateTime(),
                            rs.getString("user_id")
                    );
                    logHistory.add(log);
                }
                logger.info("[TRACKING] Ditemukan {} jejak riwayat untuk Resi [{}]", logHistory.size(), resiId);
            }
        } catch (SQLException e) {
            logger.error("[QUERY ERROR] - Gagal menarik riwayat tracking resi [{}]: {}", resiId, e.getMessage());
        }

        return logHistory;
    }

    @Override
    public List<PaketDTO> getResiByMultipleLatestStatuses(List<String> targetStatuses, String userId) {
        List<PaketDTO> daftarResi = new ArrayList<>();


        if (targetStatuses == null || targetStatuses.isEmpty()) {
            return daftarResi;
        }

        String placeholders = String.join(",", Collections.nCopies(targetStatuses.size(), "?"));

        String sql = "SELECT t1.resi_id, t1.status, p.receiver_name, p.destination_city " +
                "FROM tracking_logs t1 " +
                "INNER JOIN (" +
                "    SELECT resi_id, MAX(log_id) as max_id " +
                "    FROM tracking_logs " +
                "    GROUP BY resi_id" +
                ") t2 ON t1.log_id = t2.max_id " +
                "INNER JOIN paket p ON t1.resi_id = p.resi_id " +
                "WHERE t1.status IN (" + placeholders + ") AND t1.user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (String status : targetStatuses) {
                pstmt.setString(index++, status);
            }

            pstmt.setString(index, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaketDTO paket = new PaketDTO(
                            rs.getString("resi_id"),
                            rs.getString("status"),
                            rs.getString("receiver_name"),
                            rs.getString("destination_city")
                    );
                    daftarResi.add(paket);
                }
            }

        } catch (SQLException e) {
            System.err.println("[QUERY ERROR] Fatal: Gagal Menarik Data Resi All (Multiple Status)");
            logger.error("[QUERY ERROR] - Error: " + e.getMessage());
        }

        return daftarResi;
    }

    @Override
    public List<PaketDTO> getAllPaketByStatus(String targetStatus, String userId) {
        List<PaketDTO> daftarPaket = new ArrayList<>();

        String sql = "SELECT t1.resi_id, t1.status, p.receiver_name, p.destination_city " +
                "FROM tracking_logs t1 " +
                "INNER JOIN (" +
                "    SELECT resi_id, MAX(log_id) as max_id " +
                "    FROM tracking_logs GROUP BY resi_id" +
                ") t2 ON t1.log_id = t2.max_id " +
                "INNER JOIN paket p ON t1.resi_id = p.resi_id " +
                "WHERE t1.status = ? AND t1.user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, targetStatus);
            pstmt.setString(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaketDTO paket = new PaketDTO(
                            rs.getString("resi_id"),
                            rs.getString("status"),
                            rs.getString("receiver_name"),
                            rs.getString("destination_city")
                    );
                    daftarPaket.add(paket);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("[QUERRY ERROR] Error: Gagal Menarik Data Resi All");
            logger.error("[QUERY ERROR] - Gagal menarik data paket: {}", e.getMessage());
        }
        return daftarPaket;
    }

    @Override
    public String getLatestPaketStatusByResi(String resiId) {
        String querySql = "SELECT status FROM tracking_logs WHERE resi_id = ? ORDER BY timestamp DESC LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            pstmt.setString(1, resiId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("status");
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("[QUERRY ERROR] Error: Gagal Menarik Status Paket");
            logger.error("[QUERY ERROR] - Gagal menarik Status paket: {}", e.getMessage());
        }
        return null;
    }
}