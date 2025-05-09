package com.example.doanltweb.controller;

import com.example.doanltweb.service.EmailService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.example.doanltweb.dao.CartDao;
import com.example.doanltweb.dao.OrderDao;
import com.example.doanltweb.dao.PaymentDao;
import com.example.doanltweb.dao.model.Cart;
import com.example.doanltweb.dao.model.CartItem;
import com.example.doanltweb.dao.model.Order;
import com.example.doanltweb.dao.model.OrderDetail;
import com.example.doanltweb.dao.model.User;
import com.example.doanltweb.utils.CartUtils;
import com.google.gson.JsonObject;

@MultipartConfig
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		request.getRequestDispatcher("thanhtoan.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    OrderDao orderDao = new OrderDao();
	    CartDao cartDao = new CartDao();
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("auth");
	    Cart cart = cartDao.getCartByUserId(user.getId());
		String otp = generateOTP();

	    int paymentMethod = Integer.parseInt(request.getParameter("paymentMethod"));

	    CartUtils.mergeSessionCartToDb(user.getId(),session);
	    boolean order = orderDao.createOrder(user.getId(), cart.getTotalPrice(), paymentMethod, cart.getTotalAmount(), cart.getId(),otp);
	    if(order) {
	    	cartDao.clearCart(cart.getId());
	    	session.setAttribute("cart", new ArrayList<CartItem>());
			// Gửi email trong thread riêng
			new Thread(() -> {
				EmailService.sendOTP(user.getEmail(), otp);
			}).start();
	    }
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("success", order);
		jsonResponse.addProperty("message", order ? "Đơn hàng đã tạo thành công!" : "Đặt hàng thất bại");

		PrintWriter out = response.getWriter();
		out.print(jsonResponse.toString());
		out.flush();

	}


}
