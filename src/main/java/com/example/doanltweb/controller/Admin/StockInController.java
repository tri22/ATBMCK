package com.example.doanltweb.controller.Admin;

import com.example.doanltweb.utils.OrderUtils;
import com.example.doanltweb.utils.StockInUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.doanltweb.dao.ProductDao;
import com.example.doanltweb.dao.StockInDao;
import com.example.doanltweb.dao.model.Product;
import com.example.doanltweb.dao.model.StockIn;

@MultipartConfig
@WebServlet("/StockInController")
public class StockInController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StockInDao stockInDao = new StockInDao();
    ProductDao productDao = new ProductDao();   
	StockInUtils stockInUtils = new StockInUtils();
	OrderUtils orderUtils = new OrderUtils();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<StockIn> records = stockInDao.getAllRecord();
		Map<Product,Integer> stockInMap = stockInUtils.stockInRecord();
		Map<Product,Integer> detailMap = orderUtils.orderRecord();
		Map<Product, Integer> remainingMap = new HashMap<>();
		for (Map.Entry<Product, Integer> entry : stockInMap.entrySet()) {
			Product product = entry.getKey();
			int totalImported = entry.getValue();
			int totalSold = detailMap.getOrDefault(product, 0);
			int remaining = totalImported - totalSold;

			if (remaining > 0) {
				remainingMap.put(product, remaining);
			}
		}
		request.setAttribute("stockRemainList", remainingMap);
		request.setAttribute("records", records);
		request.getRequestDispatcher("stockIn.jsp").forward(request, response);
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8"); // đảm bảo nhận tiếng Việt
	    
	    try {
	        // Lấy dữ liệu từ form
	        int productId = Integer.parseInt(request.getParameter("productId"));
	        int quantity = Integer.parseInt(request.getParameter("quantity"));

	        Product product = productDao.getById(productId);
	        boolean s =stockInDao.newStockIn(quantity,product); 

	        response.setStatus(HttpServletResponse.SC_OK); // 200
	        response.getWriter().write("Thành công");

	    } catch (Exception e) {
	    	 e.printStackTrace();
	    	 response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
		     response.getWriter().write(e.getMessage());
	    }
	}


}
