package com.example.doanltweb.dao;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import com.example.doanltweb.dao.db.JDBIConnect;
import com.example.doanltweb.dao.model.CartItem;
import com.example.doanltweb.dao.model.Order;
import com.example.doanltweb.dao.model.OrderDetail;
import com.example.doanltweb.dao.model.Payment;
import com.example.doanltweb.dao.model.Product;
import com.example.doanltweb.dao.model.User;
import java.sql.Timestamp;

public class OrderDao {
	  static Map<Integer, Order> data = new HashMap<>();
	  UserDao userDao = new UserDao();
	  PaymentDao paymentDao = new PaymentDao();
	  CartDao cartDao = new CartDao();
	  ProductDao productDao = new ProductDao();
	  
	    public List<Order> getOrderByUserId(int id) {
	        Jdbi jdbi = JDBIConnect.get();
	        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM orders WHERE idUser = :id")
	        		.bind("id", id)
	        		.map((rs, ctx) -> {
	                    int orderId = rs.getInt("id");
	                    int userId = rs.getInt("idUser");  // Kiểm tra idUser có lấy đúng không
	                    double totalPrice = rs.getDouble("totalPrice");
	                    String orderDate = rs.getString("orderDate");
	                    String status = rs.getString("status");
	                    int idPayment = rs.getInt("idPayment");
	                    int quantity = rs.getInt("quantity");
						String sign = rs.getString("sign");
						String verifyDate = rs.getString("verifyDate");
	                    User user = userDao.getUserbyid(userId);
	                    Payment payment = paymentDao.getPaymentbyid(idPayment);
	                    return new Order(orderId, user, totalPrice, orderDate, status, payment,verifyDate, quantity,sign);
	                }).list());
	    }
	    public List<Order> getActiveOrder(int id) {
	        Jdbi jdbi = JDBIConnect.get();
	        return jdbi.withHandle(handle -> handle.createQuery(
	                "SELECT * FROM orders WHERE idUser = :id AND status != 'CANCELLED'")
	            .bind("id", id)
	            .map((rs, ctx) -> {
	                int orderId = rs.getInt("id");
	                int userId = rs.getInt("idUser");
	                double totalPrice = rs.getDouble("totalPrice");
	                String orderDate = rs.getString("orderDate");
	                String status = rs.getString("status");
	                int idPayment = rs.getInt("idPayment");
	                int quantity = rs.getInt("quantity");
	                String sign = rs.getString("sign");
					String verifyDate = rs.getString("verifyDate");
					User user = userDao.getUserbyid(userId);
					Payment payment = paymentDao.getPaymentbyid(idPayment);
					return new Order(orderId, user, totalPrice, orderDate, status, payment,verifyDate, quantity,sign);
	            })
	            .list()
	        );
	    }


	    public List<OrderDetail> getDetailById(int id) {
	        Jdbi jdbi = JDBIConnect.get();
	        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM detailorder WHERE idOrder = :id")
	                .bind("id", id)
	                .map((rs, ctx) -> {
	                	int detailId = rs.getInt("id");
	                	int orderId = rs.getInt("idOrder");
	                	int productId = rs.getInt("idProduct");
	                	int quantity = rs.getInt("quantity");
	                	double price = rs.getDouble("price");
	                	Product p = productDao.getById(productId);
	                	return new OrderDetail(detailId,orderId,p,quantity,price);
	                }).list());
	                
	               
	    }
	    public List<Order> getAllOrder() {
	        Jdbi jdbi = JDBIConnect.get();  // Kết nối với cơ sở dữ liệu
	        return jdbi.withHandle(handle -> 
	            handle.createQuery("SELECT * FROM orders")  // Truy vấn tất cả đơn hàng
	                  .map((rs, ctx) -> {
	                      int orderId = rs.getInt("id");
	                      int userId = rs.getInt("idUser");
	                      double totalPrice = rs.getDouble("totalPrice");
	                      String orderDate = rs.getString("orderDate");
	                      String status = rs.getString("status");
	                      int idPayment = rs.getInt("idPayment");
	                      int quantity = rs.getInt("quantity");
						  String sign = rs.getString("sign");
						  String verifyDate = rs.getString("verifyDate");
						  User user = userDao.getUserbyid(userId);
						  Payment payment = paymentDao.getPaymentbyid(idPayment);
						  return new Order(orderId, user, totalPrice, orderDate, status, payment,verifyDate, quantity,sign);
	                  })
	                  .list()  // Trả về danh sách đơn hàng
	        );
	    }

	    
	    public Map<Order, List<OrderDetail>> getOrderWithDetails() {
	        Map<Order, List<OrderDetail>> map = new LinkedHashMap<>();
	        List<Order> orders = getAllOrder();

	        for (Order order : orders) {
	            List<OrderDetail> details = getDetailById(order.getId());
	            map.put(order, details);
	        }

	        return map;
	    }

	public Map<Order, List<OrderDetail>> getOrderWithDetails(int userId) {
		Map<Order, List<OrderDetail>> map = new LinkedHashMap<>();
		List<Order> orders = getActiveOrder(userId);

		for (Order order : orders) {
			List<OrderDetail> details = getDetailById(order.getId());
			map.put(order, details);
		}
		return map;
	}

	public boolean createOrder(int userId, double totalPrice, int idPayment, int quantity, int cartId, String sign) {
		Jdbi jdbi = JDBIConnect.get();

		try {
			return jdbi.inTransaction(handle -> {
				LocalDateTime now = LocalDateTime.now();
				Timestamp timestampNow = Timestamp.valueOf(now); // ✅ chuyển đổi về Timestamp

				int orderId = handle.createUpdate(
								"INSERT INTO orders (idUser, totalPrice, orderDate, status, idPayment, quantity, sign, verifyDate) " +
										"VALUES (:userId, :totalPrice, :now, 'VERIFIED', :idPayment, :quantity, :sign, :now)")
						.bind("userId", userId)
						.bind("totalPrice", totalPrice)
						.bind("idPayment", idPayment)
						.bind("quantity", quantity)
						.bind("sign", sign)
						.bind("now", timestampNow)
						.executeAndReturnGeneratedKeys("id")
						.mapTo(Integer.class)
						.one();

				// Ghi chi tiết đơn hàng
				List<CartItem> cartItems = cartDao.getListCartItemByCartId(cartId);
				for (CartItem item : cartItems) {
					handle.createUpdate(
									"INSERT INTO detailorder (idOrder, idProduct, quantity, price) " +
											"VALUES (:orderId, :productId, :quantity, :price)")
							.bind("orderId", orderId)
							.bind("productId", item.getProduct().getId())
							.bind("quantity", item.getQuantity())
							.bind("price", item.getProduct().getPriceProduct())
							.execute();
				}

				// Xóa giỏ hàng sau khi đặt hàng
				handle.createUpdate("DELETE FROM cart WHERE user_id = :userId")
						.bind("userId", userId)
						.execute();

				return true;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean updateStatus(int id, String status) {
			Jdbi jdbi = JDBIConnect.get();
			int rowsAffected = jdbi.withHandle(handle -> 
			handle.createUpdate("UPDATE orders SET status = :status WHERE id = :id")
		    .bind("status", status)
		    .bind("id", id)
		    .execute());
			return rowsAffected>0;
		}

	public boolean updateVerifyDate(int id) {
		Jdbi jdbi = JDBIConnect.get();
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestampNow = Timestamp.valueOf(now);
		int rowsAffected = jdbi.withHandle(handle ->
				handle.createUpdate("UPDATE orders SET verifyDate = :verifyDate WHERE id = :id")
						.bind("id", id)
						.bind("verifyDate", timestampNow)
						.execute());
		return rowsAffected>0;
	}

		public List<OrderDetail> getAllDetail() {
		Jdbi jdbi = JDBIConnect.get();
		return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM detailorder")
				.map((rs, ctx) -> {
					int detailId = rs.getInt("id");
					int orderId = rs.getInt("idOrder");
					int productId = rs.getInt("idProduct");
					int quantity = rs.getInt("quantity");
					double price = rs.getDouble("price");
					Product p = productDao.getById(productId);
					return new OrderDetail(detailId,orderId,p,quantity,price);
				}).list());

	}


	public Order getById(int id) {
		Jdbi jdbi = JDBIConnect.get();
		return jdbi.withHandle(handle ->
				handle.createQuery("SELECT * FROM orders WHERE id = :id ")
						.bind("id", id)
						.map((rs, ctx) -> {
							Order order = new Order();
							order.setId(id);
							order.setOrderDate(rs.getString("orderDate"));
							order.setQuantity(rs.getInt("quantity"));
							order.setTotalPrice(rs.getDouble("totalPrice"));
							order.setStatus(rs.getString("status"));
							order.setSign(rs.getString("sign"));
							User user = userDao.getUserbyid(rs.getInt("idUser"));  // Lấy thông tin người dùng
							Payment payment = paymentDao.getPaymentbyid(rs.getInt("idPayment"));
							order.setUser(user);
							order.setPaymentMethod(payment);

							return order;
						})
						.findFirst()
						.orElse(null)
		);
	}



	public List<Order> getOrdersWithPagination(int offset, int limit) {
	    Jdbi jdbi = JDBIConnect.get();
	    return jdbi.withHandle(handle ->
	        handle.createQuery("SELECT * FROM orders ORDER BY orderDate DESC LIMIT :limit OFFSET :offset")
	              .bind("limit", limit)
	              .bind("offset", offset)
	              .map((rs, ctx) -> {
	                  Order order = new Order();
	                  order.setId(rs.getInt("id"));
	                  order.setOrderDate((rs.getString("orderDate")));   
	                  order.setQuantity(rs.getInt("quantity"));
	                  order.setTotalPrice(rs.getDouble("totalPrice"));
	                  order.setStatus(rs.getString("status"));
					  order.setSign(rs.getString("sign"));
	                  User user = userDao.getUserbyid(rs.getInt("idUser"));  // Lấy thông tin người dùng
                      Payment payment = paymentDao.getPaymentbyid(rs.getInt("idPayment"));
	                  order.setUser(user);
	                  order.setPaymentMethod(payment);

	                  return order;
	              })
	              .list()
	    );
	}


	public boolean insertSignature(String sign, int userId) {
		Jdbi jdbi = JDBIConnect.get();
		int rowsAffected = jdbi.withHandle(handle ->
				handle.createUpdate("UPDATE orders SET sign = :sign WHERE idUser = :userId")
						.bind("sign", sign)
						.bind("userId", userId)
						.execute());
		return rowsAffected>0;
	}

	public Map<Order, List<OrderDetail>> getFilteredOrders(String status, String fromDateStr, String toDateStr, Integer paymentMethod) {
		Jdbi jdbi = JDBIConnect.get();
		Map<Order, List<OrderDetail>> map = new LinkedHashMap<>();

		StringBuilder queryBuilder = new StringBuilder("SELECT * FROM orders WHERE 1=1");

		if (status != null && !status.isEmpty()) {
			queryBuilder.append(" AND status = :status");
		}
		if (fromDateStr != null && !fromDateStr.isEmpty()) {
			queryBuilder.append(" AND orderDate >= :fromDate");
		}
		if (toDateStr != null && !toDateStr.isEmpty()) {
			queryBuilder.append(" AND orderDate <= :toDate");
		}
		if (paymentMethod != null) {
			queryBuilder.append(" AND idPayment = :paymentMethod");
		}

		List<Order> orders = jdbi.withHandle(handle -> {
			var query = handle.createQuery(queryBuilder.toString());

			if (status != null && !status.isEmpty()) {
				query.bind("status", status);
			}
			if (fromDateStr != null && !fromDateStr.isEmpty()) {
				query.bind("fromDate", fromDateStr);
			}
			if (toDateStr != null && !toDateStr.isEmpty()) {
				query.bind("toDate", toDateStr);
			}
			if (paymentMethod != null) {
				query.bind("paymentMethod", paymentMethod);
			}

			return query.map((rs, ctx) -> {
				int orderId = rs.getInt("id");
				int userId = rs.getInt("idUser");
				double totalPrice = rs.getDouble("totalPrice");
				String orderDate = rs.getString("orderDate");
				String orderStatus = rs.getString("status");  // đổi tên biến để không trùng tên method
				int idPayment = rs.getInt("idPayment");
				int quantity = rs.getInt("quantity");
				String sign = rs.getString("sign");
				String verifyDate = rs.getString("verifyDate");
				User user = userDao.getUserbyid(userId);
				Payment payment = paymentDao.getPaymentbyid(idPayment);
				return new Order(orderId, user, totalPrice, orderDate, status, payment,verifyDate, quantity,sign);
			}).list();
		});

		for (Order order : orders) {
			List<OrderDetail> details = getDetailById(order.getId());
			map.put(order, details);
		}

		return map;
	}


}
