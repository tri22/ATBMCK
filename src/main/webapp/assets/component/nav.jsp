<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "sql" uri = "http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix = "x" uri = "http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<!-- Bắt đầu header  -->
<div id="header">
	<div
		class="container d-flex justify-content-between align-items-center py-4">
		<a href="/DoAnLTWeb/trangchu" class="head-title">VeggieHome</a>
		<form action="Search" method="get" class="d-flex">
			<input name="search" type="search" class="form-control me-2 "
				placeholder="Search..." style="width: 300px"> <input
				type="submit" value="Search" class="btn bg-dark-blue text-light">
		</form>

		<button class="btn">
			<i class="bi-list"></i> Menu
		</button>
	</div>
	<div
		class=" d-flex justify-content-between align-items-center bg-dark-blue ">
		<div class="container list-display-none  text-light">
			<ul class="list-head2 mb-0 d-flex justify-content-between px-0">
				<li class="list-item-head2 "><a href="trangchu"
					class="nav-link text-light">Trang chủ</a></li>

				<li class="list-item-head2 shop-active position-relative"><a
					class="nav-link shop text-light">Các sản phẩm <i
						class="bi-caret-down-fill"></i></a>
					<ul id="categoryList" class="wrap-shop-list bg-dark-blue">
						
					</ul>
				</li>
				<li class="list-item-head2"><a href="gioithieu.jsp"
					class="nav-link text-light">Giới thiệu dịch vụ</a>
				</li>
				<li class="list-item-head2"><a href="new.jsp"
					class="nav-link text-light">Tin tức</a>
				</li>
				<li class="list-item-head2"><a href="lienhe.jsp"
					class="nav-link text-light">Liên hệ</a>
				</li>

				<li class="list-item-head2">
					<a href="/DoAnLTWeb/CartServlet" class="nav-link icon-cart text-light"> <i class="bi-cart 	"></i></a>
				</li>

				<c:if test="${not empty sessionScope.auth}">
					<c:choose>
						<c:when test="${sessionScope.auth.idPermission == 1}">
							<li class="list-item-head2"><a href="admin.jsp"
								class="nav-link text-light"> ${sessionScope.auth.username} </a>
							</li>
						</c:when>
						<c:otherwise>
							<li class="list-item-head2 dropdown">
								<a class="nav-link dropdown-toggle text-light" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
										${sessionScope.auth.username}
								</a>
								<ul class="dropdown-menu">
									<li><a class="dropdown-item" href="/DoAnLTWeb/UserProfileServlet">Trang cá nhân</a></li>
									<li>
										<a data-bs-toggle="modal" data-bs-target="#reportKeyModal" class="dropdown-item" href="#">Báo mất khóa</a>
									</li>
									<li>
										<a data-bs-toggle="modal" data-bs-target="#upadateKeyModal" class="dropdown-item" href="#">Cập nhật khóa</a>

									</li>
								</ul>
							</li>

							<!-- Modal báo mất khóa -->
							<div class="modal fade" id="reportKeyModal" tabindex="-1" aria-labelledby="reportKeyModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<form id="reportKeyForm" action="/DoAnLTWeb/PublicKeyServlet" method="post">

										<div class="modal-header text-dark">
												<h5 class="modal-title" id="reportKeyModalLabel">Báo mất khóa</h5>
												<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
											</div>

											<div class="modal-body text-dark border-bottom">
												<p id="step-text">Bạn có chắc chắn muốn báo mất khóa không?</p>
												<!-- Email input -->
												<div class="input-group mb-3">
													<span class="input-group-text">Thời gian mất khóa </span>
													<input type="datetime-local" class="form-control" name="date" id="lostKeyTime" required>
												</div>
											</div>
											<div id="reportKeyMessage" class="mt-2"></div>
											<div class="d-grid gap-2 m-3">
												<button type="submit" class="btn btn-primary" style="display: block !important;">Xác nhận</button>
											</div>
										</form>
									</div>
								</div>
							</div>



							<!-- Modal cập nhật public mới -->
							<div class="modal fade" id="upadateKeyModal" tabindex="-1"
								  aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header text-dark">
											<h5 class="modal-title" >Cập nhật public key mới nhất</h5>
											<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
										</div>
										<div class="modal-body">
											<form id="updateKeyForm" action="/DoAnLTWeb/PublicKeyServlet" method="post">
												<!-- Public Key input -->
												<div class="input-group mb-3">
													<span class="input-group-text">Khóa Public mới </span>
													<input type="text" class="form-control" name="public-key" id="public-key-field" required>
												</div>
												<div id="updateKeyMessage" class="mt-2"></div>
												<div class="d-grid gap-2 mt-3">
													<button type="submit" class="btn btn-primary" style="display: block !important;">Xác nhận</button>
												</div>
											</form>
										</div>
									</div>
								</div>
							</div>
						</c:otherwise>

					</c:choose>
					<li class="list-item-head2"><a href="/DoAnLTWeb/LogoutController"
						class="nav-link text-light">Đăng xuất</a></li>
				</c:if>

				<c:if test="${empty sessionScope.auth}">
					<li class="list-item-head2"><a href="login.jsp"
						class="nav-link text-light">Đăng nhập</a></li>
				</c:if>

			</ul>
		</div>
	</div>
</div>
<!-- Kết thức header  -->
