package com.github.orions29.ekspedisi.utils;

import static com.github.orions29.ekspedisi.utils.net.networkUtil.waitForPort;

import com.github.orions29.ekspedisi.model.DatabaseConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.text.View;

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
     * <h3>Native Loader</h3>
     * <p>
     * Menginputkan Semua yang diperlukan untuk aplikasi ini
     * </p>
     *
     * @return
     * @throws IOException jika kondisi IOException terjadi
     * @author Orions29
     * @since 10 Jun 2026
     *
     */
    private static boolean nativeLoader() throws IOException {
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

            Process prc = pb.start();

//            Graceful Shutdown by Event
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

            System.out.println("Menunggu Tunnel Database Siap...");

//          Cek tiap 1 detik dan cek port
            if (waitForPort("localhost", 3306, 10000)) {
                logger.info("Tunnel Cloudflare Ready");
                return true;
            } else {
                logger.error("[ERROR TUNNEL] - Tunnel Cloudflare Gagal");
                return false;
            }

        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            System.out.println("Linux");
            System.out.println("MAAF UNTUK LINUX BELUM DIDUKUNG");
            System.exit(0);
        } else if (os.contains("mac")) {
            System.out.println("MAAF UNTUK Mac OS BELUM DIDUKUNG");
            System.out.println("Mac");
        }

//        Harusnya tidak terjadi terjadi jika osnya jelas
        return false;
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
    private static void projectCheck() {
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
            System.err.println("[FATAL ERROR] - Project Checklist Failed. Program Stop.");
            logger.error("[FATAL ERROR] - Project Checklist Failed");
            JOptionPane.showMessageDialog(null, "Project Checklist Failed. Program Stop.");
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

    public static void main(String[] args) {
        try {
//            Load Native Loader
            if (nativeLoader()) {
                projectCheck();
            } else {
//                Jika gagal menjalankan nativeLoader Keluar
                logger.error("[ERROR PROJECT INIT] - Native Loader Failed");
                JOptionPane.showMessageDialog(null, "Project Initation Failed. Program Stop (Check For Log) \n Calls For Admin!!", "Error Fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (IOException e) {
            logger.error("[ERROR PROJECT INIT] - Native Loader Failed: {}", e.getMessage());
        }

    }
}
