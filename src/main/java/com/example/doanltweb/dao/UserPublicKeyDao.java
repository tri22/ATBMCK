package com.example.doanltweb.dao;

import com.example.doanltweb.dao.db.JDBIConnect;
import com.example.doanltweb.dao.model.User;
import com.example.doanltweb.utils.DigitalSignature;
import org.jdbi.v3.core.Jdbi;

import java.security.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;


public class UserPublicKeyDao {
    private static final Jdbi jdbi = JDBIConnect.get();
    DigitalSignature ds = new DigitalSignature();
    // lưu public key
    public boolean savePublicKey(PublicKey publicKey, int userId) {
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        LocalDateTime now = LocalDateTime.now();

        return jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO user_public_key (user_id, public_key, create_at) " +
                                "VALUES (:user_id, :public_key, :create_at)")
                        .bind("user_id", userId)
                        .bind("public_key", encodedKey)
                        .bind("create_at", now)
                        .execute() > 0
        );
    }

    // đọc public key từ db
    public PublicKey getPublicKey(int userId) throws GeneralSecurityException {
        return jdbi.withHandle(handle -> {
            String encodedKey = handle.createQuery("SELECT public_key FROM user_public_key WHERE user_id = :user_id AND end_time IS NULL")
                    .bind("user_id",userId)
                    .mapTo(String.class)
                    .findOne()
                    .orElse(null);

            if (encodedKey == null) return null;

            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // hoặc "DSA" nếu cần
            return keyFactory.generatePublic(keySpec);
        });
    }

    // cập nhật key khi báo mất key
    public String report(int userId) throws Exception {
        LocalDateTime now = LocalDateTime.now();

        boolean res = jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE user_public_key SET end_time = :end_time WHERE user_id = :user_id AND end_time IS NULL")
                        .bind("end_time", now)
                        .bind("user_id", userId)
                        .execute() > 0
        );

            KeyPair keyPair = ds.generateKey();
            if(!savePublicKey(keyPair.getPublic(), userId)){
                System.out.println("failed to save public key");
                return null;
            }

            return ds.convertToPEM(keyPair.getPrivate());
    }


}
