
package com.example.doanltweb.controller;

import java.io.*;
import java.util.List;
import com.example.doanltweb.dao.model.Product;
import com.example.doanltweb.dao.model.Supplier;
import com.example.doanltweb.service.ProductService;
import com.example.doanltweb.service.SupplierService;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ProductServlet", value = "/trangchu")
public class ProductServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ProductService productService = new ProductService();
        List<Product> all = productService.getAllProducts();
//        List<Product> sale = productService.getAllProductSale();
        request.setAttribute("pall", all);
//        request.setAttribute("psale", sale);
        // nhà cung cấp
//        SupplierService service = new SupplierService();
//        List<Supplier> sall = service.getAllSupplier();
//        request.setAttribute("sall", sall);
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }
}
