package com.example.doanltweb.controller;

import com.example.doanltweb.dao.CartDao;
import com.example.doanltweb.dao.model.Cart;
import com.example.doanltweb.dao.model.CartItem;
import com.example.doanltweb.dao.model.User;
import com.example.doanltweb.service.EmailService;
import com.example.doanltweb.utils.CartUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@MultipartConfig
@WebServlet(name = "PreOrderServlet", value = "/PreOrder")
public class PreOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        CartDao cartDao = new CartDao();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");
        Cart cart = cartDao.getCartByUserId(user.getId());

        int paymentMethod = Integer.parseInt(request.getParameter("paymentMethod"));

        CartUtils.mergeSessionCartToDb(user.getId(),session);
        try {
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("idUser", user.getId());
            jsonData.addProperty("totalPrice", cart.getTotalPrice());
            LocalDateTime orderTime = LocalDateTime.now();
            session.setAttribute("orderTime", orderTime.toString());
            jsonData.addProperty("orderDate", orderTime.toString());
            jsonData.addProperty("idPayment", paymentMethod);
            jsonData.addProperty("quantity", cart.getTotalAmount());
            List<CartItem> cartItems = cartDao.getListCartItemByCartId(cart.getId());
            JsonArray orderDetailsArray = new JsonArray();
            for (CartItem item : cartItems) {
                JsonObject detail = new JsonObject();
                detail.addProperty("productId", item.getProduct().getId());
                detail.addProperty("productName", item.getProduct().getNameProduct());
                detail.addProperty("price", item.getProduct().getPriceProduct());
                detail.addProperty("quantity", item.getQuantity());
                orderDetailsArray.add(detail);
            }
            jsonData.add("orderDetails", orderDetailsArray);


            String orderData = jsonData.toString();
            File orderFile = new File("order.txt");
            try (FileWriter writer = new FileWriter(orderFile)) {
                writer.write(orderData);
            }

            // Gửi email trong thread riêng
            new Thread(() -> {
                EmailService.sendOrder(user.getEmail(),orderFile);
            }).start();

            jsonResponse.addProperty("success", true);
        }catch (Exception e){
            jsonResponse.addProperty("success", false);
            throw new ServletException(e);
        }
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
