package com.example.doanltweb.dao;

import com.example.doanltweb.dao.db.JDBIConnect;
import com.example.doanltweb.dao.model.Product;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDao {
    static Map<Integer, Product> data = new HashMap<>();
    private Jdbi jdbi;

    public ProductDao() {
        this.jdbi = JDBIConnect.get(); // Kết nối JDBI từ lớp JDBIConnect của bạn
    }

    public List<Product> getAll() {
        Jdbi jdbi = JDBIConnect.get();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM product")
                        .map((rs, ctx) -> {
                            Product product = new Product();
                            product.setId(rs.getInt("id"));
                            product.setNameProduct(rs.getString("nameProduct"));
                            product.setImage(rs.getString("image"));
                            product.setPriceProduct(rs.getDouble("priceProduct"));
                            product.setDescription(rs.getString("description"));
                            product.setManufactureDate(rs.getString("manufactureDate"));
                            product.setStock(rs.getInt("stock"));
                            product.setIdCategory(rs.getInt("idCategory"));
                            return product;
                        })
                        .list());
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (nameProduct, image, priceProduct, description, manufactureDate, power, " +
                "pressure, flowRate, pipeDiameter, voltage, brand, warrantyMonths, stock, idCategory, idSupplier) " +
                "VALUES (:nameProduct, :image, :priceProduct, :description, :manufactureDate, :power, :pressure, " +
                ":flowRate, :pipeDiameter, :voltage, :brand, :warrantyMonths, :stock, :idCategory, :idSupplier)";

        try {
            // Thêm sản phẩm vào cơ sở dữ liệu
            int rowsAffected = jdbi.withHandle(handle -> {
                return handle.createUpdate(sql)
                        .bind("nameProduct", product.getNameProduct())
                        .bind("image", product.getImage())
                        .bind("priceProduct", product.getPriceProduct())
                        .bind("description", product.getDescription())
                        .bind("manufactureDate", product.getManufactureDate())
                        .bind("stock", product.getStock())
                        .bind("idCategory", product.getIdCategory())
                        .execute(); // Thực thi câu lệnh SQL INSERT
            });

            if (rowsAffected > 0) {
                System.out.println("Sản phẩm đã được thêm thành công!");
                return true; // Thêm sản phẩm thành công
            } else {
                System.out.println("Không có dòng nào bị ảnh hưởng, thêm sản phẩm thất bại.");
                return false; // Không có dòng nào bị ảnh hưởng
            }
        } catch (Exception e) {
            // In ra thông báo lỗi
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi ra console
            return false;
        }
    }

    // Hàm cập nhật sản phẩm
    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET nameProduct = :nameProduct, image = :image, priceProduct = :priceProduct, " +
                "description = :description, manufactureDate = :manufactureDate, power = :power, pressure = :pressure, " +
                "flowRate = :flowRate, pipeDiameter = :pipeDiameter, voltage = :voltage, brand = :brand, " +
                "warrantyMonths = :warrantyMonths, stock = :stock, idCategory = :idCategory, idSupplier = :idSupplier " +
                "WHERE id = :id";

        try {
            int rowsAffected = jdbi.withHandle(handle ->
                    handle.createUpdate(sql)
                            .bind("id", product.getId())
                            .bind("nameProduct", product.getNameProduct())
                            .bind("image", product.getImage())
                            .bind("priceProduct", product.getPriceProduct())
                            .bind("description", product.getDescription())
                            .bind("manufactureDate", product.getManufactureDate())
                            .bind("stock", product.getStock())
                            .bind("idCategory", product.getIdCategory())
                            .execute()
            );

            if (rowsAffected > 0) {
                System.out.println("Sản phẩm đã được cập nhật thành công!");
                return true;
            } else {
                System.out.println("Không có dòng nào bị ảnh hưởng, cập nhật thất bại.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Hàm xóa sản phẩm
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = :id";

        try {
            int rowsAffected = jdbi.withHandle(handle ->
                    handle.createUpdate(sql)
                            .bind("id", id)
                            .execute()
            );

            if (rowsAffected > 0) {
                System.out.println("Sản phẩm đã được xóa thành công!");
                return true;
            } else {
                System.out.println("Không có dòng nào bị ảnh hưởng, xóa thất bại.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public Product getById(int id) {
        Jdbi jdbi = JDBIConnect.get();
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM product WHERE id = :id")
                .bind("id", id)
                .mapToBean(Product.class).findOne().orElse(null));
    }

    public List<Product> getSaleProduct() {
        Jdbi jdbi = JDBIConnect.get();
        String query = "SELECT * FROM product " +
                "WHERE id IN (SELECT idProduct FROM sale WHERE status = 1)";
        return jdbi.withHandle(handle -> {
            Query q = handle.createQuery(query);
            return q.mapToBean(Product.class).list();
        });
    }

    public List<Product> getProductBySupplier(int supplierId) {
        Jdbi jdbi = JDBIConnect.get();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM product WHERE idSupplier = :supplierid")
                        .bind("supplierid", supplierId)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public List<Product> getProductsByPageAndSort(int offset, int pageSize, String sort) {
        Jdbi jdbi = JDBIConnect.get();

        String orderByClause;
        if ("priceHighToLow".equals(sort)) {
            orderByClause = "ORDER BY priceProduct DESC";
        } else if ("priceLowToHigh".equals(sort)) {
            orderByClause = "ORDER BY priceProduct ASC";
        } else if ("newest".equals(sort)) {
            orderByClause = "ORDER BY manufactureDate DESC"; // Vì bảng bạn chưa có createdDate
        } else {
            orderByClause = "ORDER BY id ASC"; // Mặc định
        }

        String sql = "SELECT id, nameProduct, priceProduct, image AS imageProduct FROM product "
                + orderByClause + " LIMIT :limit OFFSET :offset";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .map((rs, ctx) -> {
                            Product p = new Product();
                            p.setId(rs.getInt("id"));
                            p.setNameProduct(rs.getString("nameProduct"));
                            p.setPriceProduct(rs.getDouble("priceProduct"));
                            p.setImage(rs.getString("imageProduct"));
                            return p;
                        })
                        .list()
        );
    }
    public int getTotalProducts() {
        Jdbi jdbi = JDBIConnect.get();
        String sql = "SELECT COUNT(*) FROM product";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<String> searchProductsstr(String query) {
        Jdbi jdbi = JDBIConnect.get(); // dùng kết nối có sẵn
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT nameProduct FROM product WHERE nameProduct LIKE :query LIMIT 5")
                        .bind("query", "%" + query + "%")
                        .mapTo(String.class)
                        .list()
        );
    }
    public List<Product> searchProducts(String keyword) {
        Jdbi jdbi = JDBIConnect.get();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM product WHERE nameProduct LIKE :keyword")
                        .bind("keyword", "%" + keyword + "%")
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();
        System.out.println(productDao.getAll());

    }


}





