package com.github.orions29.ekspedisi.utils.business;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Utility Class buat nge-generate QR Code
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: gianabif14</td></tr>
 * <tr><td><b>Date</b></td><td>: 5 Juni 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 19:07</td></tr>
 * </table>
 * <hr>
 *
 * @author gianabif14
 * @since 1.0
 */
public class QrGeneratorUtil {

    private static final Logger log = LoggerFactory.getLogger(QrGeneratorUtil.class);

    private static final String QR_DIRECTORY = "database/qr_result";

    public static void generateQrCode(String resiId) {
        try {
            // Pastikan folder qr_result ada
            Path pathDir = Paths.get(QR_DIRECTORY);
            if (!Files.exists(pathDir)) {
                Files.createDirectories(pathDir);
            }

            // Path untuk file gambar QR
            String filePath = QR_DIRECTORY + "/" + resiId + ".png";
            Path pathFile = FileSystems.getDefault().getPath(filePath);

            // Generate QR Code menggunakan ZXing
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(resiId, BarcodeFormat.QR_CODE, 300, 300);

            // Konversi BitMatrix ke BufferedImage
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Siapkan kanvas baru dengan tambahan ruang di bawah untuk teks
            int qrWidth = qrImage.getWidth();
            int qrHeight = qrImage.getHeight();
            int textSpace = 40;
            BufferedImage combinedImage = new BufferedImage(qrWidth, qrHeight + textSpace, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = combinedImage.createGraphics();
            
            // Aktifkan Anti-Aliasing agar font/teks menjadi lebih halus (tidak bergerigi)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Set background putih
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, qrWidth, qrHeight + textSpace);

            // Gambar QR Code
            g.drawImage(qrImage, 0, 0, null);

            // Set font dan warna untuk teks resi
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 18)); // Font yang jelas untuk kode

            // Hitung posisi tengah untuk teks
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(resiId);
            int textX = (qrWidth - textWidth) / 2;
            int textY = qrHeight + ((textSpace - fm.getHeight()) / 2);

            // Gambar teks di bawah QR Code
            g.drawString(resiId, textX, textY);
            g.dispose();

            // Tulis gambar ke file
            ImageIO.write(combinedImage, "PNG", pathFile.toFile());

            log.info("QR Code untuk resi {} berhasil di-generate.", resiId);

        } catch (WriterException | IOException e) {
            log.error("Gagal men-generate QR Code untuk resi {}", resiId, e);
        }
    }
}
