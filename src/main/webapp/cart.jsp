<%@ page import="com.example.doanltweb.dao.model.CartItem" %>
<%@ page import="com.example.doanltweb.dao.model.Cart" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Document</title>
    <link rel="stylesheet" href="assets/css/style.css"/>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
            rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
            crossorigin="anonymous"
    />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/giohang.css">
    <link rel="stylesheet" href="assets/css/headerAndFooter.css">
</head>

<body>
<header id="header"></header>
<nav id="nav"></nav>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="border p-3">
                
                <div class="row pt-3">
                    <!-- phan thay doi -->
                    <div class="col">                 
                        	<div class="container mt-2">
							<h2 class="mb-2">Giỏ hàng của bạn</h2>
							<div class="table-responsive">
								<table class="table table-bordered text-center align-middle">
									<thead class="table-light">
										<tr>
											<th class="bg-dark-blue text-light col-3">Hình ảnh</th>
											<th class="bg-dark-blue text-light col-3">Tên sản phẩm</th>
											<th class="bg-dark-blue text-light col-2">Đơn giá</th>
											<th class="bg-dark-blue text-light col-3">Số lượng</th>
											<th class="bg-dark-blue text-light col-1"></th>
										</tr>
									</thead>
									<tbody>
											<c:forEach items="${cart}" var="item">
												<tr id="cart-item-${item.product.id}" class="cart-item"
													data-price="${item.product.priceProduct}"
													data-product-id="${item.product.id}">
													<td><img src="assets/imgs/sanpham/${item.product.image }"
														class="img-fluid anhhang"
														style="width: 80px; height: auto;"></td>
													<td>
														<h5 class="mb-1">${item.product.nameProduct}</h5>
													</td>
													<td class="price-cell">${item.product.priceProduct}</td>
													<!-- Số lượng sản phẩm -->
													<td class="product-quantity">
														<div class="quantity-wrapper">
															<input id="quantity-edit-${item.id}" type="number"
																name="weight" class="quantity-form"
																value="${item.quantity}" min="1">
															<button type="button" class="btn-update"
																data-id="${item.product.id}">
																<i class="bi bi-check"></i>
															</button>
														</div>
													</td>
													<td>
														<button class="btn btn-danger btn-sm" onclick="removeFromCart(${item.product.id})">Xóa</button>
													</td>
												</tr>
											</c:forEach>


										</tbody>
								</table>
							</div>
						</div>

                        <!-- Tổng cộng và nút hành động -->
                        <div class=" align-items-center px-3">
                            <form action="/DoAnLTWeb/CheckoutServlet" method="get" class="text-end totals">
                                <p class="mb-1" id ="total-amount"><strong>Tổng số lượng:</strong>0</p>
                                <p class="mb-1" id ="total-price"><strong>Tổng tiền:</strong>0 đ</p>
								<input id="total-quantity-input" type="hidden"
										name="total-quantity" class="quantity-form" value="${TotalAmount}"
										min="1"> 
								<input id="total-price-input" type="hidden"
										name="total-price" class="quantity-form" value="${TotalPrice}"
										min="1">
									<button  type="submit" class="btn btn-success mt-2">
                                    Tiến hành thanh toán
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <footer id="footer2"></footer>
            </div>
        </div>
    </div>
</div>
<footer id="footer"></footer>
<script>
    const header = document.getElementById("header");
    const footer = document.getElementById("footer");
    const header2 = document.getElementById("header2");
    const footer2 = document.getElementById("footer2");
    const nav = document.getElementById("nav");
    const tintuc = document.getElementById("tintuc");
    const chonmaybom = document.getElementById("chonmaybom");
    fetch("./assets/component/header.jsp")
        .then((response) => response.text())
        .then((html) => (header.innerHTML = html));
    fetch("./assets/component/footer.jsp")
        .then((response) => response.text())
        .then((html) => (footer.innerHTML = html));
    fetch("./assets/component/footer2.jsp")
        .then((response) => response.text())
        .then((html) => (footer2.innerHTML = html));
    fetch("./assets/component/nav.jsp")
        .then((response) => response.text())
        .then((html) => (nav.innerHTML = html));
</script>
<script>
	$(document).ready(function () {
		$(document).on('submit', '#reportKeyForm', function (e) {
			e.preventDefault();
			$.ajax({
				url: $(this).attr('action'),
				type: 'POST',
				data: $(this).serialize(),
				dataType: 'json',
				success: function (data) {
					if (data.success) {
						$('#reportKeyMessage').html( '<div class="alert alert-success text-dark">' + data.message + '</div>');
					} else {
						$('#reportKeyMessage').html('<div class="alert alert-danger text-dark">' + data.message + '</div>');
					}
				},
				error: function () {
					$('#reportKeyMessage').html('<div class="alert alert-danger text-dark">Có lỗi xảy ra.</div>');
				}
			});
		});


	});
	$(document).ready(function () {
		// Cập nhật khóa
		$(document).on('submit', '#updateKeyForm', function (e) {
			e.preventDefault();
			$.ajax({
				url: '/DoAnLTWeb/PublicKeyServlet',
				type: 'POST',
				data: $(this).serialize(),
				dataType: 'json',
				success: function (data) {
					console.log(data)
					if (data.success) {
						$('#updateKeyMessage').html( '<div class="alert alert-success text-dark">' + data.message + '</div>');
					} else {
						$('#updateKeyMessage').html('<div class="alert alert-danger text-dark">' + data.message + '</div>');
					}
				},
				error: function (xhr, status, error) {
					$('#updateKeyMessage').html( '<div class="alert alert-danger text-dark">' + error + '</div>');
				}
			});
		});
	});
</script>
<script src="assets/js/nav.js"></script>
<script src="assets/js/cart.js"></script>
</body>
</html>
