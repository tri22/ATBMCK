package com.example.doanltweb.dao;

import java.sql.ResultSet;
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
						String otp = rs.getString("otp");
	                    User user = userDao.getUserbyid(userId);
	                    Payment payment = paymentDao.getPaymentbyid(idPayment);
	                    return new Order(orderId, user, totalPrice, orderDate, status, payment, quantity,otp);
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
	                String otp = rs.getString("otp");
	                User user = userDao.getUserbyid(userId);
	                Payment payment = paymentDao.getPaymentbyid(idPayment);
	                return new Order(orderId, user, totalPrice, orderDate, status, payment, quantity, otp);
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
						  String otp = rs.getString("otp");
	                      User user = userDao.getUserbyid(userId);  // Lấy thông tin người dùng
	                      Payment payment = paymentDao.getPaymentbyid(idPayment);
	                      System.out.println(payment.getName());
	                      return new Order(orderId, user, totalPrice, orderDate, status, payment, quantity,otp);
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

	    public boolean createOrder(int userId, double totalPrice, int idPayment, int quantity, int cartId,String otp) {
	        Jdbi jdbi = JDBIConnect.get();

	        try {
	            return jdbi.inTransaction(handle -> {
	                // 1️⃣ Chèn đơn hàng mới và lấy ID đơn hàng
	                int orderId = handle.createUpdate(
	                        "INSERT INTO orders (idUser, totalPrice, orderDate, status, idPayment,quantity,otp) " +
	                        "VALUES (:userId, :totalPrice, NOW(), 'pending',:idPayment,:quantity,:otp)")
	                    .bind("userId", userId)
	                    .bind("totalPrice", totalPrice)
	                    .bind("idPayment", idPayment)
	                    .bind("quantity", quantity)
						.bind("otp",otp)
	                    .executeAndReturnGeneratedKeys("id")  
	                    .mapTo(Integer.class)
	                    .one();

	                // 2️⃣ Thêm các mục vào `order_details`
	                List<CartItem> cartItems = cartDao.getListCartItemByCartId(cartId); // Lấy giỏ hàng của user

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

	                // 3️⃣ Xóa giỏ hàng sau khi đặt hàng thành công
	                handle.createUpdate("DELETE FROM cart WHERE user_id = :userId")
	                    .bind("userId", userId)
	                    .execute();

	                return true; // ✅ Trả về true nếu đặt hàng thành công
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false; // ❌ Trả về false nếu có lỗi xảy ra
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


	public boolean verifyOrder(int orderId, String otp) {
		Jdbi jdbi = JDBIConnect.get();
		int rowsAffected = jdbi.withHandle(handle ->
				handle.createUpdate("UPDATE orders SET status = 'VERIFIED' WHERE id = :id AND otp = :otp AND orderDate > NOW() - INTERVAL 24 HOUR ")
						.bind("otp", otp)
						.bind("id", orderId)
						.execute());
		return rowsAffected>0;
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
	                  User user = userDao.getUserbyid(rs.getInt("idUser"));  // Lấy thông tin người dùng
                      Payment payment = paymentDao.getPaymentbyid(rs.getInt("idPayment"));
	                  order.setUser(user);
	                  order.setPaymentMethod(payment);

	                  return order;
	              })
	              .list()
	    );
	}


	




}
