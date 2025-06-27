package com.example.doanltweb.dao;

import com.example.doanltweb.dao.db.JDBIConnect;
import com.example.doanltweb.digitalSign.DigitalSignature;
import org.jdbi.v3.core.Jdbi;

import java.security.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;


public class UserPublicKeyDao {
    private static final Jdbi jdbi = JDBIConnect.get();
    LocalDateTime now = LocalDateTime.now();
    Timestamp timestampNow = Timestamp.valueOf(now);
    // lưu public key
    public boolean savePublicKey(String publicKey, int userId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO user_public_key (user_id, public_key, create_at) " +
                                "VALUES (:user_id, :public_key, :create_at)")
                        .bind("user_id", userId)
                        .bind("public_key", publicKey)
                        .bind("create_at", timestampNow)
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

    public String getValidPublicKey(String orderDate, int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT public_key FROM user_public_key " +
                                "WHERE user_id = :userId " +
                                "AND create_at <= :orderDate " +
                                "AND (:orderDate <= end_time OR end_time IS NULL) " +
                                "ORDER BY create_at DESC " +
                                "LIMIT 1")
                        .bind("userId", userId)
                        .bind("orderDate", orderDate)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }




    // cập nhật key khi báo mất key
    public boolean report(int userId, Timestamp endTime) throws Exception {
        return jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE user_public_key SET end_time = NOW() WHERE user_id = :user_id AND end_time IS NULL")
                        .bind("end_time", endTime)
                        .bind("user_id", userId)
                        .execute() > 0
        );
    }


}
