<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý thông tin kho</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">QUẢN LÝ THÔNG TIN</h2>
            <a href="<%=request.getContextPath()%>/warehouse" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <div class="grid">
            <a href="<%=request.getContextPath()%>/warehouse/items" class="menu-card">
                <h3>Quản lý thông tin hàng hóa</h3>
                <p>Tìm kiếm, thêm mới, chỉnh sửa hoặc xóa các mặt hàng trong hệ thống.</p>
            </a>
            <a href="<%=request.getContextPath()%>/warehouse/suppliers" class="menu-card">
                <h3>Quản lý thông tin nhà cung cấp</h3>
                <p>Tìm kiếm, thêm mới, chỉnh sửa hoặc xóa thông tin đối tác nhà cung cấp.</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
