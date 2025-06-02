package com.example.doanltweb.dao;

import com.example.doanltweb.dao.db.JDBIConnect;
import com.example.doanltweb.digitalSign.DigitalSignature;
import org.jdbi.v3.core.Jdbi;

import java.security.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;


public class UserPublicKeyDao {
    private static final Jdbi jdbi = JDBIConnect.get();
    DigitalSignature ds = new DigitalSignature();
    // lưu public key
    public boolean savePublicKey(String publicKey, int userId) {
        LocalDateTime now = LocalDateTime.now();
        return jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO user_public_key (user_id, public_key, create_at) " +
                                "VALUES (:user_id, :public_key, :create_at)")
                        .bind("user_id", userId)
                        .bind("public_key", publicKey)
                        .bind("create_at", now)
                        .execute() > 0
        );
    }

    // đọc public key từ db
    public String getPublicKey(int userId){
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT public_key FROM user_public_key WHERE user_id = :user_id AND end_time IS NULL")
                        .bind("user_id", userId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }


    // cập nhật key khi báo mất key
    public boolean report(int userId,LocalDateTime endTime) throws Exception {
        return jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE user_public_key SET end_time = :end_time WHERE user_id = :user_id AND end_time IS NULL")
                        .bind("end_time", endTime)
                        .bind("user_id", userId)
                        .execute() > 0
        );
    }


}
