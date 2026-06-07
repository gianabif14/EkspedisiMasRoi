package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.PaketDAO;
import com.github.orions29.ekspedisi.model.dao.PaketDAOMariaDb;
import com.github.orions29.ekspedisi.model.dao.TrackingDAO;
import com.github.orions29.ekspedisi.model.dao.TrackingDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.Paket;
import com.github.orions29.ekspedisi.model.entity.PaketDTO;
import com.github.orions29.ekspedisi.model.entity.ShipmentLog;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.utils.GeneratorId;
import com.github.orions29.ekspedisi.utils.PricingUtil;
import com.github.orions29.ekspedisi.views.LoketViews;

import java.util.List;

import javax.swing.*;


/**
 * Controller buat ngelola proses input paket dari petugas loket ekspedisi
 *
 * <p>
 * Bertugas menangani:
 * validasi input data paket,
 * generate nomor resi otomatis,
 * penyimpanan data paket ke db,
 * pencatatan tracking awal paket,
 * reset form setelah data berhasil disimpan
 * </p>
 *
 * <p>
 * Controller ini jadi  penghubung utama
 * antara OutletViews dengan layer DAO
 * (PaketDAO dan TrackingDAO)
 * </p>
 *
 * @author Erlan
 *
 */

public class LoketController {

    private LoketViews view;

    private User loggedInUser;

    private PaketDAO paketDAO;
    private TrackingDAO trackingDAO;

    public LoketController(
            LoketViews view,
            User loggedInUser
    ) {

        this.view = view;
        this.loggedInUser = loggedInUser;

        this.paketDAO =
                new PaketDAOMariaDb();

        this.trackingDAO =
                new TrackingDAOMariaDb();

        initController();
    }

    private void initController() { // inisiasi seluruh event listener

        view.getCheckPaketButton().addActionListener(e -> {
            handleCheckPaket();
        });

        view.getInputPaketButton()
                .addActionListener(e -> {

                    handleInputPaket();
                });

        view.getBeratInput()
                .addChangeListener(e -> {

                    updateEstimasiHarga();
                });

        view.getVolumeInput()
                .addChangeListener(e -> {

                    updateEstimasiHarga();
                });

        view.getLogoutButton().addActionListener(e -> {
            handleLogoutEvent();
        });

    }

    private void handleLogoutEvent() {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("EMR Tracking System - Login");
            com.github.orions29.ekspedisi.views.LoginView loginView = new com.github.orions29.ekspedisi.views.LoginView();


            new com.github.orions29.ekspedisi.controller.LoginController(loginView);

            loginFrame.setContentPane(loginView);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });

        view.dispose();
    }

    private void handleInputPaket() {
        // ngambil seluruh inputan identitas pengirim dan penerima dari form pengiriman
        String senderName =
                view.getNamaPengirimInput()
                        .getText()
                        .trim();

        String originCity =
                loggedInUser.getLocation();

        String receiverName =
                view.getNamaPenerimaInput()
                        .getText()
                        .trim();

        String destinationCity =
                view.getDestinasiPaketInput()
                        .getText()
                        .trim();

        String destinationAddress =
                view.getAlamatTujuanInput()
                        .getText()
                        .trim();

        String typePaket =
                view.getTipePaketInput()
                        .getText()
                        .trim();

        // ngambil data berat dan volume paket dari JSpinner
        double weight =
                (Double) view.getBeratInput()
                        .getValue();

        double volume =
                (Double) view.getVolumeInput()
                        .getValue();

        if (senderName.isEmpty() // error handling inputan kosong
                || receiverName.isEmpty()
                || destinationCity.isEmpty()
                || destinationAddress.isEmpty()
                || typePaket.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Semua data wajib diisi!"
            );

            return;
        }

        if (weight <= 0 || volume <= 0) { // error handling jika berat dan volume tidak diisi

            JOptionPane.showMessageDialog(
                    null,
                    "Berat dan Volume harus lebih dari 0!"
            );

            return;
        }

        try {

            // generate resi otomatis
            String resiId =
                    GeneratorId.generateResi();

            // buat object paket baru
            Paket paket =
                    new Paket(
                            resiId,
                            senderName,
                            originCity,
                            receiverName,
                            destinationCity,
                            destinationAddress,
                            weight,
                            volume,
                            typePaket
                    );

            // insert paket ke database
            boolean paketInserted =
                    paketDAO.insertPaket(paket);

            if (!paketInserted) {

                JOptionPane.showMessageDialog(
                        null,
                        "Gagal menyimpan paket!"
                );

                return;
            }
            // buat log tracking awal dengan status "diterima di loket"
            ShipmentLog log =
                    new ShipmentLog(
                            resiId,
                            loggedInUser.getLocation(),
                            "Diterima di Loket",
                            loggedInUser.getId()
                    );

            // simpen log tracking ke db
            trackingDAO.insertLog(log);

            // Generate QR Code dari resi
            com.github.orions29.ekspedisi.utils.QrGeneratorUtil.generateQrCode(resiId);

            String pesanSukses = String.format("Data Paket berhasil disimpan ke database MariaDB!\n\n"
                            + "Nomor Resi: %s\n"
                            + "Pengirim: %s (%s)\n"
                            + "Penerima: %s (%s)\n"
                            + "Berat: %.2f kg\n"
                            + "Volume: %.2f cm3\n"
                            + "Tipe: %s\n"
                            + "Lokasi Input: %s\n\n"
                            + "QR Code disimpan di: /database/qr_result",
                    resiId, senderName, originCity, receiverName, destinationCity, weight, volume, typePaket, loggedInUser.getLocation());

            JOptionPane.showMessageDialog(
                    null,
                    pesanSukses
            );

            // reset form pengisian
            view.getNamaPengirimInput().setText("");
            view.getNamaPenerimaInput().setText("");
            view.getDestinasiPaketInput().setText("");
            view.getAlamatTujuanInput().setText("");
            view.getTipePaketInput().setText("");

            view.getBeratInput().setValue(0.0d);
            view.getVolumeInput().setValue(0.0d);

            view.getHargaLabel().setText("0,00");

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Terjadi kesalahan!\n"
                            + ex.getMessage()
            );
        }
    }


    // method buat ngitung estimasi harga paket secara otomatis berdasarkan berat dan volume
    // ngambil nilai berat dan volume dari form, hitung pake PricingUtil
    // dipanggil tiap ada perubahan berat dan volume
    private void updateEstimasiHarga() {

        double weight =
                (Double) view.getBeratInput()
                        .getValue();

        double volume =
                (Double) view.getVolumeInput()
                        .getValue();

        double harga =
                PricingUtil.hitungHarga(
                        weight,
                        volume
                );

        view.getHargaLabel().setText(
                PricingUtil.formatRupiah(harga)
        );
    }


    /**
     *
     * <h3>Menampilkan Paket Yang Masih Di Loket</h3>
     * <p>
     * Menampilkan Paket yang masih ada di loket dan belum diambil Si Kurir
     * </p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void handleCheckPaket() {

        List<PaketDTO> daftarResi = trackingDAO.getAllPaketByStatus("Diterima di Loket", loggedInUser.getId());
        view.getTxtListPaketLoket().setText("");

//        Error handling kalau kosong
        if (daftarResi.isEmpty()) {
            view.getTxtListPaketLoket().setText("[KOSONG]\n\nTidak ada paket yang sedang kamu bawa saat ini.\nSantai dulu ngab!");
            return;
        }

        StringBuilder daftarPaketTxt = new StringBuilder();
        daftarPaketTxt.append("<= Daftar Paket Yang Masih Di Loket ini =>\n");
        daftarPaketTxt.append("Total Paket: ").append(daftarResi.size()).append(" item\n");
        daftarPaketTxt.append("Format RESI-Kode-Resi - Status - [Kota Tujuan]\n\n");

        int nomor = 1;
        for (PaketDTO paket : daftarResi) {
            daftarPaketTxt.append(nomor)
                    .append(". ")
                    .append(paket.resi())
                    .append(" - ")
                    .append(paket.status())
                    .append(" - [").append(paket.destinationCity()).append("]\n");
            nomor++;
        }

        view.getTxtListPaketLoket().setText(daftarPaketTxt.toString());

//        biar paling atas atau apalah
        view.getTxtListPaketLoket().setCaretPosition(0);
    }

}