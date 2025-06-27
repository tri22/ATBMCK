package com.example.doanltweb.controller;

import com.example.doanltweb.dao.OrderDao;
import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.dao.model.CartItem;
import com.example.doanltweb.dao.model.Order;
import com.example.doanltweb.dao.model.OrderDetail;
import com.example.doanltweb.dao.model.User;
import com.example.doanltweb.digitalSign.DigitalSignature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@MultipartConfig
@WebServlet("/VerifyOrderServlet")
public class VerifyOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyOrderServlet() {
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
	    JsonObject jsonResponse = new JsonObject();
		HttpSession session = request.getSession();

		UserPublicKeyDao userPublicKeyDao = new UserPublicKeyDao();
		DigitalSignature ds = new DigitalSignature();
		User user = (User) session.getAttribute("auth");
	    try {
	        OrderDao orderDao = new OrderDao();
	        String signature = request.getParameter("sign").trim();
			String orderIdStr = request.getParameter("orderId").trim();
	        if (!orderIdStr.isEmpty()) {
	            int orderId = Integer.parseInt(orderIdStr);
				Order order = orderDao.getById(orderId);

				JsonObject jsonData = new JsonObject();
				jsonData.addProperty("idUser", user.getId());
				jsonData.addProperty("totalPrice", order.getTotalPrice());
				jsonData.addProperty("orderDate", order.getOrderDate());
				jsonData.addProperty("idPayment", order.getPaymentMethod().getId());
				jsonData.addProperty("quantity", order.getQuantity());
				List<OrderDetail> orderDetails = orderDao.getDetailById(orderId);
				JsonArray orderDetailsArray = new JsonArray();
				for (OrderDetail item : orderDetails) {
					JsonObject detail = new JsonObject();
					detail.addProperty("productId", item.getProduct().getId());
					detail.addProperty("productName", item.getProduct().getNameProduct());
					detail.addProperty("price", item.getProduct().getPriceProduct());
					detail.addProperty("quantity", item.getQuantity());
					orderDetailsArray.add(detail);
				}
				jsonData.add("orderDetails", orderDetailsArray);
				String orderData = jsonData.toString();
				System.out.println("Data:"+ orderData);
				String publicKey = userPublicKeyDao.getPublicKey(user.getId());
	            boolean success = ds.verifySignature(orderData,signature,publicKey);
				if (success) {
					boolean updated = orderDao.updateStatus(orderId,"VERIFIED");
					boolean inserted = orderDao.insertSignature(signature,user.getId());
					boolean updateVerifyDate = orderDao.updateVerifyDate(orderId);
					jsonResponse.addProperty("success", updated&&inserted&&updateVerifyDate);
					jsonResponse.addProperty("message", updated&&inserted&&updateVerifyDate ? "Thành công!" : "Thất bại");
				}

	        }
	    } catch (Exception e) {
	        jsonResponse.addProperty("success", false);
	        jsonResponse.addProperty("message", "Lỗi xử lý: " + e.getMessage());
	    }
	    PrintWriter out = response.getWriter();
	    out.print(jsonResponse.toString());
	    out.flush();
	}

}
