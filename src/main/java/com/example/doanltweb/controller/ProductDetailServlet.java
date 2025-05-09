package com.example.doanltweb.controller;

import com.example.doanltweb.dao.ProductDao;
import com.example.doanltweb.dao.model.Product;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ProductDetailServlet", value = "/chitietsanpham")
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ.");
            return;
        }
        // Lấy thông tin sản phẩm từ CSDL
        ProductDao productDao = new ProductDao();
        Product product = productDao.getById(Integer.parseInt(id));

        // Kiểm tra nếu sản phẩm không tồn tại
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tồn tại.");
            return;
        }

        // Gửi dữ liệu sản phẩm đến trang JSP
        request.setAttribute("product", product);
        request.getRequestDispatcher("chitietsanpham.jsp").forward(request, response);
    }
}