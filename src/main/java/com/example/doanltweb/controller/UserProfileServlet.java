package com.example.doanltweb.controller;

import com.example.doanltweb.dao.UserPublicKeyDao;
import com.example.doanltweb.dao.model.CartItem;
import com.example.doanltweb.digitalSign.DigitalSignature;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.example.doanltweb.dao.OrderDao;
import com.example.doanltweb.dao.UserDao;
import com.example.doanltweb.dao.model.Order;
import com.example.doanltweb.dao.model.OrderDetail;
import com.example.doanltweb.dao.model.User;
import com.google.gson.*;
import org.json.JSONObject;

@MultipartConfig
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Xử lý các yêu cầu GET nếu có (chẳng hạn hiển thị thông tin người dùng)
		HttpSession session = request.getSession();
		DigitalSignature ds = new DigitalSignature();
		UserPublicKeyDao userPublicKeyDao = new UserPublicKeyDao();
    	User user = (User) session.getAttribute("auth");
		OrderDao orderDao = new OrderDao();
		Map<Order,String> orderInfo = new HashMap<>();
		Map<Order,List<OrderDetail>> orders = orderDao.getOrderWithDetails(user.getId());
		for (Order order : orders.keySet()) {

			JsonObject jsonData = new JsonObject();
			jsonData.addProperty("idUser", user.getId());
			jsonData.addProperty("totalPrice", order.getTotalPrice());
			jsonData.addProperty("orderDate", order.getOrderDate());
			jsonData.addProperty("idPayment", order.getPaymentMethod().getId());
			jsonData.addProperty("quantity", order.getQuantity());
			List<OrderDetail> details = orders.get(order);
			JsonArray orderDetailsArray = new JsonArray();
			for (OrderDetail item : details) {
				JsonObject detail = new JsonObject();
				detail.addProperty("productId", item.getProduct().getId());
				detail.addProperty("productName", item.getProduct().getNameProduct());
				detail.addProperty("price", item.getProduct().getPriceProduct());
				detail.addProperty("quantity", item.getQuantity());
				orderDetailsArray.add(detail);
			}
			jsonData.add("orderDetails", orderDetailsArray);
			String orderData = jsonData.toString();
			orderInfo.put(order, orderData);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime parsedOrderDate = LocalDateTime.parse(order.getOrderDate(), formatter);
			String publicKey = userPublicKeyDao.getValidPublicKey(parsedOrderDate,user.getId());
			System.out.println(publicKey);
            try {
                boolean verify = ds.verifySignature(orderData,order.getSign().trim(),publicKey);
				if(!verify){
					order.setStatus("NOT VERIFIED");
					orderDao.updateStatus(order.getId(),"NOT VERIFIED");
				}else {
					order.setStatus("VERIFIED");
					orderDao.updateStatus(order.getId(),"VERIFIED");
				}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
		request.setAttribute("orderMap", orderInfo);
		request.getRequestDispatcher("Userprofile.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set Content-Type là JSON
		String action = request.getParameter("action");

		if ("changePassword".equals(action)) {
			handleChangePassword(request, response);
		} else {

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			JsonObject jsonResponse = new JsonObject();
			Gson gson = new Gson();

			// Lấy thông tin người dùng từ session
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("auth");

			try {
				// Lấy các tham số từ request
				String fullname = request.getParameter("fullname");
				String email = request.getParameter("email");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");

				// Kiểm tra nếu có tham số bị thiếu
				if (fullname == null || email == null || address == null || phone == null) {
					jsonResponse.addProperty("success", false);
					jsonResponse.addProperty("message", "Các trường thông tin không thể để trống!");
					out.print(gson.toJson(jsonResponse));
					return;
				}

				// Cập nhật thông tin người dùng
				UserDao userDao = new UserDao();
				boolean isUpdated = userDao.updateUser(fullname, email, address, phone, user.getId());

				// Trả về kết quả
				jsonResponse.addProperty("success", isUpdated);
				jsonResponse.addProperty("message", isUpdated ? "Cập nhật thành công!" : "Cập nhật thất bại, thử lại sau!");

			} catch (Exception e) {
				e.printStackTrace();
				jsonResponse.addProperty("success", false);
				jsonResponse.addProperty("message", "Lỗi xử lý dữ liệu!");
			}

			// Chuyển đổi phản hồi thành JSON và trả về client
			String jsonString = gson.toJson(jsonResponse);
			System.out.println("JSON Response: " + jsonString);
			out.print(jsonString);
			out.flush();
		}
	}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");

		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		// Giả sử bạn có UserDAO để truy cập dữ liệu người dùng
		UserDao userDAO = new UserDao();

		if (!newPassword.equals(confirmPassword)) {
			json.put("success", false);
			json.put("message", "Mật khẩu mới và xác nhận không khớp.");
		} else if (!userDAO.checkPassword(auth.getId(), currentPassword)) {
			json.put("success", false);
			json.put("message", "Mật khẩu hiện tại không đúng.");
		} else {
			boolean updated = userDAO.updatePassword(auth.getId(), newPassword);
			if (updated) {
				json.put("success", true);
				json.put("message", "Đổi mật khẩu thành công!");
			} else {
				json.put("success", false);
				json.put("message", "Đổi mật khẩu thất bại.");
			}
		}

		out.print(json.toString());
		out.flush();
	}
}


