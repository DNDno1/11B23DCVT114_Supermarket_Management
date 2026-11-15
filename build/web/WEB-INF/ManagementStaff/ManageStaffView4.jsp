<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User4, util.HtmlUtil"%>
<%
    User4 user = (User4) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Nhân viên Quản lý</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">TRANG CHỦ NHÂN VIÊN QUẢN LÝ</h2>
        </div>
        <p class="muted">Xin chào <strong><%=HtmlUtil.esc(user.getName())%></strong>. Chọn chức năng báo cáo thống kê để theo dõi hoạt động kinh doanh của siêu thị.</p>

        <div class="grid" style="margin-top: 16px;">
            <a href="<%=request.getContextPath()%>/manager/stats" class="menu-card">
                <h3>Xem báo cáo thống kê</h3>
                <p>Xem thống kê theo sản phẩm, theo nhà cung cấp theo doanh thu, và tổng hợp doanh thu - chi phí nhập hàng.</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
