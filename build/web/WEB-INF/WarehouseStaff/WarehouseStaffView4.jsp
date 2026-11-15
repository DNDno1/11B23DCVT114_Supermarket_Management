<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User4, util.HtmlUtil"%>
<%
    User4 user = (User4) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Nhân viên Kho</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">TRANG CHỦ NHÂN VIÊN KHO</h2>
        </div>
        <p class="muted">Xin chào <strong><%=HtmlUtil.esc(user.getName())%></strong>. Chọn nghiệp vụ kho hàng bên dưới.</p>

        <div class="grid" style="margin-top: 16px;">
            <a href="<%=request.getContextPath()%>/warehouse/manage" class="menu-card">
                <h3>Quản lý thông tin</h3>
                <p>Thêm, sửa, xóa và tra cứu thông tin hàng hóa và nhà cung cấp.</p>
            </a>
            <a href="<%=request.getContextPath()%>/warehouse/import" class="menu-card">
                <h3>Nhập hàng từ nhà cung cấp</h3>
                <p>Tìm kiếm mặt hàng, lập hóa đơn nhập hàng và cập nhật số lượng tồn kho.</p>
            </a>
            <a href="<%=request.getContextPath()%>/warehouse/orders" class="menu-card">
                <h3>Duyệt đơn hàng online &amp; Xuất hàng</h3>
                <p>Xem hàng đợi đơn đặt hàng trực tuyến, kiểm tra tồn kho và xuất hàng cho nhân viên giao hàng.</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
