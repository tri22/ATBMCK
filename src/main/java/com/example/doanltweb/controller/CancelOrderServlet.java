package com.example.doanltweb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.example.doanltweb.dao.OrderDao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class CancelOrderServlet
 */
@MultipartConfig
@WebServlet("/CancelOrderServlet")
public class CancelOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
    	OrderDao orderDao = new OrderDao();
        String orderIdStr = request.getParameter("orderId");

        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            try {
                // Chuyển đổi từ String sang Integer
                int orderId = Integer.parseInt(orderIdStr);

                // Giả sử bạn có phương thức hủy đơn hàng
                boolean success = orderDao.updateStatus(orderId, "CANCELLED");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"status\":\"Hủy thành công\"}");

			} catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"status\":\"Lỗi\"}");
            }
        } 
	}

}
