package com.example.doanltweb.utils;

import java.security.*;
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

    public String signData(String data) throws Exception {
        if (keyPair.getPrivate() == null) throw new Exception("Private key chưa được khởi tạo.");
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(data.getBytes());
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

    public String convertToPEM(PrivateKey privateKey) throws Exception {
        byte[] encoded = privateKey.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(encoded);

        // Chia dòng mỗi 64 ký tự
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");

        int index = 0;
        while (index < base64Key.length()) {
            pem.append(base64Key, index, Math.min(index + 64, base64Key.length()));
            pem.append("\n");
            index += 64;
        }

        pem.append("-----END PRIVATE KEY-----");

        return pem.toString();
    }

}
