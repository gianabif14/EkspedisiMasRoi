package com.github.orions29.ekspedisi.utils;

import com.github.orions29.ekspedisi.model.DatabaseConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Berfungsi melakukan checklist project ini
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 29 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 21:52</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class ProjectInit {
    private static Logger logger = LoggerFactory.getLogger(ProjectInit.class);

    /**
     *
     * <h3>Apakah Port Bisa</h3>
     * <p> </p>
     *
     * @param host - Alamat Host yang mau dicek
     * @param port - Port yang mau dicek
     * @return {@link boolean} - Penjelasan mengenai data yang dikembalikan
     * @author Orions29
     * @since 11 Jun 2026
     *
     */
    private static boolean isPortOcupied(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     *
     * <h3>Pengecekan Port</h3>
     * <p> Pengecekan Port yang dibutuhkan</p>
     *
     * @param host          - Alamat Host yang mau dicek
     * @param port          - Port yang mau dicek
     * @param timeoutMillis - Waktu tunggu maksimal
     * @return {@link boolean} - True kalau dia sudah oke false kalau dia masih bad bad boy
     * @author Orions29
     * @since 11 Jun 2026
     *
     */
    private static boolean waitForPort(String host, int port, int timeoutMillis) {
//        Pengecekan Port
        if (isPortOcupied(host, port)) {
//            Jika Port Ternyata sudah occupied
            logger.error("[ERROR PORT] - Port {} Sedang ada yang pakai", port);
        } else {
            long waktuMulaiPort = System.currentTimeMillis();

            while (System.currentTimeMillis() - waktuMulaiPort < timeoutMillis) {
                try (Socket socket = new Socket()) {
                    // Coba koneksi dengan timeout 1 detik per percobaan
                    socket.connect(new InetSocketAddress(host, port), 1000);
                    return true; // Jika tidak error, berarti port sudah terbuka dan tunnel jalan
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
        // Kalau Timeout tercapai
        return false;
    }

    /**
     *
     * <h3>Native Loader</h3>
     * <p> Menginputkan Apapun itu yang diperlukan untuk aplikasi ini</p>
     *
     * @throws IOException jika kondisi IOException terjadi
     * @author Orions29
     * @since 10 Jun 2026
     *
     */
    public static void nativeLoader() throws IOException {
//        Ngambil nama Sistem Operasi Pengguna
        String os = System.getProperty("os.name").toLowerCase();

//        WIndows
        if (os.contains("win")) {
            // Menjalankan Cloudflared ke STB Gian
            String cf = new File("native/windows/cloudflared.exe").getAbsolutePath();
            ProcessBuilder pb = new ProcessBuilder(
                    cf,
                    "access",
                    "tcp",
                    "--hostname", "db.gaf.my.id",
                    "--url", "localhost:3306"
            );

            pb.inheritIO();

            Process prc = pb.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
//                    Kalau Process itu tidak null dan
                    if (prc != null && prc.isAlive()) {
                        prc.destroy();
                        logger.info("CMD connection closed safely via JVM Shutdown Hook");
                    }
                } catch (Exception e) {
                    logger.error("[ERROR CMD] - Error closing CMD connection during shutdown", e);
                }
            }));

            System.out.println("Menunggu Tunnel Cloudflare Siap...");
//            Cek tiap 1 detik
            boolean isPortOpen = waitForPort("localhost", 3306, 10000);

            if (isPortOpen) {
                System.out.println("Tunnel Cloudflare Siap!!");
                logger.info("Tunnel Cloudflare Ready");
            } else {
                logger.error("[ERROR TUNNEL] - Tunnel Cloudflare Gagal");
                System.exit(1);
            }

        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            System.out.println("Linux");
            System.out.println("MAAF UNTUK LINUX BELUM DIDUKUNG");
            System.exit(0);
        } else if (os.contains("mac")) {
            System.out.println("MAAF UNTUK Mac OS BELUM DIDUKUNG");
            System.out.println("Mac");
        }
    }

    /**
     *
     * <h3>Project Initiation Checklist</h3>
     * <p> Ngechecklist yang perlu di check </p>
     *
     * @author Orions29
     * @since 29 May 2026
     *
     */
    public static void projectCheck() {
        logger.info("Project Check Init Start");
        System.out.println(">> ProjectInit CheckList: ");


//        Checklist ENV
        boolean isEnvLoaded = SecretLoader.isLoad();
        System.out.println(isEnvLoaded ? "ENV : PASS" : "ENV : FAILED");

//        Checklist Database
        boolean isDbConnected = false;
        try {
            Connection testConn = DatabaseConfig.getConnection();
            isDbConnected = DatabaseConfig.isConnected();
        } catch (Exception e) {
            isDbConnected = false;
        }
        System.out.println(isDbConnected ? "DB CONN  : PASS" : "DB CONN  : FAILED");

        logger.info("Project Check Done");

//        Kalau ada yang gagal gaboleh Jalan Titik.
        if (!isEnvLoaded || !isDbConnected) {
            System.err.println("[FATAL ERROR] - Project init Checklist Failed. Program Stop.");
            logger.error("[FATAL ERROR] - Project init Checklist Failed");
            System.exit(1);
        }
    }

    /**
     *
     * <h3>Buat Ngetest DAO</h3>
     * <p> </p>
     *
     * @author Orions29
     * @since 31 May 2026
     *
     */
    public static void DAOTest() {
        DAOtester.main();
    }
}
