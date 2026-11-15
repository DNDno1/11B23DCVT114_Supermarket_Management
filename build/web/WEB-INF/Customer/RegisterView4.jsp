<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Customer4, util.HtmlUtil"%>
<%
    Customer4 formCust = (Customer4) request.getAttribute("formCustomer");
    if (formCust == null) formCust = new Customer4();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký thành viên - Siêu thị Trực tuyến</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container auth-box" style="max-width: 580px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">Đăng ký tài khoản khách hàng</h2>
            <a href="<%=request.getContextPath()%>/login" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/register" method="post">
            <div class="form-group">
                <label for="name">Họ và tên *</label>
                <input id="name" type="text" name="name" class="form-control"
                       value="<%=HtmlUtil.esc(formCust.getName())%>" required>
            </div>
            <div class="form-group">
                <label for="username">Tên đăng nhập *</label>
                <input id="username" type="text" name="username" class="form-control"
                       value="<%=HtmlUtil.esc(formCust.getUsername())%>" required>
            </div>
            <div class="grid">
                <div class="form-group">
                    <label for="password">Mật khẩu *</label>
                    <input id="password" type="password" name="password" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu *</label>
                    <input id="confirmPassword" type="password" name="confirmPassword" class="form-control" required>
                </div>
            </div>
            <div class="grid">
                <div class="form-group">
                    <label for="birthdate">Ngày sinh</label>
                    <input id="birthdate" type="date" name="birthdate" class="form-control"
                           value="<%=HtmlUtil.esc(formCust.getBirthdate())%>">
                </div>
                <div class="form-group">
                    <label for="phonenumber">Số điện thoại</label>
                    <input id="phonenumber" type="text" name="phonenumber" class="form-control"
                           value="<%=HtmlUtil.esc(formCust.getPhonenumber())%>">
                </div>
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <input id="email" type="email" name="email" class="form-control"
                       value="<%=HtmlUtil.esc(formCust.getEmail())%>" required>
            </div>
            <div class="form-group">
                <label for="address">Địa chỉ *</label>
                <input id="address" type="text" name="address" class="form-control"
                       value="<%=HtmlUtil.esc(formCust.getAddress())%>" required>
            </div>
            <div class="actions-row">
                <button type="submit" class="btn btn-primary">Xác nhận đăng ký</button>
                <a href="<%=request.getContextPath()%>/login" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
