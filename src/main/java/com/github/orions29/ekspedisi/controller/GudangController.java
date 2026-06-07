package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.TrackingDAO;
import com.github.orions29.ekspedisi.model.dao.TrackingDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.views.CameraScannerDialog;
import com.github.orions29.ekspedisi.views.GudangViews;
import com.github.orions29.ekspedisi.views.LoginView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.*;

/**
 * Controller buat ngelola proses scanning
 * dan update status paket di fasilitas gudang
 *
 * <p>
 * Tugasnya:
 * validasi nomor resi paket,
 * ngambilan status logistik dari ComboBox,
 * pencatatan tracking paket ke database,
 * nampilin aktivitas gudang secara realtime
 * </p>
 *
 * <p>
 * Controller ini connect in GudangViews
 * dengan TrackingDAO sebagai jembatan
 * antara antarmuka user dan database tracking
 * </p>
 *
 * @author Erlan
 */
public class GudangController {

    private GudangViews view;
    private TrackingDAO trackingDAO;
    private User loggedInUser;

    private final Map<String, Integer> STATE_HIERARCHY = Map.of(
            "Tiba di Fasilitas Sortir", 1,
            "Sedang Disortir", 2,
            "Transit Gudang", 3
//            "Diserahkan ke Kurir", 4
    );

    public GudangController(
            GudangViews view,
            User loggedInUser
    ) {

        this.view = view;
        this.loggedInUser = loggedInUser;

        this.trackingDAO =
                new TrackingDAOMariaDb();

        initController();
    }


    // ngehubungin seluruh komponen UI
    // event: tombol update status, input enter pada field resi, realtime scanning gudang
    private void initController() {

        view.getBtnUpdate()
                .addActionListener(e -> {

                    handleMassTransitUpdate();
                });

        view.getBtnLogout().addActionListener(e -> {
            handleLogoutEvent();
        });

        view.getBtnCekPaketDiGudang().addActionListener(e -> {
            handleCekPaket();
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
        CameraScannerDialog dialog =
                new com.github.orions29.ekspedisi.views.CameraScannerDialog(view);
        dialog.setVisible(true);

        String scannedResi = dialog.getScannedResult();
        if (scannedResi != null && !scannedResi.trim().isEmpty()) {
            view.getTxtResi().setText(scannedResi);
        }
    }

    public void handleMassTransitUpdate() {

        String resiId = view.getTxtResi().getText().trim();
        String selectedStatus = view.getComboStatus().getSelectedItem().toString();

        if (resiId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Scan barcode atau masukkan nomor resi dulu ngab!");
            view.getTxtResi().requestFocus(); // Kembalikan kursor ke input
            return;
        }

        // Siapkan stempel waktu di awal biar bisa dipakai di semua skenario
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

        //        State Verivied
        String currentStatus = trackingDAO.getLatestPaketStatusByResi(resiId);

        if (currentStatus != null) {
            Integer currentLevel = STATE_HIERARCHY.get(currentStatus);
            Integer newLevel = STATE_HIERARCHY.get(selectedStatus);

            // Validasi, Apakah status yang dipilih levelnya lebih rendah atau jalan di tempat?
            if (currentLevel != null && newLevel != null && newLevel <= currentLevel) {

                JOptionPane.showMessageDialog(null,
                        "Forbidden: Paket sedang berada di state [" + currentStatus + "].\n" +
                                "Tidak bisa diturunkan/diulang ke state [" + selectedStatus + "].",
                        "State Machine Error", JOptionPane.ERROR_MESSAGE);

                // Cetak log penolakan ke layar matrix
                String rejectLog = String.format("[%s] REJECTED: Resi %s => Dilarang mundur ke [%s]!\n",
                        timestamp, resiId, selectedStatus);

                view.getTxtConsole().append(rejectLog);
                view.getTxtConsole().setCaretPosition(view.getTxtConsole().getDocument().getLength());

                view.getTxtResi().selectAll(); // Highlight teks biar tinggal timpa pakai scanner
                view.getTxtResi().requestFocus();
                return;
            }
        }

        ShipmentLog logBaru = new ShipmentLog(
                resiId,
                loggedInUser.getLocation(),
                selectedStatus,
                loggedInUser.getId()
        );

        // Rodokkan ke DB
        boolean isSakses = trackingDAO.insertLog(logBaru);
        String logMessage;

        // Error Handling Sukses or Failed
        if (isSakses) {
            logMessage = String.format("[%s] SUCCESS: Resi %s => [%s] @ %s\n",
                    timestamp, resiId, selectedStatus, loggedInUser.getLocation());

            if (currentStatus != null) {
                handleCekPaket();
            }

        } else {
            logMessage = String.format("[%s] FAILED : Resi %s => Gagal ditambahkan ke MariaDB!\n",
                    timestamp, resiId);
            JOptionPane.showMessageDialog(null, "Error Db: Gagal memperbarui status paket ke database.\nCek Resi Valid atau Tidak.\nJika masih error Kasih Tau Yang Maha Admin!", "Error Database", JOptionPane.ERROR_MESSAGE);
        }

        // Cetak hasil akhir ke console
        view.getTxtConsole().append(logMessage);
        view.getTxtConsole().setCaretPosition(view.getTxtConsole().getDocument().getLength());

        // Ibarat kata CLS (Bersih-bersih untuk scan berikutnya)
        view.getTxtResi().setText("");
        view.getTxtResi().requestFocus();
    }

    public void handleCekPaket() {
//        List status yang akan tertampil
        List<String> statusGudang = Arrays.asList(
                "Tiba di Fasilitas Sortir",
                "Sedang Disortir",
                "Transit Gudang"
        );

//        Ngambil daftar resi yang statusnya ada di list statusGudang di or kan
        List<PaketDTO> daftarResi = trackingDAO.getResiByMultipleLatestStatuses(statusGudang, loggedInUser.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("<== DAFTAR PAKET ==>\n");
        sb.append("Total Paket Menunggu Eksekusi: ").append(daftarResi.size()).append(" item\n\n");

//        Error handling kalau kosong
        if (daftarResi.isEmpty()) {
            sb.append("[KOSONG]\nKerjaan beres Ngab");
        } else {
            int nomor = 1;
            for (PaketDTO resi : daftarResi) {
                sb.append(nomor)
                        .append(". ")
                        .append(resi.resi())
                        .append(" - ")
                        .append(resi.status())
                        .append(" - [").append(resi.destinationCity()).append("]\n");
                nomor++;
            }
        }

        // Nembak ke kanvas dan paksa scrollbar ke posisi paling atas
        view.getTxtConsole().setText(sb.toString());
        view.getTxtConsole().setCaretPosition(0);
    }
}