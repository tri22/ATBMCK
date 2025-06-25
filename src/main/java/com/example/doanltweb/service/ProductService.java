package com.example.doanltweb.service;


import com.example.doanltweb.dao.ProductDao;
import com.example.doanltweb.dao.model.Product;

import java.util.List;

public class ProductService {
    private ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    // Thêm sản phẩm
    public boolean addProduct(Product product) {
        return productDao.addProduct(product);
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productDao.getAll();
    }

    public boolean updateProduct(Product product) {

        return productDao.updateProduct(product);
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        return productDao.deleteProduct(id);
    }


}
