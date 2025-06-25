package com.example.doanltweb.dao.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Product implements java.io.Serializable {
        private int id;
        private String nameProduct;
        private String image;
        private double priceProduct;
        private String description;
        private String manufactureDate;
        private int stock;
        private int idCategory;

    public Product() {
    }

        public Product(String nameProduct, String image, double priceProduct, String description, String manufactureDate, int stock, int idCategory) {
            this.nameProduct = nameProduct;
            this.image = image;
            this.priceProduct = priceProduct;
            this.description = description;
            this.manufactureDate = manufactureDate;
            this.stock = stock;
            this.idCategory = idCategory;

        }

        public int getId() {
            return id;
        }

//        public void setId(int id) {
//            this.id = id;
//        }
        public void setId(int id) {
            this.id = id;
        }


        public String getNameProduct() {
            return nameProduct;
        }

        public void setNameProduct(String nameProduct) {
            this.nameProduct = nameProduct;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public double getPriceProduct() {
            return priceProduct;
        }

        public void setPriceProduct(double priceProduct) {
            this.priceProduct = priceProduct;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getManufactureDate() {
            return manufactureDate;
        }

        public void setManufactureDate(String manufactureDate) {
            this.manufactureDate = manufactureDate;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(int idCategory) {
            this.idCategory = idCategory;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return id == product.id; // So sánh theo ID chẳng hạn
        }

        @Override
        public int hashCode() {
            return Objects.hash(id); // Hash dựa trên ID
        }


        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", nameProduct='" + nameProduct + '\'' +
                    ", image='" + image + '\'' +
                    ", priceProduct=" + priceProduct +
                    ", description='" + description + '\'' +
                    ", manufactureDate='" + manufactureDate + '\'' +
                    ", stock=" + stock +
                    ", idCategory=" + idCategory +
                    '}';
        }
    }

