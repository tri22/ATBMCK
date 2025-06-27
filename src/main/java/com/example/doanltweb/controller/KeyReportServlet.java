package com.example.doanltweb.controller;
import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.dao.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MultipartConfig
@WebServlet("/KeyReportServlet")
public class KeyReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public KeyReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("auth");

            String dateStr = request.getParameter("date");
            LocalDateTime time = LocalDateTime.parse(dateStr);
            Timestamp timestamp = Timestamp.valueOf(time);


            UserPublicKeyDao dao = new UserPublicKeyDao();
            boolean privateKey = dao.report(user.getId(),timestamp); // Trả về private key
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (privateKey) {
                response.getWriter().write("{\"success\": true, \"message\": \"Khóa đã được báo cáo thành công.\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể báo cáo khóa.\"}");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
