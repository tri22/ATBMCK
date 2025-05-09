<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Document</title>
<link rel="stylesheet" href="assets/css/style.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
	crossorigin="anonymous" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="assets/css/Userprofilestyle.css">

<link rel="stylesheet" href="assets/css/headerAndFooter.css">
</head>

<body style="background-color: rgb(242, 244, 247)">
	<header id="header"></header>
	<nav id="nav"></nav>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="border p-3">
					<div class="row pt-2">
						<!-- Container chính -->
						<div class="container py-5">
							<div class="row justify-content-center">
								<div class="col-lg-8">
									<!-- Card chứa thông tin người dùng -->
									<div class="card p-4">
										<!-- Avatar và tên người dùng -->
										<div class="text-center mb-4">
											<div class="avatar mx-auto mb-3">
												<img src="data:image/jpeg;base64," alt="User Avatar">
											</div>
											<h3 class="fw-bold">${auth.fullname}</h3>
											<p class="text-muted">${auth.email}</p>
										</div>

										<!-- Form chi tiết người dùng -->
										<form id="userForm" action="DetailUserController"
											method="post">
											<div class="mb-3">
												<label class="form-label fw-bold">Họ và tên</label> <input
													type="text" class="form-control" name="fullname"
													id="fullname" value="${auth.fullname}" readonly>
											</div>

											<div class="mb-3">
												<label class="form-label fw-bold">Email</label> <input
													type="email" class="form-control" name="email" id="email"
													value="${auth.email}" readonly>
											</div>

											<div class="mb-3">
												<label class="form-label fw-bold">Địa chỉ</label> <input
													type="text" class="form-control" name="address"
													id="address" value="${auth.address}" readonly>
											</div>

											<div class="mb-3">
												<label class="form-label fw-bold">Số điện thoại</label> <input
													type="text" class="form-control" name="phone" id="phone"
													value="${auth.phone}" readonly>
											</div>

											<!-- Nút hành động -->
											<div class="d-flex justify-content-between mt-4">
												<a href="LogoutController"
													class="btn btn-outline-danger px-4">Logout</a>

												<!-- Nút Edit -->
												<button type="button" id="editBtn"
													class="btn bg-dark-blue px-4 text-light">Edit
													profile</button>

												<!-- Nút Submit (Ẩn ban đầu) -->
												<button type="submit" id="submitBtn"
													class="btn btn-success px-4" style="display: none;">Submit</button>
											</div>
										</form>

										<!-- Hiển thị thông báo -->
										<div id="message" class="mt-3"></div>

                                        <!-- Form đổi mật khẩu -->
                                        <div class="card p-4 mt-4">
                                            <h5 class="fw-bold mb-3">Đổi mật khẩu</h5>
                                            <form id="changePasswordForm">


                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Mật khẩu hiện tại</label>
                                                    <input type="password" class="form-control" name="currentPassword" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Mật khẩu mới</label>
                                                    <input type="password" class="form-control" name="newPassword" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Xác nhận mật khẩu mới</label>
                                                    <input type="password" class="form-control" name="confirmPassword" required>
                                                </div>
                                                <button type="submit" class="btn btn-primary mt-3">Đổi mật khẩu</button>

												<div id="changePasswordMessage" class="mt-3 mb-3"></div>
											</form>
                                        </div>

									</div>
								</div>
							</div>
						</div>

						<div class="container mt-2">
							<h2 class="mb-2">Đơn hàng của bạn</h2>
							<div class="table-responsive">
								<table class="table table-bordered text-center align-middle">
									<thead class="table-light">
										<tr>
											<th class="bg-dark-blue text-light">Ngày đặt hàng</th>
											<th class="bg-dark-blue text-light">Tổng số lượng</th>
											<th class="bg-dark-blue text-light">Tổng tiền</th>
											<th class="bg-dark-blue text-light">Trạng thái</th>
											<th class="bg-dark-blue text-light"></th>
										</tr>
									</thead>
									<tbody>

										<c:forEach var="entry" items="${orderMap}">
											<c:set var="order" value="${entry.key}" />
											<c:set var="details" value="${entry.value}" />

											<tr style="cursor: pointer;" class="table-row-main">
												<td>${order.orderDate}</td>
												<td onclick="toggleDetails(${order.id})">${order.user.fullname}</td>
												<td onclick="toggleDetails(${order.id})">${order.quantity}</td>
												<td onclick="toggleDetails(${order.id})">${order.totalPrice}</td>
												<td onclick="toggleDetails(${order.id})"
													data-order-id="${order.id}"><span
													id="status-${order.id}" class="badge text-dark">${order.status}</span>

												</td>
												<td>
													<c:if
														test="${order.status == 'PENDING' || order.status == 'VERIFIED'}">
														<button id="btn-${order.id}" class="btn btn-danger btn-sm"
															onclick="cancelOrder(${order.id})">Hủy đơn</button>
													</c:if> <c:if test="${order.status == 'PENDING'}">
														<button id="btn-confirm-${order.id}"
															onclick="showOtpModal(${order.id})"
															class="btn btn-success btn-sm">Xác nhận</button>
													</c:if>
												</td>
											</tr>
										</c:forEach>
										<!-- Modal dùng chung cho tất cả đơn hàng -->
										<div class="modal fade" id="otpModal" tabindex="-1"
											aria-labelledby="otpModalLabel" aria-hidden="true">
											<div class="modal-dialog">
												<div class="modal-content">
													<div class="modal-header">
														<h5 class="modal-title" id="otpModalLabel">Xác nhận
															đơn hàng</h5>
														<button type="button" class="btn-close"
															data-bs-dismiss="modal" aria-label="Đóng"></button>
													</div>
													<div class="modal-body">
														<form id="verifyOtpForm">
															<input type="hidden" class="form-control" name="orderId"
																id="orderId">
															<div class="mb-3">
																<label for="otp" class="form-label">Nhập OTP:</label> <input
																	type="text" class="form-control" id="otp" name="otp"
																	required>
															</div>
															<div id="verifyMessage" class="mt-2"></div>
															<div class="d-grid gap-2 mt-3">
																<button type="submit" class="btn btn-primary">Xác
																	nhận</button>
															</div>
														</form>
													</div>
												</div>
											</div>
										</div>

									</tbody>
								</table>
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
  const footer2 = document.getElementById("footer2");
  const nav = document.getElementById("nav");
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

	<script src="assets/js/nav.js"></script>
	<script src="assets/js/userProfile.js"></script>
	<script>
	function toggleDetails(orderId) {
		let detailsRow = document.getElementById("orderDetails-" + orderId);
		if (detailsRow) {
			detailsRow.classList.toggle("show");
		} else {
			console.error("Không tìm thấy phần tử orderDetails" + orderId);
		}
	}

	function showOtpModal(orderId) {
	    $('#otpModal').modal('show');
	    $('#otpModalLabel').text('Xác nhận đơn hàng #' + orderId); // Đổi title cho đẹp
	    $('#orderId').val(orderId); // Gán orderId vào hidden input
	}


	function closeForm(orderId) {
	    // Đóng modal OTP tương ứng với mỗi đơn hàng
	    $('#otpModal-' + orderId).modal('hide');
	}

	</script>
	<script>
	$(document).ready(function () {
	    $('#verifyOtpForm').submit(function (event) {
	        event.preventDefault();

	        var orderId = $('#orderId').val();
	        var otp = $('#otp').val();
	        var messageDiv = $('#verifyMessage');

	        $.ajax({
	            url: '/DoAnLTWeb/VerifyOrderServlet',
	            type: 'POST',
	            data: {
	                orderId: orderId,
	                otp: otp
	            },
	            success: function (data) {
	                if (typeof data === 'string') {
	                    data = JSON.parse(data);
	                }
	                if (data.success) {
	                    messageDiv.html('<div class="alert alert-success">' + data.message + '</div>');
	                    setTimeout(function () {
	                        $('#otpModal').modal('hide');
	                        messageDiv.html('');
	                    }, 1000);
	                 // Cập nhật trạng thái đơn hàng
	    				const elementId = 'status-' + orderId;
	    				const statusCell = document.getElementById(elementId);

	    				if (statusCell) {
	    					statusCell.textContent = "VERIFIED";
	    					$('#btn-confirm-' + orderId).hide();
	    				} else {
	    					console.warn(`Không tìm thấy phần tử với id: ${elementId}`);
	    				}
	                } else {
	                    messageDiv.html('<div class="alert alert-danger">' + data.message + '</div>');
	                }
	            },
	            error: function () {
	                messageDiv.html('<div class="alert alert-danger">Lỗi kết nối server!</div>');
	            }
	        });
	    });
	});

	</script>



</body>
</html>
