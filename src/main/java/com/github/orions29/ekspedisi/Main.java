package com.github.orions29.ekspedisi;

import com.github.orions29.ekspedisi.controller.LoginController;
import com.github.orions29.ekspedisi.utils.ProjectInit;
import com.github.orions29.ekspedisi.views.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


/**
 * Project: Ekspedisi Mas Roi
 * Package: com.github.orions29.eskpedisi
 * <p>
 * Deskripsi fungsional dari file ini.
 * </p>
 *
 * <hr>
 * <b>Author</b>: Orions29
 * <b>Date</b>: 15 May 2026
 * <b>Time</b>: 22:05
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class Main {
    static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
//        Jalanin Project Init Recheck
        ProjectInit.projectCheck();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.warn("[VIEWS ERROR] - Gagal estetik lookAndFeel");
            System.err.println("[VIEWS ERROR] - Gagal estetik cahhhhhhhhhhhhhhhhhh lookAndFeel");
        }


// Pake Invokelater untuk membuat GUI di thread yang berbeda
        SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("EMR Tracking System - Login");
            LoginView loginView = new LoginView();

            new LoginController(loginView);

            frame.setContentPane(loginView);
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
