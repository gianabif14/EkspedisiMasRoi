package com.github.orions29.ekspedisi.model.entity;

/**
 * Project: EkspedisiMasRoi
 * Package: com.github.orions29.ekspedisi.model.entity
 * <p>
 * Model table dari User
 * </p>
 *
 * <hr>
 * <table border="0">
 * <tr><td><b>Author</b></td><td>: Orions29</td></tr>
 * <tr><td><b>Date</b></td><td>: 30 May 2026</td></tr>
 * <tr><td><b>Time</b></td><td>: 10:32</td></tr>
 * </table>
 * <hr>
 *
 * @author Orions29
 * @since 1.0
 */
public class User {
    private String id;
    private String username;
    private String passwordHash;
    private String role;
    private String location;

    public User() {
    }

    public User(String id, String username, String passwordHash, String role, String location) {
        this.id = id.replaceAll("[\\r\\n]+", "").trim();
        this.username = username.replaceAll("[\\r\\n]+", "").trim();
        this.passwordHash = passwordHash;
        this.role = role;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Biar gampang di debug
     *
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", location='" + (location != null ? location : "Mobile/Kurir") + '\'' +
                '}';
    }
}
