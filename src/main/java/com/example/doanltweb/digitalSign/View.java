package com.example.doanltweb.digitalSign;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class View extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DigitalSignature ds = new DigitalSignature();
    public View() {
        setTitle("Ứng dụng chữ ký số");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo panel menu
        JPanel menuPanel = createMenuPanel();

        // Tạo panel chính với CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Thêm các panel vào mainPanel
        mainPanel.add(createSignPanel(), "SIGN");
        mainPanel.add(createVerifyPanel(), "VERIFY");
        mainPanel.add(createGenKeyPanel(), "GENKEY");

        // Thêm các thành phần vào frame
        add(menuPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Hiển thị panel mặc định
        cardLayout.show(mainPanel, "GENKEY");

        setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new FlowLayout());

        JButton btnKy = new JButton("Ký");
        JButton btnXacMinh = new JButton("Xác minh chữ ký");
        JButton btnTaoKey = new JButton("Tạo key");

        btnKy.addActionListener(e -> cardLayout.show(mainPanel, "SIGN"));
        btnXacMinh.addActionListener(e -> cardLayout.show(mainPanel, "VERIFY"));
        btnTaoKey.addActionListener(e -> cardLayout.show(mainPanel, "GENKEY"));

        menuPanel.add(btnKy);
        menuPanel.add(btnXacMinh);
        menuPanel.add(btnTaoKey);

        return menuPanel;
    }

    private JPanel createSignPanel() {
        JPanel signPanel = new JPanel();
        signPanel.setLayout(new BoxLayout(signPanel, BoxLayout.Y_AXIS));
        signPanel.setBorder(BorderFactory.createTitledBorder("Tạo chữ ký"));

        // Bước 1: Chọn file
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFile = new JLabel("Chọn file:");
        JTextField txtFile = new JTextField(50);
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtFile.setText(selectedFile.getAbsolutePath());
            }
        });

        filePanel.add(lblFile);
        filePanel.add(txtFile);
        filePanel.add(btnBrowse);

        // Bước 2: Chọn khóa riêng
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblKey = new JLabel("Khóa riêng tư:");
        JTextField txtKey = new JTextField(50);
        JButton btnKey = new JButton("Chọn khóa");
        btnKey.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtKey.setText(selectedFile.getAbsolutePath());
            }
        });

        keyPanel.add(lblKey);
        keyPanel.add(txtKey);
        keyPanel.add(btnKey);
        JTextArea resultArea = new JTextArea(5, 30);

        // Bước 4: Nút thực hiện ký
        JButton btnSign = new JButton("Ký");
        btnSign.addActionListener(e -> {
            try {
                String res = ds.signData(txtKey.getText(),txtFile.getText()) ;
                resultArea.setText(res);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Bước 5: Hiển thị kết quả

        resultArea.setEditable(false);
        JScrollPane scrollResult = new JScrollPane(resultArea);

        JButton btnExportSign= new JButton("Xuất chữ ký");

        // Sự kiện khi nhấn nút "Xuất chữ ký"
        btnExportSign.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu chữ ký");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {

                    String sign = resultArea.getText();

                    // Ghi vào file
                    FileWriter writer = new FileWriter(fileToSave);
                    writer.write(sign);
                    writer.close();

                    JOptionPane.showMessageDialog(null, "Đã lưu chữ ký tại: " + fileToSave.getAbsolutePath());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi lưu chữ ký: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        });

        JButton copyBtn = new JButton("Copy");

        // Khi nhấn nút Copy
        copyBtn.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(resultArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "Đã sao chép!");
        });
        JPanel wrapper = new JPanel(new FlowLayout());
        wrapper.add(btnSign);
        wrapper.add(copyBtn);
        wrapper.add(btnExportSign);
        // Thêm các thành phần vào panel chính
        signPanel.add(filePanel);
        signPanel.add(keyPanel);
        signPanel.add(wrapper);
        signPanel.add(scrollResult);

        return signPanel;
    }


    private JPanel createVerifyPanel() {
        JPanel panelXacMinh = new JPanel();
        panelXacMinh.setLayout(new BoxLayout(panelXacMinh, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Giao diện Xác minh chữ ký");
        panelXacMinh.add(label);

        JButton btnChonTaiLieu = new JButton("Chọn tài liệu gốc");
        JButton btnChonChuKy = new JButton("Chọn file chữ ký");
        JButton btnChonPublicKey = new JButton("Chọn Public Key");
        JButton btnXacMinh = new JButton("Xác minh chữ ký");
        JLabel lblKetQua = new JLabel("Chưa xác minh");

        File[] selectedFiles = new File[3]; // 0: tài liệu, 1: chữ ký, 2: public key

        // Button chọn tài liệu
        btnChonTaiLieu.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFiles[0] = fc.getSelectedFile();
            }
        });

        // Button chọn chữ ký
        btnChonChuKy.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFiles[1] = fc.getSelectedFile();
            }
        });

        // Button chọn public key
        btnChonPublicKey.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFiles[2] = fc.getSelectedFile();
            }
        });

        // Nút xác minh
        btnXacMinh.addActionListener(e -> {
            if (selectedFiles[0] == null || selectedFiles[1] == null || selectedFiles[2] == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ tài liệu, chữ ký và public key");
                return;
            }

            try {
                byte[] data = Files.readAllBytes(selectedFiles[0].toPath());
                byte[] signatureBytes = Files.readAllBytes(selectedFiles[1].toPath());

                // Đọc Public Key
                byte[] keyBytes = Files.readAllBytes(selectedFiles[2].toPath());
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey = kf.generatePublic(spec);

                // Xác minh chữ ký
                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(publicKey);
                sig.update(data);

                boolean verified = sig.verify(signatureBytes);
                if (verified) {
                    lblKetQua.setText("Chữ ký hợp lệ!");
                } else {
                    lblKetQua.setText("Chữ ký không hợp lệ!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi xác minh: " + ex.getMessage());
            }
        });

        // Thêm các nút vào giao diện
        panelXacMinh.add(btnChonTaiLieu);
        panelXacMinh.add(btnChonChuKy);
        panelXacMinh.add(btnChonPublicKey);
        panelXacMinh.add(btnXacMinh);
        panelXacMinh.add(lblKetQua);

        return panelXacMinh;
    }


    private JPanel createGenKeyPanel() {
        JPanel panelTaoKey = new JPanel(null); // Layout tự do

        JLabel title = new JLabel("Tạo key");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(300, 20, 200, 30);

        // ----- PRIVATE KEY -----
        JLabel privateKeyLabel = new JLabel("Private Key");
        privateKeyLabel.setBounds(100, 80, 100, 30);

        JTextArea privateKeyField = new JTextArea();
        privateKeyField.setLineWrap(true);
        privateKeyField.setWrapStyleWord(true);
        JScrollPane privateScroll = new JScrollPane(privateKeyField);
        privateScroll.setBounds(200, 80, 400, 80);

        JButton exportPrivateBtn = new JButton("Xuất File");
        exportPrivateBtn.setBounds(610, 80, 100, 30);
        exportPrivateBtn.addActionListener(e -> {
            exportKeyToFile(privateKeyField.getText(), "private_key.pem");
        });
        JButton copyPrivateBtn = new JButton("Copy");
        copyPrivateBtn.setBounds(720, 80, 80, 30);
        copyPrivateBtn.addActionListener(e -> {
            StringSelection selection = new StringSelection(privateKeyField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        });

        // ----- PUBLIC KEY -----
        JLabel publicKeyLabel = new JLabel("Public Key");
        publicKeyLabel.setBounds(100, 180, 100, 30);

        JTextArea publicKeyField = new JTextArea();
        publicKeyField.setLineWrap(true);
        publicKeyField.setWrapStyleWord(true);
        JScrollPane publicScroll = new JScrollPane(publicKeyField);
        publicScroll.setBounds(200, 180, 400, 80);

        JButton exportPublicBtn = new JButton("Xuất File");
        exportPublicBtn.setBounds(610, 180, 100, 30);
        exportPublicBtn.addActionListener(e -> {
            exportKeyToFile(publicKeyField.getText(), "public_key.pem");
        });
        JButton copyPublicBtn = new JButton("Copy");
        copyPublicBtn.setBounds(720, 180, 80, 30);
        copyPublicBtn.addActionListener(e -> {
            StringSelection selection = new StringSelection(publicKeyField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        });

        // ----- BUTTON TẠO KEY -----
        JButton genKeyBtn = new JButton("Tạo key");
        genKeyBtn.setBounds(330, 280, 100, 30);
        genKeyBtn.addActionListener(e -> {
            try {
                KeyPair keyPair = ds.generateKey();
                if (keyPair != null) {
                    String privateKey = ds.convertToPEM(keyPair.getPrivate(), "private");
                    String publicKey = ds.convertToPEM(keyPair.getPublic(), "public");
                    publicKeyField.setText(publicKey);
                    privateKeyField.setText(privateKey);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo key: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Thêm vào panel
        panelTaoKey.add(title);
        panelTaoKey.add(privateKeyLabel);
        panelTaoKey.add(privateScroll);
        panelTaoKey.add(exportPrivateBtn);
        panelTaoKey.add(copyPrivateBtn);
        panelTaoKey.add(publicKeyLabel);
        panelTaoKey.add(publicScroll);
        panelTaoKey.add(exportPublicBtn);
        panelTaoKey.add(copyPublicBtn);
        panelTaoKey.add(genKeyBtn);



        return panelTaoKey;
    }


    private void exportKeyToFile(String keyContent, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (java.io.FileWriter writer = new java.io.FileWriter(fileToSave)) {
                writer.write(keyContent);
                JOptionPane.showMessageDialog(this, "Đã lưu file thành công: " + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi ghi file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}
