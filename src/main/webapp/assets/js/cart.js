
    // Hàm tính tổng số lượng và tổng tiền
    function recalcTotals() {
        let totalQuantity = 0;
        let totalPrice = 0;

        const cartItems = document.querySelectorAll(".cart-item"); // Lấy tất cả các cart-item

        cartItems.forEach(item => {
            const qtyInput = item.querySelector("input.quantity-form");
            if (!qtyInput) return; // Nếu không có input số lượng thì bỏ qua phần tử này

            const quantity = parseInt(qtyInput.value);
            const price = parseFloat(item.dataset.price);

            if (!isNaN(quantity) && !isNaN(price)) {
                totalQuantity += quantity;
                totalPrice += quantity * price;
            }
        });

        // Cập nhật lại tổng số lượng và tổng tiền trên UI
        updateTotals(totalQuantity, totalPrice);
    }

    // Hàm cập nhật tổng số lượng và tổng tiền lên UI
    function updateTotals(totalQuantity, totalPrice) {
        document.getElementById("total-amount").innerHTML = '<strong>Tổng số lượng:</strong>' + totalQuantity;
        document.getElementById("total-price").innerHTML = '<strong>Tổng tiền:</strong> ' + totalPrice.toLocaleString() + ' đ';
    }

    // Gán sự kiện 'change' cho các input số lượng
    const quantityInputs = document.querySelectorAll("input.quantity-form");
    quantityInputs.forEach(input => {
        input.addEventListener("change", recalcTotals);
    });

    recalcTotals(); // Cập nhật tổng ban đầu

	// Lấy tất cả các nút cập nhật
	const updateButtons = document.querySelectorAll(".btn-update");

	updateButtons.forEach(btn => {
	    btn.addEventListener("click", function () {
	        const productId = this.dataset.id;
	        const quantityInput = this.closest(".quantity-wrapper").querySelector("input.quantity-form");

	        if (!quantityInput) {
	            alert("❌ Không tìm thấy input số lượng!");
	            return;
	        }

	        const newQuantity = parseInt(quantityInput.value);
	        if (isNaN(newQuantity) || newQuantity < 1) {
	            alert("❌ Số lượng không hợp lệ! Vui lòng nhập số hợp lệ.");
	            return;
	        }

	        // Gửi yêu cầu AJAX để cập nhật giỏ hàng
	        $.ajax({
	            url: "/DoAnLTWeb/CartServlet",
	            type: "POST",
	            contentType: "application/x-www-form-urlencoded",
	            data: {
	                productId: productId,
	                quantity: newQuantity,
	                action: "update"
	            },
	            success: function(responseData) {
	                if (responseData.status === "success") {
	                    alert("✅ Cập nhật giỏ hàng thành công!");
	                    recalcTotals(); // Cập nhật lại tổng sau khi cập nhật giỏ hàng
	                } else {
	                    alert(responseData.message);
	                }
	            },
	            error: function(xhr, status, error) {
	                alert("❌ Có lỗi xảy ra, vui lòng thử lại!");
	                console.error("Lỗi:", error);
	            }
	        });
	    });
	});


// Hàm tính tổng số lượng và tổng tiền sau khi cập nhật giỏ hàng
function recalcTotals() {
    let totalQuantity = 0;
    let totalPrice = 0;

    // Lấy tất cả các phần tử giỏ hàng
    const cartItems = document.querySelectorAll(".cart-item");

    // Nếu không có sản phẩm trong giỏ hàng
    if (cartItems.length === 0) {
        updateTotals(totalQuantity, totalPrice);  // Cập nhật lại thông tin giỏ hàng rỗng
        return;
    }

    cartItems.forEach(item => {
        // Lấy số lượng từ input
        const qtyInput = item.querySelector("input.quantity-form");
        const quantity = parseInt(qtyInput.value);

        // Lấy giá sản phẩm từ thuộc tính data-price của hàng
        const price = parseFloat(item.dataset.price);

        totalQuantity += quantity;
        totalPrice += quantity * price;
    });

    // Cập nhật lại tổng số lượng và tổng tiền lên UI
    updateTotals(totalQuantity, totalPrice);
}

// Hàm cập nhật tổng số lượng và tổng tiền lên UI
function updateTotals(totalQuantity, totalPrice) {
    const totalAmountElem = document.getElementById("total-amount");
    const totalPriceElem = document.getElementById("total-price");

    // Kiểm tra xem phần tử có tồn tại không
    if (totalAmountElem && totalPriceElem) {
        totalAmountElem.innerHTML = '<strong>Tổng số lượng:</strong>' + totalQuantity;
        totalPriceElem.innerHTML = '<strong>Tổng tiền:</strong> ' + totalPrice.toLocaleString() + ' đ';
    }
}


// Hàm xoá sản phẩm khỏi giỏ hàng
function removeFromCart(productId) {
    $.ajax({
        url: '/DoAnLTWeb/RemoveFromCartServlet',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        data: { productId: productId },
        success: function(response) {
            // Tìm phần tử trong DOM và xoá
            $('#cart-item-' + productId).remove();
            recalcTotals(); // Cập nhật lại tổng sau khi xoá sản phẩm
            alert(response);
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi xoá:", error);
            alert("❌ Có lỗi xảy ra khi xoá sản phẩm!");
        }
    });
}
