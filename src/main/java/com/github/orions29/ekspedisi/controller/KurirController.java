package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.TrackingDAO;
import com.github.orions29.ekspedisi.model.dao.TrackingDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.views.KurirViews;
import com.github.orions29.ekspedisi.views.LoginView;

import java.util.List;

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

        String resi =
                view.getTxtResi()
                        .getText()
                        .trim();

        if (resi.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Nomor resi wajib diisi"
            );

            return;
        }

        ShipmentLog logBaru =
                new ShipmentLog(
                        resi,
                        loggedInUser.getLocation(),
                        "Dibawa Kurir",
                        loggedInUser.getId()
                );

        boolean success =
                trackingDAO.insertLog(logBaru);

        if (success) {

            JOptionPane.showMessageDialog(
                    null,
                    "Status pengiriman berhasil diperbarui!\n\n"
                            + "Resi : "
                            + resi
            );

            view.getTxtResi()
                    .setText("");

        } else { // error handling gagal update tracking

            JOptionPane.showMessageDialog(
                    null,
                    "Gagal update tracking paket!"
            );
        }
    }


    // method untuk mengubah status paket menjadi diterima oleh penerima
    // status yang dicatat: "diterima oleh penerima"
    private void handlePaketSelesai() {

        // ngambil resi dari input field
        String resi =
                view.getTxtResi()
                        .getText()
                        .trim();

        // validasi agar resi tidak kosong
        if (resi.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Nomor resi wajib diisi"
            );

            return;
        }

        // Buat tracking log baru
        // dengan status paket diterima oleh penerima
        ShipmentLog logBaru =
                new ShipmentLog(
                        resi,
                        "Paket Telah Diterima",
                        "Paket sudah diterima oleh penerima",
                        loggedInUser.getId()
                );

        // menyimpan tracking pengiriman ke db
        boolean success =
                trackingDAO.insertLog(logBaru);

        // success handler
        if (success) {

            JOptionPane.showMessageDialog(
                    null,
                    "Paket berhasil diselesaikan!"
            );

            view.getTxtResi()
                    .setText("");

        } else {
            // failed handlerw
            JOptionPane.showMessageDialog(
                    null,
                    "Gagal update status paket!"
            );
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