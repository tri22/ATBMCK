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


                <div class="row ">
                    <div class="col-md-3">
                        <div id="chonmaybom"></div>
                        <div id="tintuc"></div>
                    </div>
                    <!-- phan thay doi -->
                    <div class="col-md-9">
                        <h3 class="card-title text-center py-3">Tin Tức Nổi Bật</h3>
                        <ul class="list-unstyled">
                            <!-- tin1 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="tintuchot.jsp">
                                            <img src="assets/imgs/sanpham/product-1.jpg" alt="Cách chọn rau sạch" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>5 mẹo chọn rau củ tươi sạch đúng chuẩn</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 12/06/2024 14:30</p>
                                        <p>Làm sao để phân biệt rau sạch và rau nhiễm hóa chất? Bài viết này sẽ hướng dẫn bạn cách chọn rau củ đúng mùa, tươi ngon, an toàn cho sức khỏe gia đình.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- tin2 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="#">
                                            <img src="assets/imgs/sanpham/product-6.jpg" alt="Lợi ích của cải bó xôi" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>Bông cải xanh – Thực phẩm vàng cho sức khỏe</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 10/05/2024 10:15</p>
                                        <p>Bông cải xanh không chỉ giàu vitamin mà còn hỗ trợ tim mạch, tăng cường hệ miễn dịch. Hãy thêm bông cải xanh vào thực đơn hàng tuần của bạn nhé!</p>
                                    </div>
                                </div>
                            </div>

                            <!-- tin3 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="#">
                                            <img src="assets/imgs/sanpham/product-10.jpg" alt="Bảo quản rau củ" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>Hướng dẫn bảo quản rau củ luôn tươi ngon</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 02/05/2024 09:40</p>
                                        <p>Tủ lạnh không phải lúc nào cũng là cách tốt nhất! Khám phá các phương pháp bảo quản từng loại rau củ để giữ được chất lượng lâu dài.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- tin4 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="#">
                                            <img src="assets/imgs/sanpham/product-3.jpg" alt="Rau theo mùa" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>Ăn rau đúng mùa – Bí quyết sống khỏe</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 21/04/2024 17:22</p>
                                        <p>Việc chọn rau củ đúng mùa giúp tăng cường dưỡng chất, ít hóa chất và ngon miệng hơn. Xem ngay danh sách rau củ nên ăn trong tháng này.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- tin5 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="#">
                                            <img src="assets/imgs/sanpham/product-5.jpg" alt="Ăn chay với rau củ" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>Thực đơn ăn chay với rau củ đủ chất</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 15/04/2024 08:30</p>
                                        <p>Gợi ý các món chay từ rau củ dễ nấu, dễ ăn và giàu dinh dưỡng – phù hợp cho người muốn ăn thanh đạm nhưng vẫn đảm bảo năng lượng.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- tin6 -->
                            <div class="container border-bottom border-top">
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <a href="#">
                                            <img src="assets/imgs/sanpham/product-7.jpg" alt="Giá trị dinh dưỡng của cà rốt" class="img-fluid" style="max-width: 200px;">
                                        </a>
                                    </div>
                                    <div class="col">
                                        <h6>Cà rốt – Nguồn vitamin A tự nhiên tuyệt vời</h6>
                                        <p><i class="fa-solid fa-calendar" style="color: #76797f;"></i> 03/04/2024 11:45</p>
                                        <p>Không chỉ tốt cho mắt, cà rốt còn hỗ trợ giảm cân và làm đẹp da. Tìm hiểu thêm những công dụng đặc biệt của loại củ quen thuộc này.</p>
                                    </div>
                                </div>
                            </div>
                        </ul>
                    </div>

                    <footer id="footer2"></footer>
                    <div id="bought-product"></div>
                </div>
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
        fetch("./assets/component/header2.jsp")
            .then((response) => response.text())
            .then((html) => (header2.innerHTML = html));
        fetch("./assets/component/nav.jsp")
            .then((response) => response.text())
            .then((html) => (nav.innerHTML = html));
        fetch("./assets/component/tintuc.jsp")
            .then((response) => response.text())
            .then((html) => (tintuc.innerHTML = html));
        fetch("./assets/component/chonmaybom.jsp")
            .then((response) => response.text())
            .then((html) => (chonmaybom.innerHTML = html));
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
	<script src="assets/js/boughtProduct.js"></script>
</body>
</html>
