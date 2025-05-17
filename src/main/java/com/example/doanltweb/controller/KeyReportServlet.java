package com.example.doanltweb.controller;
import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.dao.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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

            String userIdStr = request.getParameter("user_id");
            int userId = Integer.parseInt(userIdStr);


            UserPublicKeyDao dao = new UserPublicKeyDao();
            String privateKey = dao.report(userId); // Trả về private key
            System.out.println("report "+privateKey);
            response.setContentType("text/plain");
            response.getWriter().write(privateKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
