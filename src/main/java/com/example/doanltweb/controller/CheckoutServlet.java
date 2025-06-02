package com.example.doanltweb.controller;

import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.digitalSign.DigitalSignature;
import com.example.doanltweb.service.EmailService;
import com.google.gson.JsonArray;
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

import java.io.*;
import java.security.GeneralSecurityException;
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


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("thanhtoan.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		JsonObject jsonResponse = new JsonObject();

		OrderDao orderDao = new OrderDao();
		CartDao cartDao = new CartDao();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("auth");
		Cart cart = cartDao.getCartByUserId(user.getId());
		UserPublicKeyDao userPublicKeyDao = new UserPublicKeyDao();
		DigitalSignature ds = new DigitalSignature();


		String publicKey = userPublicKeyDao.getPublicKey(user.getId());
        String sign = request.getParameter("signature").trim();
		System.out.println("Signature raw: " + sign);
		int paymentMethod = Integer.parseInt(request.getParameter("paymentMethod"));
		CartUtils.mergeSessionCartToDb(user.getId(),session);

		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("idUser", user.getId());
		jsonData.addProperty("totalPrice", cart.getTotalPrice());
		String orderTime = (String) session.getAttribute("orderTime");
		jsonData.addProperty("orderDate", orderTime);
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
		System.out.println("orderData: " + orderData);
        try {
            boolean verify = ds.verifySignature(orderData,sign,publicKey);
			if(verify) {
				boolean order = orderDao.createOrder(user.getId(), cart.getTotalPrice(), paymentMethod, cart.getTotalAmount(), cart.getId(),sign);
				if(order) {
					cartDao.clearCart(cart.getId());
					session.setAttribute("cart", new ArrayList<CartItem>());
				}
				jsonResponse.addProperty("success", order);
				jsonResponse.addProperty("message", order ? "Đơn hàng đã tạo thành công!" : "Đặt hàng thất bại");
			}else {
				jsonResponse.addProperty("success", false);
				jsonResponse.addProperty("message", "Chữ ký không hợp lệ!");
			}
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

		PrintWriter out = response.getWriter();
		out.print(jsonResponse.toString());
		out.flush();
	}


}
