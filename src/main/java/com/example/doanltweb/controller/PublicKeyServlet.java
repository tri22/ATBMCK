package com.example.doanltweb.controller;
import com.example.doanltweb.dao.OrderDao;
import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.dao.model.User;
import com.example.doanltweb.service.EmailService;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@MultipartConfig
@WebServlet("/PublicKeyServlet")
public class PublicKeyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public PublicKeyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");
        if (user == null) return;

        UserPublicKeyDao dao = new UserPublicKeyDao();
        String date = request.getParameter("date");
        String newPublicKey = request.getParameter("public-key");
        String sign = request.getParameter("signature");

        boolean success = false;
        if (date != null && newPublicKey != null) {
            try {
                LocalDateTime time = LocalDateTime.parse(date);
                Timestamp timestamp = Timestamp.valueOf(time);
                success =  dao.report(user.getId(), timestamp);
                System.out.println("Report: " + success);
                success = dao.savePublicKey(newPublicKey, user.getId());
                System.out.println("Update key: " + success);
            } catch (Exception e) {
                 e.getMessage();
            }
        }else if(sign != null){

            OrderDao orderDao = new OrderDao();
            success = orderDao.insertSignature(sign.trim(), user.getId());
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", success);
        jsonResponse.addProperty("message", success ? "Thao tác thành công!" : "Thao tác thất bại");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }


}
