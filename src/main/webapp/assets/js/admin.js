// quan li sp

function loadProductData() {
    if ($.fn.dataTable.isDataTable('#productTable')) {
        $('#productTable').DataTable().clear().destroy();
    }
    // Static mapping for category and supplier names (replace with dynamic data if necessary)
    var categories = {
        1: 'Máy bơm nước',
        2: 'Máy bơm chân không',
        3: 'Máy bơm ly tâm',
        4: 'Máy bơm định lượng',
        5: 'Máy bơm hóa chất',
        6: 'Máy bơm chìm',
        7: 'Máy bơm nước sạch',
        8: 'Máy bơm công nghiệp'
    };

    var suppliers = {
        1: 'Công ty A',
        2: 'Công ty B',
        3: 'Công ty C',
        4: 'Công ty D',
        5: 'Công ty E',
        6: 'Công ty F',
        7: 'Công ty G',
        8: 'Công ty H',
        9: 'Công ty I',
        10: 'Công ty J'
    };
    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/productController', // API của bạn
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data); // Kiểm tra dữ liệu trả về
            var tableBody = $('#productBody');
            tableBody.empty(); // Xóa dữ liệu cũ

            data.forEach(function (product) {

                // Get category and supplier names based on their IDs
                var categoryName = categories[product.idCategory] || 'N/A';
                var supplierName = suppliers[product.idSupplier] || 'N/A';

                var row = `<tr>
                        <td>${product.id}</td>
                        <td style="min-width: 70px;">${product.nameProduct}</td>
                        <td><img src="/assets/imgs/maybom/${product.image}" alt="Product Image" style="width: 60px; height: 60px;"></td>
                        <td>${product.priceProduct}</td>
                        <td style="min-width: 150px;">${product.description}</td>
                        <td style="min-width: 90px;">${product.manufactureDate}</td>
                        <td>${product.power}</td>
                        <td>${product.pressure}</td>
                        <td>${product.flowRate}</td>
                        <td>${product.pipeDiameter}</td>
                        <td>${product.voltage}</td>
                        <td>${product.brand}</td>
                        <td>${product.warrantyMonths}</td>
                        <td>${product.stock}</td>               
                    
                        <td style="min-width: 100px;" data-category-id="${product.idCategory}">${categoryName}</td> <!-- Display category name -->
                        <td style="min-width: 70px;" data-supplier-id="${product.idSupplier}">${supplierName}</td> <!-- Display supplier name -->
                        <td>
                            <div class="d-flex gap-2 justify-content-center">
                                <button class="btn btn-sm btn-primary product-edit-btn" data-id="${product.id}">Sửa</button>
                                <button class="btn btn-sm btn-danger product-delete-btn" data-id="${product.id}">Xóa</button>
                            </div>
                        </td>
                    </tr>`;
                tableBody.append(row);
            });

            // Destroy bảng cũ nếu có
            if ($.fn.DataTable.isDataTable('#productTable')) {
                $('#productTable').DataTable().destroy();
            }

            // Khởi tạo DataTable với tiếng Việt
            $('#productTable').DataTable({
                "paging": true,
                "searching": true,
                "ordering": true,
                "lengthChange": true,
                "language": {
                    "lengthMenu": "Hiển thị _MENU_ sản phẩm mỗi trang",
                    "zeroRecords": "Không tìm thấy sản phẩm nào",
                    "info": "Hiển thị _START_ đến _END_ của _TOTAL_ sản phẩm",
                    "infoEmpty": "Không có sản phẩm nào",
                    "infoFiltered": "(lọc từ _MAX_ sản phẩm)",
                    "search": "Tìm kiếm:",
                    "paginate": {
                        "first": "Đầu",
                        "last": "Cuối",
                        "next": "Sau",
                        "previous": "Trước"
                    },
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Lỗi lấy dữ liệu sản phẩm: " + error);
            console.log(xhr.responseText);
        }
    });
}
document.addEventListener('DOMContentLoaded',loadProductData)

// Hàm xử lý thêm sản phẩm
function addProduct(event) {
    event.preventDefault();  // Ngăn reload trang

    // Lấy form theo ID
    var form = document.getElementById('addProductForm');
    var formData = new FormData(form);
    const productData = Object.fromEntries(formData.entries());

    // Ép kiểu dữ liệu
    productData.nameProduct = String(productData.nameProduct);
    productData.image = String(productData.image);
    productData.priceProduct = parseFloat(productData.priceProduct);
    productData.description = String(productData.description);
    productData.manufactureDate = String(productData.manufactureDate);
    productData.power = String(productData.power);
    productData.pressure = parseFloat(productData.pressure);
    productData.flowRate = parseFloat(productData.flowRate);
    productData.pipeDiameter = parseFloat(productData.pipeDiameter);
    productData.voltage = parseInt(productData.voltage);
    productData.brand = String(productData.brand);
    productData.warrantyMonths = parseInt(productData.warrantyMonths);
    productData.stock = parseInt(productData.stock);
    productData.idCategory = parseInt(productData.idCategory);
    productData.idSupplier = parseInt(productData.idSupplier);

    console.log(productData); // Debug data

    // Gửi AJAX
    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/productController',
        type: 'POST',
        data: JSON.stringify(productData),
        contentType: 'application/json',
        success: function (response) {
            alert(response.message);
            if (response.message === "Sản phẩm đã được thêm thành công!") {
                $('#addProductModal').modal('hide');
                // Giả sử có hàm loadProducts()
                loadProductData();
            }
        },
        error: function (xhr, status, error) {
            alert("Có lỗi xảy ra: " + error);
        }
    });
}
// sua san pham
// Hàm xử lý khi nhấn nút "Sửa" để hiển thị dữ liệu sản phẩm vào modal
$(document).on('click', '.product-edit-btn', function() {
    var id = $(this).data('id');  // Lấy id sản phẩm từ thuộc tính data-id
    showEditProductModal(id);  // Hiển thị modal sửa sản phẩm
});

// Hàm hiển thị dữ liệu sản phẩm trong modal sửa
function showEditProductModal(id) {
    // Lấy dữ liệu của sản phẩm từ dòng chứa nút sửa

    var row = $('button[data-id="'+id+'"]').closest('tr');  // Lấy dòng chứa nút sửa
    // Lấy giá trị từ các cột trong dòng đó và điền vào form modal
    $('#editProductId').val(id);
    $('#editProductName').val(row.find('td').eq(1).text());  // Lấy tên sản phẩm từ cột thứ 2
    $('#editProductImage').val(row.find('td').eq(2).find('img').attr('src'));  // Lấy hình ảnh từ cột thứ 3
    $('#editProductPrice').val(row.find('td').eq(3).text());  // Lấy giá từ cột thứ 4
    $('#editProductDescription').val(row.find('td').eq(4).text());  // Lấy mô tả từ cột thứ 5
    $('#editProductManufactureDate').val(row.find('td').eq(5).text());  // Lấy ngày sản xuất từ cột thứ 6
    $('#editProductPower').val(row.find('td').eq(6).text());  // Lấy công suất từ cột thứ 7
    $('#editProductPressure').val(row.find('td').eq(7).text());  // Lấy áp suất từ cột thứ 8
    $('#editProductFlowRate').val(row.find('td').eq(8).text());  // Lấy lưu lượng từ cột thứ 9
    $('#editProductPipeDiameter').val(row.find('td').eq(9).text());  // Lấy đường kính ống từ cột thứ 10
    $('#editProductVoltage').val(row.find('td').eq(10).text());  // Lấy điện áp từ cột thứ 11
    $('#editProductBrand').val(row.find('td').eq(11).text());  // Lấy thương hiệu từ cột thứ 12
    $('#editProductWarrantyMonths').val(row.find('td').eq(12).text());  // Lấy bảo hành từ cột thứ 13
    $('#editProductStock').val(row.find('td').eq(13).text());  // Lấy tồn kho từ cột thứ 14
    $('#editProductCategory').val(row.find('td').eq(14).data('category-id'));  // Lấy danh mục từ cột thứ 15
    $('#editProductSupplier').val(row.find('td').eq(15).data('supplier-id')); // Lấy nhà cung cấp từ cột thứ 16

    // Hiển thị modal
    $('#editProductModal').modal('show');
}

// Hàm xử lý sửa sản phẩm
function editProduct(event) {
    event.preventDefault();  // Ngăn reload trang

    // Lấy form theo ID
    var form = document.getElementById('editProductForm');
    var formData = new FormData(form);
    const productData = Object.fromEntries(formData.entries());
    var pid =parseInt(document.getElementById('editProductId').value);
    // Ép kiểu dữ liệu
    productData.id = parseInt(productData.id);  // ID sản phẩm cần sửa
    productData.nameProduct = String(productData.nameProduct);
    productData.image = String(productData.image);
    productData.priceProduct = parseFloat(productData.priceProduct);
    productData.description = String(productData.description);
    productData.manufactureDate = String(productData.manufactureDate);
    productData.power = String(productData.power);
    productData.pressure = parseFloat(productData.pressure);
    productData.flowRate = parseFloat(productData.flowRate);
    productData.pipeDiameter = parseFloat(productData.pipeDiameter);
    productData.voltage = parseInt(productData.voltage);
    productData.brand = String(productData.brand);
    productData.warrantyMonths = parseInt(productData.warrantyMonths);
    productData.stock = parseInt(productData.stock);
    productData.idCategory = parseInt(productData.idCategory);
    productData.idSupplier = parseInt(productData.idSupplier);

    console.log(productData); // Debug data

    // Gửi yêu cầu PUT để cập nhật sản phẩm

    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/productController?id=' + pid,  // API xử lý PUT sửa sản phẩm
        type: 'PUT',
        data: JSON.stringify(productData),
        contentType: 'application/json',
        success: function(response) {
            alert(response.message);
            if (response.message === "Sản phẩm đã được cập nhật thành công!") {
                $('#editProductModal').modal('hide');  // Đóng modal sau khi sửa thành công
                loadProductData();  // Tải lại dữ liệu sản phẩm
            }
        },
        error: function(xhr, status, error) {
            console.error('Lỗi cập nhật :', error);
            alert("Có lỗi xảy ra: " + error);
        }
    });
}
// Hàm xử lý xóa sản phẩm

$(document).off('click', '.product-delete-btn').on('click', '.product-delete-btn', function () {
    var productId = $(this).data('id');

    if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        $.ajax({
            url: 'http://localhost:8080/DoAnLTWeb_war/productController?id=' + productId,
            type: 'DELETE',
            success: function (response) {
                alert(response.message);
                loadProductData();
            },
            error: function (xhr, status, error) {
                console.log(xhr);
                let errorMessage;
                try {
                    errorMessage = JSON.parse(xhr.responseText).message || 'Có lỗi xảy ra khi xóa sản phẩm';
                } catch (e) {
                    errorMessage = 'Có lỗi xảy ra khi xóa sản phẩm';
                }
                alert(errorMessage);
            }
        });
    }
});

// --------------khuyen mãi--------------------
function loadSaleData() {
    if ($.fn.dataTable.isDataTable('#promotionTable')) {
        $('#promotionTable').DataTable().clear().destroy();
    }

    // Static mapping for status names (you can modify or make this dynamic if necessary)
    var statuses = {
        1: 'Đang áp dụng',
        0: 'Không áp dụng'
    };

    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/SaleController', // API của bạn
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data); // Kiểm tra dữ liệu trả về
            var tableBody = $('#promotionBody');
            tableBody.empty(); // Xóa dữ liệu cũ

            data.forEach(function (sale) {

                // Get status name based on status ID
                var statusName = statuses[sale.status] || 'N/A';

                var row = `<tr>
                        <td>${sale.id}</td>
                        <td style="min-width: 50px;">${sale.promotion}</td>
                        <td style="min-width: 130px;">${sale.description}</td>
                        <td>${statusName}</td>
                        <td style="min-width: 100px;">${sale.startDate}</td>
                        <td style="min-width: 100px;">${sale.endDate}</td>
                        <td>${sale.idProduct}</td>
                        <td>
                            <div class="d-flex gap-2 justify-content-center">
                                <button class="btn btn-sm btn-primary promotion-edit-btn" data-id="${sale.id}">Sửa</button>
                                <button class="btn btn-sm btn-danger promotion-delete-btn" data-id="${sale.id}">Xóa</button>
                            </div>
                        </td>
                    </tr>`;
                tableBody.append(row);
            });

            // Destroy bảng cũ nếu có
            if ($.fn.DataTable.isDataTable('#promotionTable')) {
                $('#promotionTable').DataTable().destroy();
            }

            // Khởi tạo DataTable với tiếng Việt
            $('#promotionTable').DataTable({
                "paging": true,
                "searching": true,
                "ordering": true,
                "lengthChange": true,
                "language": {
                    "lengthMenu": "Hiển thị _MENU_ khuyến mãi mỗi trang",
                    "zeroRecords": "Không tìm thấy khuyến mãi nào",
                    "info": "Hiển thị _START_ đến _END_ của _TOTAL_ khuyến mãi",
                    "infoEmpty": "Không có khuyến mãi nào",
                    "infoFiltered": "(lọc từ _MAX_ khuyến mãi)",
                    "search": "Tìm kiếm:",
                    "paginate": {
                        "first": "Đầu",
                        "last": "Cuối",
                        "next": "Sau",
                        "previous": "Trước"
                    },
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Lỗi lấy dữ liệu khuyến mãi: " + error);
            console.log(xhr.responseText);
        }
    });
}

document.addEventListener('DOMContentLoaded', loadSaleData);

//them khuyến mãi
$(document).ready(function () {
    $('#addPromotionForm').off('submit').on('submit', function (e) {
        e.preventDefault(); // Ngăn form reload trang

        const startDate = new Date($('#startDate').val());
        const endDate = new Date($('#endDate').val());

        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (endDate <= startDate) {
            alert(" Ngày kết thúc phải sau ngày bắt đầu!");
            return; // Dừng submit
        }

        // Lấy dữ liệu từ form
        const saleData = {
            promotion: parseFloat($('#promotionValue').val()),
            description: $('#description').val(),
            status: parseInt($('#status').val()),
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val(),
            idProduct: parseInt($('#idProduct').val())
        };

        // Gửi dữ liệu lên servlet qua POST
        $.ajax({
            url: 'http://localhost:8080/DoAnLTWeb_war/SaleController',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(saleData),
            success: function (response) {
                alert(" Thêm khuyến mãi thành công!");
                $('#addPromotionForm')[0].reset();
                $('#addPromotionModal').modal('hide');
                loadSaleData();
            },
            error: function (xhr, status, error) {
                console.error("Lỗi khi thêm khuyến mãi:", error);
                alert(" Thêm khuyến mãi thất bại!");
            }
        });
    });
});

//sửa khuyến mãi

$(document).on('click', '.promotion-edit-btn', function (event) {
    event.stopPropagation();
    const row = $(this).closest('tr');
    const id = $(this).data('id');

    // Lấy dữ liệu từng cột
    const promotion = row.find('td:eq(1)').text();
    const description = row.find('td:eq(2)').text();
    const statusText = row.find('td:eq(3)').text().trim();
    const startDate = row.find('td:eq(4)').text();
    const endDate = row.find('td:eq(5)').text();
    const idProduct = row.find('td:eq(6)').text();

    // Chuyển status từ text sang số
    const status = (statusText === 'Đang áp dụng') ? 1 : 0;

    // Format ngày để đưa vào input type="datetime-local"
    const formatDateTime = (dateTimeString) => {
        const date = new Date(dateTimeString);
        return date.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
    };

    // Gán dữ liệu vào form
    $('#editPromotionId').val(id);
    $('#editPromotionValue').val(promotion);
    $('#editDescription').val(description);
    $('#editStatus').val(status);
    $('#editStartDate').val(formatDateTime(startDate));
    $('#editEndDate').val(formatDateTime(endDate));
    $('#editIdProduct').val(idProduct);

    // Hiển thị modal
    $('#editPromotionModal').modal('show');
});
// Khi modal đóng, xóa backdrop và khôi phục cuộn trang
$('#editPromotionModal').on('hidden.bs.modal', function () {
    $('.modal-backdrop').remove();
    $('body').css('overflow', 'auto');  // Khôi phục khả năng cuộn trang
});

$('#editPromotionForm').off('submit').on('submit', function (e) {
    e.preventDefault();

    const startDate = new Date($('#editStartDate').val());
    const endDate = new Date($('#editEndDate').val());

    if (endDate <= startDate) {
        alert("Ngày kết thúc phải sau ngày bắt đầu!");
        return;
    }

    const updatedSale = {
        id: parseInt($('#editPromotionId').val()),
        promotion: parseFloat($('#editPromotionValue').val()),
        description: $('#editDescription').val(),
        status: parseInt($('#editStatus').val()),
        startDate: $('#editStartDate').val(),
        endDate: $('#editEndDate').val(),
        idProduct: parseInt($('#editIdProduct').val())
    };

    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/SaleController',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(updatedSale),
        success: function (response) {
            alert("Cập nhật khuyến mãi thành công!");
            $('#editPromotionModal').modal('hide');
            loadSaleData(); // Reload lại danh sách khuyến mãi
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi cập nhật khuyến mãi:", error);
            alert("Cập nhật khuyến mãi thất bại!");
        }
    });
});
// xoa
$(document).off('click', '.promotion-delete-btn').on('click', '.promotion-delete-btn', function () {
    const id = $(this).data('id');

    if (confirm("Bạn có chắc chắn muốn xóa khuyến mãi này không?")) {
        $.ajax({
            url: `http://localhost:8080/DoAnLTWeb_war/SaleController?id=${id}`,
            type: 'DELETE',
            success: function (response) {
                alert(response.message || "Xóa thành công!");
                loadSaleData(); // Tải lại danh sách
            },
            error: function (xhr, status, error) {
                console.error("Lỗi khi xóa khuyến mãi:", error);
                alert("Xóa khuyến mãi thất bại hoặc không tìm thấy!");
            }
        });
    }
});
// user
function loadUserData() {
    if ($.fn.dataTable.isDataTable('#userTable')) {
        $('#userTable').DataTable().clear().destroy();
    }

    // Mapping quyền (idPermission)
    const permissionMap = {
        1: "Admin",
        2: "Nhân viên",
        3: "Khách hàng",
        4: "Cộng tác viên" // Thêm quyền mới
    };

    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/UserManagerController',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            const tableBody = $('#userBody');
            tableBody.empty();

            data.forEach(user => {
                const permission = permissionMap[user.idPermission] || 'Không rõ';
                const verified = user.isVerified === 1 ? 'Hoạt động' : 'Không hoạt động';
                const avatar = user.avatar
                    ? `<img src="${user.avatar}" alt="Avatar" width="40" height="40" style="border-radius: 50%;">`
                    : '—';

                const row = `
                    <tr>
                        <td>${user.id}</td>
                        <td style="min-width: 100px;">${avatar}</td>
                        <td style="min-width: 100px;">${user.username}</td>
                        <td>${user.fullname || ''}</td>
                        <td style="min-width: 150px;">${user.email || ''}</td>
                        <td>${user.phone || ''}</td>
                        <td style="min-width: 70px;">${user.address || ''}</td>
                        <td>${permission}</td>
                        <td style="min-width: 100px;">${verified}</td>
                        <td>
                            <div class="d-flex gap-2 justify-content-center">
                                <button class="btn btn-sm btn-primary user-edit-btn" data-id="${user.id}">Sửa</button>
                                <button class="btn btn-sm btn-danger user-delete-btn" data-id="${user.id}">Xóa</button>
                            </div>
                        </td>
                    </tr>
                `;
                tableBody.append(row);
            });
            // Destroy bảng cũ nếu có
            if ($.fn.DataTable.isDataTable('#userTable')) {
                $('#userTable').DataTable().destroy();
            }

            $('#userTable').DataTable({
                paging: true,
                searching: true,
                ordering: true,
                lengthChange: true,
                language: {
                    lengthMenu: "Hiển thị _MENU_ người dùng mỗi trang",
                    zeroRecords: "Không tìm thấy người dùng nào",
                    info: "Hiển thị _START_ đến _END_ của _TOTAL_ người dùng",
                    infoEmpty: "Không có người dùng nào",
                    infoFiltered: "(lọc từ _MAX_ người dùng)",
                    search: "Tìm kiếm:",
                    paginate: {
                        first: "Đầu",
                        last: "Cuối",
                        next: "Sau",
                        previous: "Trước"
                    }
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi lấy danh sách người dùng:", error);
            console.log(xhr.responseText);
        }
    });
}

document.addEventListener('DOMContentLoaded', loadUserData);
// sửa
// Sửa thông tin người dùng
$(document).on('click', '.user-edit-btn', function (event) {
    event.stopPropagation();
    const row = $(this).closest('tr');
    const id = $(this).data('id');

    // Lấy dữ liệu từng cột
    const username = row.find('td:eq(2)').text();
    const fullname = row.find('td:eq(3)').text();
    const email = row.find('td:eq(4)').text();
    const phone = row.find('td:eq(5)').text();
    const address = row.find('td:eq(6)').text();
    const permissionText = row.find('td:eq(7)').text().trim();
    const verifiedText = row.find('td:eq(8)').text().trim();
    const avatar = row.find('td:eq(1)').find('img').attr('src');

    // Mapping quyền (idPermission)
    const permissionMap = {
        "Admin": 1,
        "Nhân viên": 2,
        "Khách hàng": 3,
        "Cộng tác viên": 4
    };

    // Chuyển quyền và trạng thái xác thực từ text sang số
    const idPermission = permissionMap[permissionText] || 3;  // Mặc định là "Khách hàng"
    const isVerified = (verifiedText === 'Đã xác thực') ? 1 : 0;

    // Gán dữ liệu vào form
    $('#editUserId').val(id);
    $('#editUsername').val(username);
    $('#editFullname').val(fullname);
    $('#editEmail').val(email);
    $('#editPhone').val(phone);
    $('#editAddress').val(address);
    $('#editPermission').val(idPermission);
    $('#editVerified').val(isVerified);
    $('#editAvatar').val(avatar); // Link ảnh đại diện

    // Hiển thị modal
    $('#editUserModal').modal('show');
});

// Khi modal đóng, xóa backdrop và khôi phục cuộn trang
$('#editUserModal').on('hidden.bs.modal', function () {
    $('.modal-backdrop').remove();
    $('body').css('overflow', 'auto');  // Khôi phục khả năng cuộn trang
});

// Xử lý khi gửi form sửa người dùng
$('#editUserForm').off('submit').on('submit', function (e) {
    e.preventDefault();

    const updatedUser = {
        id: parseInt($('#editUserId').val()),
        username: $('#editUsername').val(),
        fullname: $('#editFullname').val(),
        email: $('#editEmail').val(),
        phone: $('#editPhone').val(),
        address: $('#editAddress').val(),
        idPermission: parseInt($('#editPermission').val()),
        isVerified: parseInt($('#editVerified').val()),
        avatar: $('#editAvatar').val()
    };

    // Gửi yêu cầu PUT để cập nhật người dùng
    $.ajax({
        url: 'http://localhost:8080/DoAnLTWeb_war/UserManagerController',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(updatedUser),
        success: function (response) {
            alert("Cập nhật người dùng thành công!");
            $('#editUserModal').modal('hide');
            loadUserData(); // Reload lại danh sách người dùng
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi cập nhật người dùng:", error);
            alert("Cập nhật người dùng thất bại!");
        }
    });
});
// Gỡ sự kiện cũ trước khi gán lại để tránh lặp
$(document).off('click', '.user-delete-btn').on('click', '.user-delete-btn', function (e) {
    e.preventDefault();
    const userId = $(this).data('id');

    if (confirm("Bạn có chắc chắn muốn xóa?")) {
        $.ajax({
            url: 'http://localhost:8080/DoAnLTWeb_war/UserManagerController',
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify({ id: userId }),
            success: function (response) {
                alert("Người dùng đã được xóa.");
                loadUserData(); // Tải lại bảng dữ liệu
            },
            error: function (xhr, status, error) {
                console.error("Lỗi khi xóa người dùng:", error);
                alert("Xóa người dùng thất bại!");
            }
        });
    }
});


