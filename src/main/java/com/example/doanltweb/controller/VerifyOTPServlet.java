package com.example.doanltweb.controller;

import java.io.*;

import com.example.doanltweb.dao.OTPDAO;
import com.example.doanltweb.dao.UserDao;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;

@WebServlet(name = "verify-otp", value = "/verify-otp")
public class VerifyOTPServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDao userDAO = new UserDao();
        OTPDAO otpDAO = new OTPDAO();
        String userIdParam = request.getParameter("user_id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.getWriter().write("Error: Missing user_id parameter.");
            return;
        }

        int userId = Integer.parseInt(userIdParam);
        String enteredOTP = request.getParameter("otp");

        if (enteredOTP == null || enteredOTP.isEmpty()) {
            response.getWriter().write("Error: Missing OTP.");
            return;
        }

        String correctOTP = otpDAO.getOTPByUserId(userId);
        if (correctOTP != null && correctOTP.equals(enteredOTP)) {
            userDAO.updateUserVerifiedById(userId);
            response.getWriter().write("Verification successful! Your account is now activated.");
        } else {
            response.getWriter().write("Invalid OTP. Please try again.");
        }
    }
}