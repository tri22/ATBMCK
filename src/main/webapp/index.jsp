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
    <link rel="stylesheet" href="assets/css/card.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
            crossorigin="anonymous"
    />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="assets/css/headerAndFooter.css">
</head>
<body style="background-color: rgb(242 244 247)">
<header id="header"></header>
<nav id="nav"></nav>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="border p-3">
                <div class="row">
                    <c:forEach items="${sall}" var="s">
                        <div class="col-md-2">
                            <div class="card position-relative themenu">
                                <div class="card-body">
                                    <a href="maybomtheohang?id_supplier=${s.id}"><img src="assets/imgs/nhataitro/${s.image}"
                                                                  class=" img-fluid rounded"> </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <!-- phan thay doi -->
                <div class="" row pt-3>
                    <div class="col-md">
                        <!-- may bom khuyen mai  -->
                        <div class="container my-4" style="background-color:#162e5c">
                            <div class="row">
                                <div class="text-center">
                                    <img src="assets/imgs/khac/khuyenmai.png" class="img-fluid rounded">
                                </div>
                            </div>
                            <br>
                            <div class="row g-4">
									<c:forEach items="${pall}" var="p" end="3">
										<div class="col-md-3">
											<div id="isReload"></div>
											<a href="chitietsanpham?id=${p.id}"
												style="text-decoration: none">
												<div class="card position-relative">
													<div class="discount-badge">-10%</div>
													<img src="assets/imgs/maybom/${p.image}"
														class="card-img-top" alt="Bơm tăng áp mini Pamtex 10" />
													<div class="card-body themaybom" style="height: 200px">
														<h6 class="card-title">${p.nameProduct}</h6>
														<p class="old-price">Giá cũ: 720.000đ</p>
														<p class="new-price">Giá mới: 650.000đ</p>
														<c:if test="${p.stock != 0}">
															<div class="option">
																<div class="wrap-option">
																	<%-- <form class="addToCartForm">
																		<input type="hidden" name="productId" value="${ p.getId()}"/>
																		<input type="hidden" name="quantity" value=1 />
																		<button type="submit" class="icon-cart">
																			<i class="bi-cart4"></i>
																		</button>
																	</form> --%>

																	<a href="#" class="icon-like"> <i
																		class="bi bi-cash-stack"></i>
																	</a>

																</div>
															</div>
														</c:if>
													</div>
												</div>
											</a>
										</div>
									</c:forEach>
									<!-- Add more products as needed -->
                            </div>
                        </div>
                        <!-- may bom khuyen mai  -->
                        <div class="container my-4">
                            <div class="row">
                                <h4>Máy bơm nước các loại</h4>
                            </div>
                            <div class="row mb-4">
                                <div class="col-md-auto">
                                    <p>Sắp xếp theo</p>
                                </div>
                                <div class="col-2">
                                    <select class="form-control form-select-sm">
                                        <option selected>Mặc định</option>
                                        <option>Giá cao đến thấp</option>
                                        <option>Giá thấp đến cao</option>
                                        <option>Mới nhất</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row g-4">
                               <c:forEach items="${pall}" var="p" end="7">
										<div class="col-md-3 ">
											<div id="isReload"></div>
											<a href="chitietsanpham?id=${p.id}"
												style="text-decoration: none">
												<div class="card position-relative">
													<div class="discount-badge">-10%</div>
													<img src="assets/imgs/maybom/${p.image}"
														class="card-img-top" alt="Bơm tăng áp mini Pamtex 10" />
													<div class="card-body themaybom" style="height: 200px">
														<h6 class="card-title">${p.nameProduct}</h6>
														<p class="old-price">Giá cũ: 720.000đ</p>
														<p class="new-price">Giá mới: 650.000đ</p>
														<c:if test="${p.stock != 0}">
															<div class="option">
																<div class="wrap-option">
																	<form class="addToCartForm">
																		<input type="hidden" name="productId" value="${ p.getId()}"/>
																		<input type="hidden" name="quantity" value=1 />
																		<button type="submit" class="icon-cart">
																			<i class="bi-cart4"></i>
																		</button>
																	</form>

																	<a href="#" class="icon-like"> <i
																		class="bi bi-cash-stack"></i>
																	</a>

																</div>
															</div>
														</c:if>
													</div>
												</div>
											</a>
										</div>
									</c:forEach>

                                <!-- Add more products as needed -->
                            </div>
                            <br>
                            <div class="row justify-content-center">
                                <div class="col-4 ">
                                    <button class="form-control hover-shadow">Xem thêm</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer id="footer2"></footer>
            <div id="bought-product"></div>
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
    const chonmaybom = document.getElementById("chonmaybom");
    const boughtProduct = document.getElementById("bought-product");
    fetch("./assets/component/boughtProduct.jsp")
    	.then((response) => response.text())
    	.then((html) => (boughtProduct.innerHTML = html));
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
    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".addToCartForm").forEach(form => {
            form.addEventListener("submit", function (event) {
                event.preventDefault(); // Ngăn reload

                var formData = new FormData(form); // Lấy dữ liệu từ form

                $.ajax({
                    url: "/DoAnLTWeb/AddToCartServlet",
                    method: "POST",
                    data: formData,
                    processData: false, // Bắt buộc khi dùng FormData
                    contentType: false, // Bắt buộc khi dùng FormData
                    success: function (data) {
                        if (data.status === "success") {
                            alert(data.message);
                        } else {
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("Có lỗi xảy ra, vui lòng thử lại!");
                        console.error("Lỗi:", error);
                    }
                });
            });
        });
    });
</script>
<script src="assets/js/nav.js"></script>
<script src="assets/js/boughtProduct.js"></script>
</body>
</html>
