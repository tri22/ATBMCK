package com.example.doanltweb.digitalSign;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

    private void exportToFile(String keyContent, String defaultFileName) {
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
        JTextArea txtFile = new JTextArea(7, 60);
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    txtFile.setText(readFileAsString(selectedFile.getAbsolutePath()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                try {
                    txtKey.setText(readFileAsString(selectedFile.getAbsolutePath()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                String keyPem = txtKey.getText().trim();
                System.out.println(keyPem);
                String res = ds.signData(keyPem,txtFile.getText().trim()) ;
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
                exportToFile(resultArea.getText(),"sign.txt");
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
        JPanel verifyPanel = new JPanel();
        verifyPanel.setLayout(new BoxLayout(verifyPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Giao diện Xác minh chữ ký");
        verifyPanel.add(label);

        File[] selectedFiles = new File[3]; // 0: tài liệu, 1: chữ ký, 2: public key

        // Tạo TextAreas
        JTextArea taiLieuArea = createTextArea();
        JTextArea chuKyArea = createTextArea();

        // Tạo các button
        JButton btnChonTaiLieu = new JButton("Chọn tài liệu gốc");
        JButton btnChonChuKy = new JButton("Chọn file chữ ký");
        JButton btnChonPublicKey = new JButton("Chọn Public Key");
        JButton btnXacMinh = new JButton("Xác minh chữ ký");

        // Panel chứa button + textArea cho tài liệu
        verifyPanel.add(createRowPanel(btnChonTaiLieu, taiLieuArea));
        verifyPanel.add(Box.createVerticalStrut(5));

        // Panel chứa button + textArea cho chữ ký
        verifyPanel.add(createRowPanel(btnChonChuKy, chuKyArea));
        verifyPanel.add(Box.createVerticalStrut(5));

        // Panel chứa button + textArea cho public key
        JPanel panel = new JPanel(new FlowLayout());
        JTextArea pubKeyArea = createTextArea();
        panel.add(btnChonPublicKey );
        panel.add(pubKeyArea);
        verifyPanel.add(panel);
        verifyPanel.add(Box.createVerticalStrut(10));

        // Thêm nút xác minh + kết quả
        verifyPanel.add(btnXacMinh);

        // Xử lý chọn tài liệu
        btnChonTaiLieu.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFiles[0] = fc.getSelectedFile();
                try {
                    taiLieuArea.setText(Files.readString(selectedFiles[0].toPath()));
                } catch (IOException ex) {
                    taiLieuArea.setText("Không thể đọc tài liệu: " + ex.getMessage());
                }
            }
        });

        // Chữ ký
        btnChonChuKy.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFiles[1] = fc.getSelectedFile();
                try {
                    chuKyArea.setText(readFileAsString(selectedFiles[1].getAbsolutePath()));
                } catch (IOException ex) {
                    chuKyArea.setText("Không thể đọc chữ ký: " + ex.getMessage());
                }
            }
        });


        // Public key
        btnChonPublicKey.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String content = readFileAsString(selectedFile.getAbsolutePath());
                    pubKeyArea.setText(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Xác minh chữ ký
        btnXacMinh.addActionListener(e -> {
            try {
                String originalData = taiLieuArea.getText().trim();
                String signature = chuKyArea.getText().trim();
                String publicKeyPem = pubKeyArea.getText().trim();
                boolean verified = ds.verifySignature(originalData, signature, publicKeyPem);
                JOptionPane.showMessageDialog(null, verified ? "Chữ ký hợp lệ!" : "Chữ ký không hợp lệ!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi xác minh: " + ex.getMessage());
            }
        });

        return verifyPanel;
    }

    // Tạo text area mặc định
    private JTextArea createTextArea() {
        JTextArea area = new JTextArea(4, 40);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        return area;
    }

    // Tạo panel chứa button + textarea (cùng hàng)
    private JPanel createRowPanel(JButton button, JTextArea textArea) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.add(button);
        rowPanel.add(Box.createHorizontalStrut(10));
        rowPanel.add(new JScrollPane(textArea));
        return rowPanel;
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
            exportToFile(privateKeyField.getText(), "private_key.pem");
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
            exportToFile(publicKeyField.getText(), "public_key.pem");
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




    public static String readFileAsString(String filePath) throws IOException {
        String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        // Chuẩn hóa xuống dòng, loại bỏ BOM nếu có
        content = content.replace("\r\n", "\n").replace("\r", "\n").trim();
        if (content.startsWith("\uFEFF")) { // BOM
            content = content.substring(1);
        }
        return content;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}
