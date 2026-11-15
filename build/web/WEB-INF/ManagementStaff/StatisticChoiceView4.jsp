<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chọn loại thống kê</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHỌN BÁO CÁO THỐNG KÊ</h2>
            <a href="<%=request.getContextPath()%>/manager" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <div class="grid">
            <a href="<%=request.getContextPath()%>/manager/stats/items" class="menu-card">
                <h3>1. Thống kê theo sản phẩm</h3>
                <p>Xem danh sách mặt hàng theo doanh thu bán hàng và lịch sử các lượt nhập hàng trong khoảng thời gian.</p>
            </a>
            <a href="<%=request.getContextPath()%>/manager/stats/suppliers" class="menu-card">
                <h3>2. Thống kê theo nhà cung cấp</h3>
                <p>Xem doanh thu, sản lượng tiêu thụ và danh sách hóa đơn nhập hàng của từng nhà cung cấp.</p>
            </a>
            <a href="<%=request.getContextPath()%>/manager/stats/revenue" class="menu-card">
                <h3>3. Thống kê theo doanh thu</h3>
                <p>Tổng hợp doanh thu bán trực tiếp tại quầy, bán hàng trực tuyến, tổng chi nhập hàng và lợi nhuận.</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
