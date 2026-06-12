package com.github.orions29.ekspedisi.utils.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils.net
 * <p>
 * Utility Class untuk Semua Kebutuhan Networking
 * </p>
 *
 * <hr>
 * <b>Author</b>: Orions29
 * <b>Date</b>: 12 June 2026
 * <b>Time</b>: 10:49
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class networkUtil {
    private static Logger logger = LoggerFactory.getLogger(networkUtil.class);

    /**
     *
     * <h3>Port Cek</h3>
     * <p>
     * Mengecek Port tertentu apakah port ini ada yang menggunakan
     * </p>
     *
     * @param host - Alamat Host yang mau dicek
     * @param port - Port yang mau dicek
     * @return {@link boolean} - Penjelasan mengenai data yang dikembalikan
     * @author Orions29
     * @since 11 Jun 2026
     *
     */
    public static boolean isPortOcupied(String host, int port) {
        try (Socket socket = new Socket()) {
//            Nunggu 1 detik untuk mencoba koneksi
            socket.connect(new InetSocketAddress(host, port), 1500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     *
     * <h3>Trying To Reach Port</h3>
     * <p>
     * Waiting Function untuk menunggu port sampai tersambung
     * </p>
     *
     * @param host          - Alamat Host yang mau dicek
     * @param port          - Port yang mau dicek
     * @param timeoutMillis - Waktu tunggu maksimal
     * @return {@link boolean} - True kalau dia sudah oke false kalau dia masih bad bad boy
     * @author Orions29
     * @since 11 Jun 2026
     *
     */
    public static boolean waitForPort(String host, int port, int timeoutMillis) {
//        Pengecekan Port
        if (isPortOcupied(host, port)) {
//            Jika Port Ternyata sudah occupied
            logger.error("[ERROR PORT] - Port {} Sedang ada yang pakai", port);

            // Kalau Port Terpakai
            return false;
        } else {
            long waktuMulaiPort = System.currentTimeMillis();

            while (System.currentTimeMillis() - waktuMulaiPort < timeoutMillis) {
                try (Socket socket = new Socket()) {
                    // Coba koneksi dengan timeout 1 detik per percobaan
                    socket.connect(new InetSocketAddress(host, port), 1000);
                    // Jika tidak error, berarti port sudah terbuka dan tunnel jalan
                    return true;
                } catch (IOException e) {
                    // Port belum terbuka, tunggu 1 detik sebelum mencoba lagi
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }

        // Kalau Port Terpakai
        return false;
    }
}
