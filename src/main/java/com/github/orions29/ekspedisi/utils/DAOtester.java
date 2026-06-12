package com.github.orions29.ekspedisi.utils;

import com.github.orions29.ekspedisi.model.dao.*;
import com.github.orions29.ekspedisi.model.entity.*;
import com.github.orions29.ekspedisi.utils.business.GeneratorId;

import java.util.List;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Class buat ngetest apakah sambungan ke database ini ok ok aja bisa jalan
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 12:06</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.1
 */
public class DAOtester {
    public static void main() {
        System.out.println("\n==>> TEST DAO <<==\n");

        UserDAO userDao = new UserDAOMariaDb();
        PaketDAO paketDao = new PaketDAOMariaDb();
        TrackingDAO trackingDao = new TrackingDAOMariaDb();

//        Testing UserDAO
        System.out.println(">> TEST 1: USER DAO <<");

//        Akses DAO User
//        Narik Wong Gudang
        System.out.println("Tarik User (G-2001) : " + userDao.getUserById("G-2001"));

//        Auth User
        System.out.println("Auth Gagal          : " + userDao.authenticate("loket_rani", "salah_password"));
        System.out.println("Auth Sukses         : " + userDao.authenticate("kurir_anto", "bee5688aea66a47460b19c76f8f199c6b9585eb726f8322b1429793863609ca3"));

//        Insert & Update User
        String dummyUserId = GeneratorId.generateUserId("kurir");
        User userBaru = new User(dummyUserId, "kurir_tester", "dummyhash123", "kurir", "Mobile");
        boolean isUserInserted = userDao.insertUser(userBaru);
        System.out.println("Insert User Baru    : " + isUserInserted);

        if (isUserInserted) {
            userBaru.setLocation("Gudang Pusat Jakarta");
            userBaru.setRole("gudang");
            System.out.println("Update User         : " + userDao.updateUser(userBaru));
            System.out.println("Cek Hasil Update    : " + userDao.getUserById(dummyUserId));
        }
        System.out.println(">>\n");

//        Testing PaketDAO
        System.out.println(">> TEST 2: PAKET DAO <<");
        String dummyResi = GeneratorId.generateResi();
        Paket paketBaru = new Paket(dummyResi, "Bapak A", "Yogyakarta", "Ibu B", "Jakarta", "Jl. Sudirman 1", 2.5, 1000.0, "Reguler");

//        Akses DAO Paket
        boolean isPaketInserted = paketDao.insertPaket(paketBaru);
        System.out.println("Insert Resi Baru    : " + isPaketInserted);
        System.out.println("Tarik Data Paket    : " + paketDao.getPaketByResi(dummyResi));
        System.out.println(">>\n");

//        Testing TrackingDAO
        System.out.println(">> TEST 3: TRACKING DAO <<");
        if (isPaketInserted) {
            trackingDao.insertLog(new ShipmentLog(dummyResi, "Loket Pusat Godean", "Diterima di Loket", "L-1001"));
            trackingDao.insertLog(new ShipmentLog(dummyResi, "Gudang Sortir Godean", "Sedang Disortir", "G-2001"));

//        Akses DAO Tracking
            List<ShipmentLog> riwayat = trackingDao.getShipmentLogByResi(dummyResi);
            System.out.println("Total Riwayat Ditemukan : " + riwayat.size());

            for (ShipmentLog jejak : riwayat) {
                System.out.println(" -> [" + jejak.getTimestamp() + "] " + jejak.getStatus() + " @ " + jejak.getLocation());
            }
        } else {
            System.out.println("X FASE 3 DILEWATI: Gagal insert paket ke database.");
        }
        System.out.println(">>\n");

        System.out.println(">> Test DAO Selesai");
    }
}