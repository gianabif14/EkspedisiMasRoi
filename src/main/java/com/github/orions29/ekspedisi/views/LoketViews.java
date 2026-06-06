package com.github.orions29.ekspedisi.views;

import com.github.orions29.ekspedisi.model.entity.User;

import javax.swing.*;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.views
 * <p>
 * Panel Outlet dengan Arsitektur Dual-Tab
 * </p>
 *
 * <hr>
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 Mei 2026</td></tr>
 * <hr>
 */
public class LoketViews extends javax.swing.JFrame {

    private final User loggedInUser;

    public LoketViews(User user) {
        // Error Handling kalau User belum auth dari controller tp bisa masuk Loket
        if (user == null) {
            throw new IllegalArgumentException("Whooppssssssss Illegal Access bro!, Panel loket wajib menerima data User yang valid.");
        }
        this.loggedInUser = user;

        initComponents();

        // User yang Login
        jLabel3.setText(loggedInUser.getUsername() + " (" + loggedInUser.getId() + ")");
        jLabel1.setText("Panel Outlet Ekspedisi - " + (loggedInUser.getLocation() != null ? loggedInUser.getLocation() : "Pusat"));

        kotaPengirimInput.setText(loggedInUser.getLocation() != null ? loggedInUser.getLocation() : "Pusat HQ");
        kotaPengirimInput.setEditable(false);

        // Di Nolkan semua spinnernya
        beratInput.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 99999.99d, 0.5d));
        volumeInput.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 99999.99d, 10.0d));

        logoutButton.setBackground(new java.awt.Color(153, 0, 51));
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        tabbedPane = new javax.swing.JTabbedPane();
        panelTabInput = new javax.swing.JPanel();
        panelTabPaketList = new javax.swing.JPanel();

        panelInputPaket = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tipePaketInput = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        volumeInput = new javax.swing.JSpinner();
        beratInput = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        inputPaketButton = new javax.swing.JButton();

        panelInputIdentitas = new javax.swing.JPanel();
        namaPenerimaInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        destinasiPaketInput = new javax.swing.JTextField();
        labelNamaPenerima = new javax.swing.JLabel();
        labelDestinasiPaket = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        alamatTujuanInput = new javax.swing.JTextArea();
        labelAlamatTujuan = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelNamaPengirim = new javax.swing.JLabel();
        namaPengirimInput = new javax.swing.JTextField();
        kotaPengirimInput = new javax.swing.JTextField();

        // Komponen Panel Radar (Baru)
        checkPaketButton = new javax.swing.JButton();
        jScrollPanePaketList = new javax.swing.JScrollPane();
        txtPaketListLoket = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("Panel Outlet Ekspedisi (Loket)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jLabel2.setText("Petugas :");

        jLabel3.setText("Username_Loket");

        logoutButton.setText("Logout");

        panelInputPaket.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel14.setText("Data Paket");
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel10.setText("Berat (Kg)");
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel11.setText("Volume (Cm3)");
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel12.setText("Kategori Barang");
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jLabel15.setText("Estimasi Harga (Rp)");
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel16.setText("Rp");
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel17.setText("0,00");
        inputPaketButton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        inputPaketButton.setText("Simpan Data Paket");

        javax.swing.GroupLayout panelInputPaketLayout = new javax.swing.GroupLayout(panelInputPaket);
        panelInputPaket.setLayout(panelInputPaketLayout);
        panelInputPaketLayout.setHorizontalGroup(
                panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelInputPaketLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelInputPaketLayout.createSequentialGroup()
                                                .addGroup(panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jLabel10)
                                                                .addComponent(jLabel14)
                                                                .addComponent(jLabel12)
                                                                .addComponent(tipePaketInput)
                                                                .addComponent(jLabel11)
                                                                .addComponent(volumeInput)
                                                                .addComponent(beratInput, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel15)
                                                        .addGroup(panelInputPaketLayout.createSequentialGroup()
                                                                .addComponent(jLabel16)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel17)))
                                                .addGap(0, 22, Short.MAX_VALUE))
                                        .addComponent(inputPaketButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        panelInputPaketLayout.setVerticalGroup(
                panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelInputPaketLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tipePaketInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(volumeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(beratInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInputPaketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(inputPaketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23))
        );

        panelInputIdentitas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setText("Kota Pengirim");
        labelNamaPenerima.setFont(new java.awt.Font("Segoe UI", 0, 14));
        labelNamaPenerima.setText("Nama Penerima");
        labelDestinasiPaket.setFont(new java.awt.Font("Segoe UI", 0, 14));
        labelDestinasiPaket.setText("Kota Tujuan");
        alamatTujuanInput.setColumns(20);
        alamatTujuanInput.setRows(5);
        jScrollPane1.setViewportView(alamatTujuanInput);
        labelAlamatTujuan.setFont(new java.awt.Font("Segoe UI", 0, 14));
        labelAlamatTujuan.setText("Alamat Detail Tujuan");
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel13.setText("Identitas dan Alamat");
        labelNamaPengirim.setFont(new java.awt.Font("Segoe UI", 0, 14));
        labelNamaPengirim.setText("Nama Pengirim");

        javax.swing.GroupLayout panelInputIdentitasLayout = new javax.swing.GroupLayout(panelInputIdentitas);
        panelInputIdentitas.setLayout(panelInputIdentitasLayout);
        panelInputIdentitasLayout.setHorizontalGroup(
                panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelInputIdentitasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13)
                                        .addGroup(panelInputIdentitasLayout.createSequentialGroup()
                                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(labelNamaPengirim)
                                                        .addComponent(jLabel6)
                                                        .addComponent(namaPengirimInput)
                                                        .addComponent(kotaPengirimInput, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                                                .addGap(57, 57, 57)
                                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(labelDestinasiPaket)
                                                                .addComponent(labelNamaPenerima)
                                                                .addComponent(labelAlamatTujuan)
                                                                .addComponent(namaPenerimaInput)
                                                                .addComponent(destinasiPaketInput, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(30, Short.MAX_VALUE))
        );
        panelInputIdentitasLayout.setVerticalGroup(
                panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelInputIdentitasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelNamaPengirim)
                                        .addComponent(labelNamaPenerima))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(namaPengirimInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(namaPenerimaInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelDestinasiPaket)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInputIdentitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(destinasiPaketInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(kotaPengirimInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelAlamatTujuan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Layout untuk Tab 1 (Menggabungkan Identitas & Paket)
        javax.swing.GroupLayout panelTabInputLayout = new javax.swing.GroupLayout(panelTabInput);
        panelTabInput.setLayout(panelTabInputLayout);
        panelTabInputLayout.setHorizontalGroup(
                panelTabInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelTabInputLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelInputIdentitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelInputPaket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTabInputLayout.setVerticalGroup(
                panelTabInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelTabInputLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelTabInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelInputPaket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelInputIdentitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Input Paket Baru", panelTabInput);


        // SETTING ISI TAB 2: RADAR LOKET
        checkPaketButton.setFont(new java.awt.Font("Segoe UI", 1, 12));
        checkPaketButton.setText("Refresh Muatan Loket");

        txtPaketListLoket.setEditable(false);
        txtPaketListLoket.setBackground(new java.awt.Color(0, 0, 0));
        txtPaketListLoket.setColumns(20);
        txtPaketListLoket.setFont(new java.awt.Font("Consolas", 0, 14));
        txtPaketListLoket.setForeground(new java.awt.Color(0, 255, 0));
        txtPaketListLoket.setRows(5);
        jScrollPanePaketList.setViewportView(txtPaketListLoket);

        javax.swing.GroupLayout panelTabRadarLayout = new javax.swing.GroupLayout(panelTabPaketList);
        panelTabPaketList.setLayout(panelTabRadarLayout);
        panelTabRadarLayout.setHorizontalGroup(
                panelTabRadarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelTabRadarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelTabRadarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPanePaketList, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
                                        .addGroup(panelTabRadarLayout.createSequentialGroup()
                                                .addComponent(checkPaketButton)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        panelTabRadarLayout.setVerticalGroup(
                panelTabRadarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelTabRadarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(checkPaketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPanePaketList, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                .addContainerGap())
        );

        tabbedPane.addTab("Cek Paket di Loket", panelTabPaketList);


        // SETTING MAIN FRAME
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tabbedPane)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(logoutButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(logoutButton))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    /**
     *
     * <h3>Dummy Views</h3>
     * <p> </p>
     *
     * @param args - Deskripsi fungsi parameter ini
     * @author Orions29
     * @since 6 Jun 2026
     *
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        SwingUtilities.invokeLater(() -> {
            User dummyUser = new User("L-2001", "loket_Herry", "hash", "loket", "Fasilitas Sortir Godean");
            LoketViews frame = new LoketViews(dummyUser);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


    public JButton getCheckPaketButton() {
        return checkPaketButton;
    }

    public javax.swing.JButton getLogoutButton() {
        return logoutButton;
    }

    public javax.swing.JButton getInputPaketButton() {
        return inputPaketButton;
    }

    public javax.swing.JTextField getNamaPengirimInput() {
        return namaPengirimInput;
    }

    public javax.swing.JTextField getNamaPenerimaInput() {
        return namaPenerimaInput;
    }

    public javax.swing.JTextField getDestinasiPaketInput() {
        return destinasiPaketInput;
    }

    public javax.swing.JTextArea getAlamatTujuanInput() {
        return alamatTujuanInput;
    }

    public javax.swing.JTextField getTipePaketInput() {
        return tipePaketInput;
    }

    public javax.swing.JSpinner getBeratInput() {
        return beratInput;
    }

    public javax.swing.JSpinner getVolumeInput() {
        return volumeInput;
    }

    public javax.swing.JLabel getHargaLabel() {
        return jLabel17;
    }

    public javax.swing.JTextArea getTxtListPaketLoket() {
        return txtPaketListLoket;
    }

    private javax.swing.JTextArea alamatTujuanInput;
    private javax.swing.JSpinner beratInput;
    private javax.swing.JButton checkPaketButton;
    private javax.swing.JTextField destinasiPaketInput;
    private javax.swing.JButton inputPaketButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPanePaketList;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField kotaPengirimInput;
    private javax.swing.JLabel labelAlamatTujuan;
    private javax.swing.JLabel labelDestinasiPaket;
    private javax.swing.JLabel labelNamaPenerima;
    private javax.swing.JLabel labelNamaPengirim;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField namaPenerimaInput;
    private javax.swing.JTextField namaPengirimInput;
    private javax.swing.JPanel panelInputIdentitas;
    private javax.swing.JPanel panelInputPaket;
    private javax.swing.JPanel panelTabInput;
    private javax.swing.JPanel panelTabPaketList;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField tipePaketInput;
    private javax.swing.JTextArea txtPaketListLoket;
    private javax.swing.JSpinner volumeInput;
    // End of variables declaration
}