package com.github.orions29.ekspedisi.utils.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.utils
 * <p>
 * Utility Class buat ngehash seseuatu
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 12:10</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class HashUtil {


    private static final Logger log = LoggerFactory.getLogger(HashUtil.class);

    /**
     *
     * <h3>Ngehash Sesuatu</h3>
     * <p>Method untuk ngehash sesuatu ke SHA-256</p>
     *
     * @param originalString - String yang mau di Hash
     * @return {@link String} - Hash String
     * @author Orions29
     * @since 1 Jun 2026
     *
     */
    public static String hashSHA256(String originalString) {
        Logger logger = LoggerFactory.getLogger(HashUtil.class);
        try {
//            Ngambil Objek Hashing
            MessageDigest hashMethod = MessageDigest.getInstance("SHA-256");
//            Ngehash String menjadi Byte
            byte[] encodedHash = hashMethod.digest(originalString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("[FATAL ERROR] - Hash Algorithm Not Found in this machino");
            throw new RuntimeException("Fatal Error: Mesin kriptografi SHA-256 tidak tersedia di JVM!", e);

        }
    }
}
