package com.example.doanltweb.controller;
import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@MultipartConfig
@WebServlet("/KeyReportServlet")
public class KeyReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public KeyReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Hàm tạo OTP 6 chữ số
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        // Tạo 6 chữ số ngẫu nhiên
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10)); // Chọn ngẫu nhiên 1 số từ 0 đến 9
        }

        return otp.toString();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserPublicKeyDao dao = new UserPublicKeyDao();
        String publicKey = request.getParameter("publicKey");
        String timestamp = request.getParameter("timestamp");
        String userId = request.getParameter("userId");
        if (timestamp != null&&userId!=null) {
            LocalDateTime time = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            try {
                boolean success = dao.report(Integer.parseInt(userId), time,publicKey) ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void sendOTPMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = request.getParameter("email");
            String otp = generateOTP();
            if(email!= null) {
                // Gửi email trong thread riêng
                new Thread(() -> {
                    EmailService.sendOTP(email, otp);
                }).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
