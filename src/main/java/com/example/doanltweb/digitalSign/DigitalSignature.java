package com.example.doanltweb.digitalSign;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class DigitalSignature {
    private KeyPair keyPair;



    public DigitalSignature() {}


    public DigitalSignature(KeyPair keyPair) {
        this.keyPair = keyPair;

    }

    public KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        this.keyPair = keyGen.generateKeyPair();
        return this.keyPair;
    }

    public KeyPair generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        this.keyPair = keyGen.generateKeyPair();
        return this.keyPair;
    }

    public String signData(String key,String data) throws Exception {
        PrivateKey privateKey = (PrivateKey) loadKeyFromPEM(key);

        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }




    public boolean verifySignature(String data, String signatureStr) throws Exception {
        if (keyPair.getPublic() == null) throw new Exception("Public key chưa được khởi tạo.");
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(keyPair.getPublic());
        signature.update(data.getBytes());
        byte[] digitalSignature = Base64.getDecoder().decode(signatureStr);
        return signature.verify(digitalSignature);
    }

    // Getter
    public KeyPair getKeyPair() {
        return keyPair;
    }

    public String convertToPEM(Key key, String type) throws Exception {
        byte[] encoded = key.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(encoded);

        // Chuẩn hóa type để in hoa toàn bộ (phòng trường hợp truyền "private", "Public",...)
        String upperType = type.toUpperCase();

        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(upperType).append(" KEY-----\n");

        int index = 0;
        while (index < base64Key.length()) {
            pem.append(base64Key, index, Math.min(index + 64, base64Key.length()));
            pem.append("\n");
            index += 64;
        }

        pem.append("-----END ").append(upperType).append(" KEY-----");

        return pem.toString();
    }

    public Key loadKeyFromPEM(String pem) throws Exception {
        pem = pem.trim();

        if (pem.contains("-----BEGIN PUBLIC KEY-----")) {
            // Xử lý Public Key (X.509)
            String content = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(content);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);

        } else if (pem.contains("-----BEGIN PRIVATE KEY-----")) {
            // Xử lý Private Key (PKCS#8)
            String content = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(content);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);

        } else {
            throw new IllegalArgumentException("Không hỗ trợ định dạng PEM này.");
        }
    }


}
