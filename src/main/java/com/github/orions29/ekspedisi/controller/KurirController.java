package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.TrackingDAO;
import com.github.orions29.ekspedisi.model.dao.TrackingDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.views.KurirViews;
import com.github.orions29.ekspedisi.views.LoginView;

import java.util.List;
import java.util.Set;

import javax.swing.*;

/**
 * Controller buat mengelola proses pengiriman
 * dan update status paket oleh kang kurir
 *
 * <p>
 * Tugasnya adalah:
 * validasi nomor resi,
 * update status paket saat dibawa kurir,
 * pencatatan paket diterima penerima,
 * serta penyimpanan tracking pengiriman ke database
 * </p>
 *
 * <p>
 * Controller ini menghubungkan KurirViews
 * dengan TrackingDAO untuk proses
 * update riwayat pengiriman paket
 * </p>
 *
 * @author Erlan
 */


// constructor utama kurirController
// nerima data user kurir yang lagi login, inisialisasi trackingDAO, jalanin semua event listener controller
public class KurirController {

    private KurirViews view;
    private User loggedInUser;
    private TrackingDAO trackingDAO;

    private final Set<String> ALLOWED_PICKUP_STATUSES = Set.of(
            "Transit Gudang",
            "Diterima Di Loket"
    );

    public KurirController(
            KurirViews view,
            User loggedInUser
    ) {

        this.view = view;
        this.loggedInUser = loggedInUser;

        this.trackingDAO =
                new TrackingDAOMariaDb();

        initController();
    }


    // method buat konekin semua komponen UI sama logic controller, menggunakan event listener
    private void initController() {

        view.getSubmitPaket()
                .addActionListener(e -> {
                    handleDeliveryUpdate();
                });

        view.getSendToGudangButton().addActionListener(e -> {
            handleTakeToGudang();
        });

        view.getPaketSelesaiButton()
                .addActionListener(e -> {

                    handlePaketSelesai();
                });

        view.getCekPaketButton().addActionListener(e -> {
            handleCekMuatan();
        });

        view.getLogoutButton().addActionListener(e -> {
            handleLogoutEvent();
        });

        view.getBtnCamera().addActionListener(e -> {
            handleCameraScan();
        });
    }

    private void handleLogoutEvent() {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("EMR Tracking System - Login");
            LoginView loginView = new LoginView();

            new LoginController(loginView);

            loginFrame.setContentPane(loginView);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });

        view.dispose();
    }

    private void handleCameraScan() {
        com.github.orions29.ekspedisi.views.CameraScannerDialog dialog =
                new com.github.orions29.ekspedisi.views.CameraScannerDialog(view);
        dialog.setVisible(true);

        String scannedResi = dialog.getScannedResult();
        if (scannedResi != null && !scannedResi.trim().isEmpty()) {
            view.getTxtResi().setText(scannedResi);
        }
    }


    // memproses update status paket ketika paket dibawa kurir
    // Status : "Diserahkan ke kurir"
    // method ini akan membuat log baru buat disimpen di db
    private void handleDeliveryUpdate() {

        String resi = view.getTxtResi().getText().trim();

        if (resi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor resi wajib diisi ngab!");
            return;
        }

//        Cek Status Terakhir
        String currentStatus = trackingDAO.getLatestPaketStatusByResi(resi);

        if (currentStatus == null) {
            JOptionPane.showMessageDialog(null, "Resi fiktif! Data tidak ditemukan di MariaDB.", "Error 404", JOptionPane.ERROR_MESSAGE);
            view.getTxtResi().selectAll();
            return;
        }

//        Pengecekan SOP dengan Set SOP
        if (!ALLOWED_PICKUP_STATUSES.contains(currentStatus)) {
            JOptionPane.showMessageDialog(null,
                    "SOP DILANGGAR! Paket ini masih berstatus: [" + currentStatus + "].\n" +
                            "Kamu HANYA boleh mengambil muatan yang berstatus 'Transit Gudang' atau 'Diterima Di Loket'!",
                    "Akses Kurir Ditolak", JOptionPane.ERROR_MESSAGE);

            view.getTxtResi().selectAll();
            view.getTxtResi().requestFocus();

//            Pokoknya engggak
            return;
        }

        ShipmentLog logBaru = new ShipmentLog(
                resi,
                loggedInUser.getLocation(),
                "Dibawa Kurir",
                loggedInUser.getId()
        );

//        Insertkan
        boolean success = trackingDAO.insertLog(logBaru);

        if (success) {
            JOptionPane.showMessageDialog(null, "Muatan diamankan! Paket masuk ke tas Kurir.\nResi : " + resi);
            view.getTxtResi().setText("");

            handleCekMuatan();
        } else {
            JOptionPane.showMessageDialog(null, "Fatal Error: Gagal update tracking paket ke MariaDB!");
        }
    }

    /**
     *
     * <h3>Handle Take To Gudang</h3>
     * <p> Paket yang sudah sampai di gudang akan diberikan ke state ini yang selanjutnya akan diproses gudang</p>
     *
     * @author Orions29
     * @since 7 Jun 2026
     *
     */
    private void handleTakeToGudang() {

        String resi = view.getTxtResi().getText().trim();

        if (resi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor resi wajib diisi ngab!!");
            return;
        }

        String currentStatus = trackingDAO.getLatestPaketStatusByResi(resi);

        // Kurir cuma bisa memulangkan paket yang ada di dalam tasnya
        if (!"Dibawa Kurir".equals(currentStatus)) {
            JOptionPane.showMessageDialog(null,
                    "SOP DILANGGAR! Kamu tidak bisa menyerahkan paket ini ke Gudang karena statusnya [" + currentStatus + "].\n" +
                            "Kamu HANYA bisa mengembalikan paket yang berstatus 'Dibawa Kurir'!",
                    "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            view.getTxtResi().selectAll();
            return;
        }

        // Lempar statusnya kembali menjadi "Transit Gudang"
        ShipmentLog logBaru = new ShipmentLog(
                resi,
                loggedInUser.getLocation(),
                "Transit Gudang",
                loggedInUser.getId()
        );

        boolean success = trackingDAO.insertLog(logBaru);

        if (success) {
            JOptionPane.showMessageDialog(null, "Serah terima sukses, Muatan resmi diserahkan ke otoritas Gudang.");
            view.getTxtResi().setText("");

            handleCekMuatan();
        } else {
            JOptionPane.showMessageDialog(null, "Fatal Error: MariaDB menolak eksekusi!");
        }
    }


    // method untuk mengubah status paket menjadi diterima oleh penerima
    // status yang dicatat: "diterima oleh penerima"
    // method untuk mengubah status paket menjadi diterima oleh penerima
    private void handlePaketSelesai() {

        String resi = view.getTxtResi().getText().trim();

        if (resi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor resi wajib diisi");
            return;
        }

        String currentStatus = trackingDAO.getLatestPaketStatusByResi(resi);

        // Kurir HARAM hukumnya menyelesaikan paket kalau dia belum men-scan paket itu sebagai "Dibawa Kurir"
        if (!"Dibawa Kurir".equals(currentStatus)) {
            JOptionPane.showMessageDialog(null,
                    "SOP DILANGGAR! Kamu tidak bisa menyelesaikan paket ini karena statusnya masih [" + currentStatus + "].\n" +
                            "Paket harus berstatus 'Dibawa Kurir' terlebih dahulu!",
                    "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            view.getTxtResi().selectAll();
            return;
        }

        ShipmentLog logBaru = new ShipmentLog(
                resi,
                "Paket Telah Diterima",
                "Paket sudah diterima oleh penerima Langsung di Kurir Jarak Jauh",
                loggedInUser.getId()
        );

        boolean success = trackingDAO.insertLog(logBaru);

        if (success) {
            JOptionPane.showMessageDialog(null, "Misi Selesai! Paket berhasil dikirimkan!");
            view.getTxtResi().setText("");

            handleCekMuatan();
        } else {
            JOptionPane.showMessageDialog(null, "Fatal Error: Gagal update status paket ke MariaDB!");
        }
    }

    /**
     *
     * <h3>Cek Muatan Kurir</h3>
     * <p> Daftar Paket yang masih dibawa kurir</p>
     *
     * @author Orions29
     * @since 2 Jun 2026
     *
     */
    private void handleCekMuatan() {
        List<PaketDTO> daftarResi = trackingDAO.getAllPaketByStatus("Dibawa Kurir", loggedInUser.getId());
//        Map<String,String> daftarResi = trackingDAO.getResiByLatestStatusAndUser("Dibawa Kurir", loggedInUser.getId());
        view.getListPaketAreaButton().setText("");

//        Error handling kalau kosong
        if (daftarResi.isEmpty()) {
            view.getListPaketAreaButton().setText("[KOSONG]\n\nTidak ada paket yang sedang kamu bawa saat ini.\nSantai dulu ngab!");
            return;
        }

        StringBuilder daftarPaketTxt = new StringBuilder();
        daftarPaketTxt.append("<= DAFTAR MUATAN =>\n");
        daftarPaketTxt.append("Total Paket: ").append(daftarResi.size()).append(" item\n");
        daftarPaketTxt.append("Format RESI-Kode-Resi - [Kota Tujuan]\n\n");

        int nomor = 1;
        for (PaketDTO paket : daftarResi) {
            daftarPaketTxt.append(nomor).append(". ").append(paket.resi()).append(" - [").append(paket.destinationCity()).append("]\n");
            nomor++;
        }

        view.getListPaketAreaButton().setText(daftarPaketTxt.toString());

//        biar paling atas atau apalah
        view.getListPaketAreaButton().setCaretPosition(0);
    }
}