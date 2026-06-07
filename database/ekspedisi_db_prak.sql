-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 07 Jun 2026 pada 15.43
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.4.14

SET
SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET
time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ekspedisi_db_prak`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `paket`
--

CREATE TABLE `paket`
(
    `resi_id`             varchar(30)   NOT NULL,
    `sender_name`         varchar(100)  NOT NULL,
    `origin_city`         varchar(100)  NOT NULL,
    `receiver_name`       varchar(100)  NOT NULL,
    `destination_city`    varchar(100)  NOT NULL,
    `destination_address` text          NOT NULL,
    `weight`              decimal(8, 2) NOT NULL,
    `volume`              decimal(8, 2) NOT NULL,
    `type_paket`          varchar(50)   NOT NULL,
    `created_at`          timestamp     NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tracking_logs`
--

CREATE TABLE `tracking_logs`
(
    `log_id`    int(11) NOT NULL,
    `resi_id`   varchar(30)  NOT NULL,
    `user_id`   varchar(20) DEFAULT NULL,
    `status`    varchar(50)  NOT NULL,
    `location`  varchar(100) NOT NULL,
    `timestamp` datetime    DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users`
(
    `id`            varchar(20)  NOT NULL,
    `username`      varchar(50)  NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `role`          enum('admin','kurir','gudang','loket') NOT NULL,
    `location`      varchar(100)          DEFAULT NULL,
    `created_at`    timestamp    NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users` Pass 123 Default
--

INSERT INTO `users` (`id`, `username`, `password_hash`, `role`, `location`, `created_at`)
VALUES ('A-1001', 'admintest', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'admin', 'Admin YK',
        '2026-05-31 17:27:47'),
       ('G-2002', 'gudangtest', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'gudang',
        'DC Surabaya', '2026-05-31 17:28:22'),
       ('K-3003', 'kurirtest', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'kurir', 'Godean',
        '2026-05-31 17:29:14'),
       ('L-1002', 'lokettest', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'loket',
        'Loket Pusat Godean', '2026-05-31 17:28:49');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `paket`
--
ALTER TABLE `paket`
    ADD PRIMARY KEY (`resi_id`),
  ADD KEY `idx_nama_penerima` (`receiver_name`);

--
-- Indeks untuk tabel `tracking_logs`
--
ALTER TABLE `tracking_logs`
    ADD PRIMARY KEY (`log_id`),
  ADD KEY `idx_tracking_courier` (`user_id`),
  ADD KEY `idx_status_user` (`status`,`user_id`),
  ADD KEY `idx_resi_waktu` (`resi_id`,`timestamp`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tracking_logs`
--
ALTER TABLE `tracking_logs`
    MODIFY `log_id` int (11) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `tracking_logs`
--
ALTER TABLE `tracking_logs`
    ADD CONSTRAINT `fk_tracking_paket` FOREIGN KEY (`resi_id`) REFERENCES `paket` (`resi_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_tracking_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON
DELETE
SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
