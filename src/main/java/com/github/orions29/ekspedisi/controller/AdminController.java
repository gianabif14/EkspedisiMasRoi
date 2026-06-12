package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.UserDAO;
import com.github.orions29.ekspedisi.model.dao.UserDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.utils.business.GeneratorId;
import com.github.orions29.ekspedisi.utils.business.HashUtil;
import com.github.orions29.ekspedisi.views.AdminViews;
import com.github.orions29.ekspedisi.views.LoginView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.controller
 * <p>
 * Deskripsi fungsional dari file ini.
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 05 June 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 23:01</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class AdminController {

    private AdminViews view;
    private User loggedInAdmin;
    private UserDAO userDao;


    Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(AdminViews view, User loggedInAdmin) {
        this.view = view;
        this.loggedInAdmin = loggedInAdmin;
        this.userDao = new UserDAOMariaDb();

        initController();
    }

    private void initController() {

        view.getBtnCari().addActionListener(e -> handleCari());
        view.getBtnSimpan().addActionListener(e -> handleTambahUser());
        view.getBtnUpdate().addActionListener(e -> handleUpdateUser());
        view.getBtnHapus().addActionListener(e -> handleHapusUser());
        view.getBtnLogout().addActionListener(e -> handleLogout());
    }

    /**
     *
     * <h3></h3>
     * <p> </p>
     *
     * @param message $END$ - Deskripsi fungsi parameter ini
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void logToConsole(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        view.getTxtConsole().append(String.format("[%s] %s\n", timestamp, message));
        view.getTxtConsole().setCaretPosition(view.getTxtConsole().getDocument().getLength());
    }

    /**
     *
     * <h3>Clear Form Tampilan</h3>
     * <p> </p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void clearForm() {
        view.getTxtUserId().setText("");
        view.getTxtUsername().setText("");
        view.getTxtPassword().setText("");
        view.getTxtLokasi().setText("");
        view.getComboRole().setSelectedIndex(0);
    }


    /**
     *
     * <h3>Cari User By ID</h3>
     * <p> Mencari User berdasarkan ID </p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void handleCari() {
        String targetId = view.getTxtUserId().getText().trim();
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Masukkan ID Pekerja terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDao.getUserById(targetId);

        if (user != null) {
            view.getTxtUsername().setText(user.getUsername());
            view.getTxtLokasi().setText(user.getLocation() != null ? user.getLocation() : "");
            view.getComboRole().setSelectedItem(user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1).toLowerCase());
            view.getTxtPassword().setText(""); // Dikosongkan demi keamanan memori

            logToConsole("DATA DITEMUKAN: " + user.getId() + " | " + user.getUsername());
        } else {
            logToConsole("DATA TIDAK DITEMUKAN: ID " + targetId + " fiktif.");
            JOptionPane.showMessageDialog(view, "Pekerja tidak ditemukan di database MariaDB.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * <h3>Menambah User Baru / Insert</h3>
     * <p> </p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void handleTambahUser() {
        String role = view.getComboRole().getSelectedItem().toString().toLowerCase();
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword()).trim();
        String lokasi = view.getTxtLokasi().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username dan Password wajib diisi untuk data baru!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (lokasi.isEmpty() && !role.equals("kurir")) {
            JOptionPane.showMessageDialog(view, "Lokasi wajib diisi untuk staf Loket dan Gudang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idBaru = GeneratorId.generateUserId(role);
        String hashPass = HashUtil.hashSHA256(password);
        String finalLokasi = (lokasi.isEmpty() && role.equals("kurir")) ? null : lokasi;

        User userBaru = new User(idBaru, username, hashPass, role, finalLokasi);

        if (userDao.insertUser(userBaru)) {
            logToConsole("INSERT USER SUKSES: " + idBaru + " | " + username);
            clearForm();
            logger.info("INSER USER SUKSES : User {} berhasil ditambahkan ke database. - {}", username, loggedInAdmin.getId());
        } else {
            logToConsole("INSERT USER GAGAL: Kemungkinan username sudah dipakai.");
            logger.error("INSER USER GAGAL : User {} gagal ditambahkan ke database. - {}", username, loggedInAdmin.getId());
        }
    }


    /**
     *
     * <h3>Mengupdate User</h3>
     * <p> </p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void handleUpdateUser() {
        String targetId = view.getTxtUserId().getText().trim();
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Cari ID Pekerja terlebih dahulu sebelum melakukan update.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = view.getComboRole().getSelectedItem().toString().toLowerCase();
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword()).trim();
        String lokasi = view.getTxtLokasi().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username dan Password tidak boleh kosong saat update. Silakan ketik ulang password.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String finalLokasi = (lokasi.isEmpty() && role.equals("kurir")) ? null : lokasi;
        String hashPass = HashUtil.hashSHA256(password);

        User updatedUser = new User(targetId, username, hashPass, role, finalLokasi);

        if (userDao.updateUser(updatedUser)) {
            logToConsole("UPDATE SUKSES: Data untuk ID " + targetId + " telah diperbarui.");
            clearForm();
            logger.info("UPDATE SUKSES : User {} berhasil diperbarui. {}", username, loggedInAdmin.getId());
        } else {
            logToConsole("UPDATE GAGAL: Terjadi kesalahan pada database.");
            logger.info("UPDATE GAGAL : User {} gagal diperbarui. - {}", username, loggedInAdmin.getId());
        }
    }

    /**
     *
     * <h3>Menghapus User</h3>
     * <p> Menghapus User berdasarkan ID pada text View</p>
     *
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    private void handleHapusUser() {
        String targetId = view.getTxtUserId().getText().trim();
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Masukkan ID Pekerja yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(view, "Hapus pekerja " + targetId + " secara permanen?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (userDao.deleteUser(targetId)) {
                logToConsole("DELETE SUKSES: Pekerja " + targetId + " dihanguskan dari MariaDB.");
                clearForm();
                logger.info("DELETE SUKSES : User {} berhasil dihapus dari database. - {}", targetId, loggedInAdmin.getId());
            } else {
                logToConsole("DELETE GAGAL: ID tidak ditemukan atau terikat pada data logistik.");
                JOptionPane.showMessageDialog(view, "Gagal Hapus! Pekerja ini mungkin masih terikat dengan data resi pengiriman.", "Error Foreign Key", JOptionPane.ERROR_MESSAGE);
                logger.info("DELETE GAGAL : User {} gagal dihapus dari database. - {}", targetId, loggedInAdmin.getId());
            }
        }
    }


    // NAVIGASI
    private void handleLogout() {
        JOptionPane.showMessageDialog(view, "Logout berhasil. Mengembalikan akses kendali.");


        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("EMR Tracking System - Login");
            LoginView loginView = new LoginView();

            // Jodohkan view baru dengan controller-nya
            new LoginController(loginView);

            loginFrame.setContentPane(loginView);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });

        view.dispose();
    }
}