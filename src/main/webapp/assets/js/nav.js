
document.addEventListener("DOMContentLoaded", function () {
    $.ajax({
        url: "/DoAnLTWeb/CategoryServlet",
        method: "GET",
        dataType: "json",
        success: function (data) {
            console.log("Dữ liệu nhận được:", data);

            let categoryList = document.getElementById("categoryList");
            if (!categoryList) {
                console.error("Không tìm thấy phần tử categoryList!");
                return;
            }

            categoryList.innerHTML = ""; // Xóa dữ liệu cũ nếu có

            data.forEach(category => {
                let li = document.createElement("li");
                li.className = "shop-list";
                li.innerHTML = '<a href="ShopController?categoryId=' + category.id + '" class="text-light">' +
                    category.categoryName +
                    '</a>';
                categoryList.appendChild(li);
            });

            console.log("Danh sách đã được cập nhật:", categoryList.innerHTML);
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi gọi API:", error);
        }
    });
});


$('#confirm-btn').click(function () {
    const email = $('#emailInput').val().trim();
    if (!email) {
        alert("Vui lòng nhập email.");
        return;
    }

    // Gửi Ajax đến servlet gửi OTP
    $.ajax({
        url: '/DoAnLTWeb/SendOtpServlet',
        type: 'POST',
        data: {email: email},
        success: function (response) {
            if (response.trim() === 'success') {
                // Hiện OTP + đổi giao diện
                $('#otp-group').removeClass('d-none');
                $('#confirm-btn').addClass('d-none');
                $('#submit-otp-btn').removeClass('d-none');
                $('#step-text').text("Nhập mã OTP đã gửi đến email của bạn.");
            } else {
                alert("Không thể gửi OTP. Vui lòng thử lại.");
            }
        },
        error: function () {
            alert("Có lỗi xảy ra khi gửi OTP.");
        }
    });
});

