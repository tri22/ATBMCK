<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Giới thiệu - Rau củ sạch</title>
  <link rel="stylesheet" href="assets/css/style.css" />
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
          <div class="col-md-3">
            <div id="chonmaybom"></div>
            <div id="tintuc"></div>
          </div>
          <!-- phần thay đổi -->
          <div class="col-md-9">
            <div class="text-center">
              <h2 class="text-primary">Giới thiệu</h2>
              <h3 class="text-primary font-weight-bold">
                CÔNG TY TNHH RAU CỦ QUẢ HÀ NỘI XANH
              </h3>
            </div>

            <!-- Nội dung mô tả -->
            <div class="content mt-3">
              <h5><em>Lĩnh vực hoạt động:</em></h5>
              <p>
                Chúng tôi hoạt động trong lĩnh vực trồng trọt, thu hoạch và phân phối
                các loại rau củ quả sạch, an toàn: rau ăn lá, rau gia vị, củ quả theo mùa,
                trái cây hữu cơ… phục vụ nhu cầu tiêu dùng cá nhân, nhà hàng, siêu thị và các đơn vị chế biến thực phẩm.
                Nhiều sản phẩm được khách hàng tin dùng như
                <strong>
                  rau cải xanh, cải bó xôi, cà rốt, bí đỏ, cà chua, khoai lang, đậu bắp, rau muống, dưa chuột, xà lách…
                </strong>
              </p>
              <p>
                Đội ngũ nhân viên giàu kinh nghiệm và tâm huyết luôn đảm bảo chất lượng sản phẩm từ khâu sản xuất đến giao hàng.
                Chúng tôi cam kết cung cấp rau củ tươi mới mỗi ngày, được kiểm định nghiêm ngặt, bảo đảm an toàn vệ sinh thực phẩm.
              </p>
              <h5><em>Chúng tôi cam kết:</em></h5>
              <ul>
                <li>
                  <strong>
                    Cung cấp rau củ quả chất lượng cao với giá cả hợp lý đến tay người tiêu dùng.
                  </strong>
                </li>
                <li>
                  <strong>Đặt sự hài lòng của khách hàng làm trung tâm trong mọi hoạt động.</strong>
                </li>
              </ul>
            </div>

            <!-- Hình ảnh sản phẩm -->
            <div class="brand-logos text-center mt-4">
              <img
                      src="assets/imgs/nhataitro/rau-cu-qua.jpg"
                      alt="Rau củ quả"
                      class="img-fluid"
              />
            </div>
          </div>
        </div>

        <footer id="footer2"></footer>
        <div id="bought-product"></div>
      </div>
    </div>
  </div>
</div>
<footer id="footer"></footer>

<!-- Script Load JSP -->
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

<!-- Ajax xử lý biểu mẫu -->
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
