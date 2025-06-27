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
        SecureRandom random = SecureRandom.getInstanceStrong(); // tạo ngẫu nhiên mạnh
        keyGen.initialize(2048, random); // truyền random vào
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




    public boolean verifySignature(String data, String signatureStr, String pubKey) throws Exception {
        PublicKey publicKey = (PublicKey) loadKeyFromPEM(pubKey);
        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initVerify(publicKey);
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
        pem = pem.replaceAll("\\r", "").trim(); // bỏ \r và trim đầu cuối

        if (pem.contains("-----BEGIN PUBLIC KEY-----")) {
            String content = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", ""); // bỏ tất cả khoảng trắng
            byte[] decoded = Base64.getDecoder().decode(content);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);

        } else if (pem.contains("-----BEGIN PRIVATE KEY-----")) {
            String content = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(content);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        } else {
            System.out.println("== Định dạng PEM không hợp lệ ==");
            System.out.println(pem);
            throw new IllegalArgumentException("Không hỗ trợ định dạng PEM này.");
        }
    }


    public static void main(String[] args) throws Exception {
        DigitalSignature ds = new DigitalSignature();
        System.out.println(ds.loadKeyFromPEM("-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv57O7JbDGnhySMR8eSot\n" +
                "e4NptfaRYKrVqv4MNOLNFgkEFKgm4sI7SKo9B7BnmlQ6egUkJYpdWT6psA6bCgAZ\n" +
                "xdC+ol3w982m5/MO5gs2MOQ/AwvETJg2I10OyMn+4+tXO5zmamJlEeO/wCWGMGbL\n" +
                "tDYCOHNFGuco1L+FYRQ80nS8Dv+j7bJnBl2afk0vn+6mRBYtQRHzh8dHfLhY5b6l\n" +
                "vLDKfwAsWy0j9T3J13OQfRzpeyvVuFcztPT+fAVPn9YMBOmQjRCZwIJ+bxZfzes0\n" +
                "0/IOHNzR94yy8odNfI41+XbGYxdBGWhznm2i7zWaaMpahBey4p3fIOdW97U2ivWc\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----"));
    }
}
