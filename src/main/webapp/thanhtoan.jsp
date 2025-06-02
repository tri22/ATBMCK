<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
                    <div class="col-md-12">
                        <div class="text-primary">
                            <h2 style="color: #162e5c !important;">Thông Tin Đơn Hàng và Vận Chuyển</h2>
                            <form id="orderForm" method="post" style="color: #162e5c !important;">
                                <div class="mb-3">
                                    <label class="form-label">Họ và Tên</label>
                                    <input type="text" class="form-control" id="fullName" value="${auth.fullname}"
                                           required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Số Điện Thoại</label>
                                    <input type="text" class="form-control" id="phone" value="${auth.phone}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" value="${auth.email}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Địa Chỉ Giao Hàng</label>
                                    <textarea class="form-control" id="address" rows="3"
                                              required>${auth.address}</textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Phương Thức Thanh Toán</label>
                                    <select class="form-select" name="paymentMethod" id="paymentMethod">
                                        <option value="1">Thanh toán khi nhận hàng</option>
                                        <option value="2">Chuyển khoản ngân hàng</option>
                                    </select>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <button type="submit" class="btn btn-success">Xác nhận đơn hàng</button>
                                    <a href="CartServlet" class="btn btn-secondary">Trở về giỏ hàng</a>
                                </div>
                            </form>
                            <div id="orderMessage" class="mt-3"></div>
                        </div>
                        <!-- Modal thêm chữ ký -->
                        <div class="modal fade " id="addSignatureModal" tabindex="-1"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header text-dark">
                                        <h5 class="modal-title">Thêm chữ ký cho đơn hàng</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Đóng"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form id="addSignatureForm" action="/DoAnLTWeb/PublicKeyServlet" method="post">
                                            <!-- Public Key input -->
                                            <div class="input-group mb-3">
                                                <span class="input-group-text">Chữ ký</span>
                                                <textarea type="text" class="form-control" name="signature"
                                                       id="signature-field" required > </textarea>
                                            </div>
                                            <div id="addSignatureMessage" class="mt-2"></div>
                                            <div class="d-grid gap-2 mt-3">
                                                <button type="submit" class="btn btn-primary"
                                                        style="display: block !important;">Xác nhận
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <footer id="footer2"></footer>
                        <div id="bought-product"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer id="footer"></footer>
<script>
    const header = document.getElementById("header");
    const footer2 = document.getElementById("footer2");
    const nav = document.getElementById("nav");
    const tintuc = document.getElementById("tintuc");
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
    $(document).ready(function () {
        let paymentMethod = null;

        // Bắt sự kiện submit form đơn hàng
        $('#orderForm').submit(function (event) {
            event.preventDefault(); // Không submit form gốc

            // Lấy phương thức thanh toán từ form
            paymentMethod = $('#paymentMethod').val(); // Đúng


            // Gửi AJAX để tạo dữ liệu cần ký và gửi mail
            $.ajax({
                url: '/DoAnLTWeb/PreOrder', // Servlet trả về dữ liệu cần ký + gửi mail
                method: 'POST',
                data: {
                    paymentMethod: paymentMethod
                },
                dataType: 'json',
                success: function (response) {
                    if (response.success) {

                        // Mở modal ký chữ ký
                        $('#addSignatureModal').modal('show');
                    } else {
                        alert("Không thể khởi tạo đơn hàng!");
                    }
                },
                error: function () {
                    alert("Lỗi kết nối server!");
                }
            });
        });

        // Khi người dùng nhấn "Xác nhận" trong modal ký
        $('#addSignatureForm').submit(function (event) {
            event.preventDefault();

            const signature = $('#signature-field').val();
            const messageDiv = $('#addSignatureMessage');
            let formData = new FormData(document.getElementById("orderForm"));
            formData.append("signature", signature);


            if (!signature) {
                messageDiv.html('<div class="alert alert-danger">Thiếu dữ liệu để ký!</div>');
                return;
            }

            $.ajax({
                url: "CheckoutServlet",
                method: "POST",
                data:formData ,
                processData: false,
                contentType: false,
                dataType: 'json',
                success: function (data) {
                    console.log(data)
                    if (data.success) {
                        messageDiv.html('<div class="alert alert-success">' + data.message + '</div>');

                    } else {
                        messageDiv.html('<div class="alert alert-danger">' + data.message + '</div>');

                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error:", error);
                    messageDiv.html('<div class="alert alert-danger">' + error + '</div>');

                }
            });
        });
    });

</script>
<script src="assets/js/nav.js"></script>
<script src="assets/js/boughtProduct.js"></script>
</body>
</html>
