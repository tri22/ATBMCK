  <%@ page contentType="text/html; charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

  <!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <link rel="stylesheet" href="assets/css/style.css" />
    <link rel="stylesheet" href="assets/css/chitietsanpham.css">
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
    <link rel="stylesheet" href="assets/css/giohang.css">
  </head>

  <body>
    <header id="header"></header> 
    <div id="header2"></div>
    <nav id="nav"></nav>
    <div class="container">
      <div class="row">
        <div class="col-md-12">
          <div class="border p-3">
           

            <div class="row pt-3">
              <!-- phan thay doi -->
              <div class="container mt-5">
                <div class="row">
                  <!-- Image Section -->
                  <div class="col-md-6">
                    <img src="assets/imgs/maybom/${product.image}"  alt="${product.nameProduct}" style="width: 550px;">
                  </div>
            
                  <!-- Product Info Section -->
                  <div class="col-md-6">
                    <h1 class="product-title">${product.nameProduct}</h1>
                    <p>
                      <span class="price">8.500.000 vnđ</span>
                      <span class="original-price">9.500.00 vnđ</span>
                    </p>
                    <div class="mb-2">
                          <span class="fa fa-star checked"></span>
                          <span class="fa fa-star checked"></span>
                          <span class="fa fa-star checked"></span>
                          <span class="fa fa-star checked"></span>
                          <span class="fa fa-star"></span>
                      </div>
                    <button class="btn buy-now-btn btn-lg bg-danger border-dark">Mua Ngay</button>
                      <a href="add?id=${product.id}">
                    <button  class="btn buy-now-btn btn-lg border-dark">Thêm vào giỏ hàng</button>
                      </a>
                    <hr>
                    <div>
                      <ul class="nav nav-tabs" id="productDetailsTab" role="tablist">
                        <li class="nav-item" role="presentation">
                          <button class="nav-link active" id="info-tab" data-bs-toggle="tab" data-bs-target="#info" type="button" role="tab">Thông tin</button>
                        </li>
                        <li class="nav-item" role="presentation">
                          <button class="nav-link" id="Describe-tab" data-bs-toggle="tab" data-bs-target="#Describe" type="button" role="tab">Mô tả</button>
                        </li>
                      </ul>
                      <div class="tab-content" id="productDetailsTabContent">
                        <div class="tab-pane fade show active" id="info" role="tabpanel"><p>Bạn đang tìm kiếm một chiếc máy bơm mạnh mẽ, bền bỉ và tiết kiệm năng lượng?
                            PAMTEX 10 chính là lựa chọn hoàn hảo! Với thiết kế hiện đại, công suất tối ưu và khả năng vận hành êm ái,
                            PAMTEX 10 đáp ứng mọi nhu cầu từ gia đình đến công nghiệp.</p>
                        <ul>
                            <li>Hiệu suất mạnh mẽ: Lưu lượng bơm lớn, đảm bảo cung cấp nước nhanh và ổn định.</li>
                            <li>Tiết kiệm điện năng: Thiết kế tối ưu giúp giảm chi phí vận hành.</li>
                            <li>Độ bền cao: Chất liệu cao cấp, chống ăn mòn, hoạt động bền bỉ trong thời gian dài.</li>
                            <li> Đa dạng ứng dụng: Phù hợp cho cả bơm nước sạch, tưới tiêu,
                                công trình xây dựng và nhiều lĩnh vực khác.</li>
                        </ul>
                        <p>Hãy để PAMTEX 10 giúp bạn tiết kiệm thời gian, công sức và chi phí!
                            Liên hệ ngay hôm nay để nhận ưu đãi đặc biệt!</p>
                            <p><strong>Xuất xứ:</strong> Hàn Quốc | <strong>Bảo hành:</strong> 12 tháng</p>
                        </div>
                        <div class="tab-pane fade" id="Describe" role="tabpanel"><table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Hạng mục</th>
                                    <th>Thông số kỹ thuật</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Công suất (W)</td>
                                    <td>${product.power}</td>
                                </tr>
                                <tr>
                                    <td>Lưu lượng (lít/phút)</td>
                                    <td>${product.pressure}</td>
                                </tr>
                                <tr>
                                    <td>Cột áp (m)</td>
                                    <td>${product.flowRate}</td>
                                </tr>
                                <tr>
                                    <td>Đường kính ống (mm)</td>
                                    <td>${product.pipeDiameter}</td>
                                </tr>
                                <tr>
                                    <td>Nguồn điện (V)</td>
                                    <td>220/50</td>
                                </tr>
                            </tbody>
                        </table></div>
                      </div>
                    </div>
                  </div>
                </div>
            
                <div class="my-4">
                    <h5>Sản phẩm liên quan</h5>
                    <div class="row">
                        <div class="col-md-3 col-sm-6 mb-4">
                            <div class="card">
                                <img src="assets\imgs\maybom\shizuko1.jpg" class="card-img-top" alt="Sản phẩm 1">
                                <div class="card-body text-center">
                                    <h6 class="card-title">Silstar SKD 100</h6>
                                    <p class="text-danger">800.000 đ</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 mb-4">
                            <div class="card">
                                <img src="assets\imgs\maybom\shizuko1.jpg" class="card-img-top" alt="Sản phẩm 2">
                                <div class="card-body text-center">
                                    <h6 class="card-title">Bơm tăng áp Seahan 150</h6>
                                    <p class="text-danger">950.000 đ</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 mb-4">
                            <div class="card">
                                <img src="assets\imgs\maybom\shizuko1.jpg" class="card-img-top" alt="Sản phẩm 3">
                                <div class="card-body text-center">
                                    <h6 class="card-title">Bơm tăng áp mini Kangaroo KG 150A</h6>
                                    <p class="text-danger">750.000 đ</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 mb-4">
                            <div class="card">
                                <img src="assets\imgs\maybom\shizuko1.jpg" class="card-img-top" alt="Sản phẩm 4">
                                <div class="card-body text-center">
                                    <h6 class="card-title">Silstar Luva 200A</h6>
                                    <p class="text-danger">850.000 đ</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
              <div class="row">
                    <div class="col border-top">
                        <h5 class="text-bold mt-2"> Đánh giá và bình luận</h5>
                        <div class="container border rating">
                            <p class="p-comment">Đánh giá và bình luận về máy bơm tăng áp Mini PAMTEX 10</p>
                            <div class="star-rating">
                                <input type="radio" id="5-stars" name="rating" value="5" />
                                <label for="5-stars" class="star">&#9733;</label>
                                <input type="radio" id="4-stars" name="rating" value="4" />
                                <label for="4-stars" class="star">&#9733;</label>
                                <input type="radio" id="3-stars" name="rating" value="3" />
                                <label for="3-stars" class="star">&#9733;</label>
                                <input type="radio" id="2-stars" name="rating" value="2" />
                                <label for="2-stars" class="star">&#9733;</label>
                                <input type="radio" id="1-star" name="rating" value="1" />
                                <label for="1-star" class="star">&#9733;</label>
                              </div>
                            <textarea class="form-control comment-area" rows="5" placeholder="Vui lòng nhập tiếng Việt có dấu. Nhập tối đa 255 kí tự"></textarea>
                            <button class="form-control btn-right com-btn justify-content-center"> Gửi đánh giá</button>
                        </div>
                      <div>
                            <h1>Bình luận</h1>
                                    <div class="mt-4 text-justify border border-top rounded" style="background-color: rgb(242 244 247)">
                                        <div class="m-2">
                                            <div class="d-flex float-left">
                                                <img src="assets/imgs/khac/anhdaidien.png" alt="" class="rounded-circle" width="40" height="40">
                                                <h4 class="m-2">User1</h4>
                                            </div>
                                            <span class="fa fa-star checked"></span>
                                            <span class="fa fa-star checked"></span>
                                            <span class="fa fa-star checked"></span>
                                            <span class="fa fa-star checked"></span>
                                            <span class="fa fa-star"></span>
                                            <p>Máy bơm rất tốt</p>
                                        </div>
                                    </div>
                                    <div class="mt-4 text-justify border border-top rounded" style="background-color: rgb(242 244 247)">
                                <div class="m-2">
                                    <div class="d-flex float-left">
                                        <img src="assets/imgs/khac/anhdaidien.png" alt="" class="rounded-circle" width="40" height="40">
                                        <h4 class="m-2">User2</h4>
                                    </div>
                                    <span class="fa fa-star checked"></span>
                                    <span class="fa fa-star checked"></span>
                                    <span class="fa fa-star checked"></span>
                                    <span class="fa fa-star checked"></span>
                                    <span class="fa fa-star"></span>
                                    <p>Máy bơm tốt</p>
                                    <p>Nhân viên tư vấn nhiệt tình</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                    <div id="tintuc"></div>
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
    <script src="assets/js/nav.js"></script>
	<script src="assets/js/boughtProduct.js"></script>
  </body>
</html>
