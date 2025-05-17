package com.example.doanltweb.dao.model;

public class UserPublickey {
    private int id;
    private String publicKey;
    private User user;
    private String creat_at;
    private String end_time;

    public UserPublickey(String publicKey, User user, String creat_at) {
        this.publicKey = publicKey;
        this.user = user;
        this.creat_at = creat_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreat_at() {
        return creat_at;
    }

    public void setCreat_at(String creat_at) {
        this.creat_at = creat_at;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "UserPublickey{" +
                "id=" + id +
                ", publicKey='" + publicKey + '\'' +
                ", user=" + user +
                ", creat_at='" + creat_at + '\'' +
                '}';
    }
}
