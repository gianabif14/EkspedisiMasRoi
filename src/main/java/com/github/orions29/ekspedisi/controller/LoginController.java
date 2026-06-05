package com.github.orions29.ekspedisi.controller;

import com.github.orions29.ekspedisi.model.dao.UserDAO;
import com.github.orions29.ekspedisi.model.dao.UserDAOMariaDb;
import com.github.orions29.ekspedisi.model.entity.User;
import com.github.orions29.ekspedisi.utils.HashUtil;
import com.github.orions29.ekspedisi.views.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Controller untuk autentikasi user
 *
 * <p>
 * Bertugas menangani proses login
 * validasi username/password,
 * routing role berdasarkan role user
 * navigasi ke dashboar sesuai role
 * </p>
 */
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    private LoginView view;
    private UserDAO userDAO;

    public LoginController(LoginView view) {

        this.view = view;
        this.userDAO = new UserDAOMariaDb();

        initController();
    }

    /**
     * Inisialisasi seluruh event listener
     */

    private void initController() {

        view.getLoginButton().addActionListener(e -> {

            handleLoginEvent();
        });

        view.getTrackButton().addActionListener(e -> {

            TrackingViews.main(null);
//            SwingUtilities.getWindowAncestor(view).dispose();
        });
    }

    /**
     * Buat proses login user ke database
     * hashing password dan autentikasi
     */
    private void handleLoginEvent() {

        String username =
                view.getUsernameInput() // ngambil inputan user dari view
                        .getText() // ngambil isi textnya
                        .trim(); // buang spasi berlebih di awal/akhir

        String password =
                String.valueOf( // mengubah char menjadi string agar bisa diproses untuk login/hashing
                        view.getPasswordInput() // ngambil komponen password field
                                .getPassword() // mengembalikan char [] beriis password user
                );

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null, // error handling usn dan pw kosong
                    "Username dan Password wajib diisi!"
            );

            return;
        }

        // Hash password input user
        String hashedPassword =
                HashUtil.hashSHA256(password);

        // Authenticate ke database
        User user =
                userDAO.authenticate(
                        username,
                        hashedPassword
                );

        if (user != null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Login Berhasil!"
            );

            routeByRole(user);

        } else { // error jika usn/pw salah

            JOptionPane.showMessageDialog(
                    null,
                    "Username/Password Salah ngab!"
            );
        }
    }

    /**
     * Routing dashboard berdasarkan role usernya apa
     *
     * @param user user hasil autentikasi database
     */
    private void routeByRole(User user) {

        String role =
                user.getRole(); // ngambil role  user

        logger.info("USER LOGGED IN : [{}] - {}", user.getId(), user.getUsername());

        switch (role.toLowerCase()) {

            case "loket": // role user loket maka akan diarahkan ke dashboard outlet

                LoketViews loketViews =
                        new LoketViews(user); // bikin dashboard loket dan mengirim data user login

                new LoketController(
                        loketViews,
                        user
                );

                loketViews.setTitle("EMR Tracking System - Loket");
                loketViews.setLocationRelativeTo(null); // window middle
                loketViews.setVisible(true); // nampilin dashboard loket/outlet

                SwingUtilities
                        .getWindowAncestor(view) // menutup windows sebelumnya
                        .dispose();

                break;

            case "gudang":  // role user gudang maka akan diarahkan ke dashboard gudang

                GudangViews gudangViews =
                        new GudangViews(user); // bikin tampilan buat role gudang

                new GudangController( // connect view dengan controller
                        gudangViews,
                        user
                );

                gudangViews.setTitle("EMR Tracking System - Gudang");
                gudangViews.setLocationRelativeTo(null); // posisi layar ditengah
                gudangViews.setVisible(true); // menampilkan halaman gudang

                SwingUtilities      // close window yang sblmnya
                        .getWindowAncestor(view)
                        .dispose();

                break;

            case "kurir":   // role user kurir maka akan diarahkan ke dashboard kurir

                KurirViews kurirViews =
                        new KurirViews(user); // bikin tampilan buat role kurir

                new KurirController(    // connect view dengan controller
                        kurirViews,
                        user
                );

                kurirViews.setTitle("EMR Tracking System - Kurir");
                kurirViews.setLocationRelativeTo(null); // set layar ditengah
                kurirViews.setVisible(true); // menampilkan halaman kurir

                SwingUtilities      // close window sebelumnya
                        .getWindowAncestor(view)
                        .dispose();

                break;

            case "admin":   // jika role admin maka akan diarahkan ke dashboard admin (under maintenance)
                AdminViews adminViews = new AdminViews(user);
                new AdminController(adminViews, user);
                adminViews.setTitle("EMR Tracking System - Admin");
                adminViews.setLocationRelativeTo(null);
                adminViews.setVisible(true);
                SwingUtilities
                        .getWindowAncestor(view)
                        .dispose();
                break;

            default:            // jika role tidak sesuai maka akan muncul error log
                JOptionPane.showMessageDialog(
                        null,
                        "Role tidak dikenali, anomali ta iki!"
                );
        }
    }
}