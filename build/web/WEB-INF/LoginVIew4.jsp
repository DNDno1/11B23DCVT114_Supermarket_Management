<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.HtmlUtil"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ thống Quản lý Siêu thị</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container auth-box">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">Đăng nhập hệ thống</h2>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input id="username" type="text" name="username" class="form-control"
                       value="<%=HtmlUtil.esc(request.getAttribute("username"))%>" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input id="password" type="password" name="password" class="form-control" required>
            </div>
            <div class="actions-row">
                <button type="submit" class="btn btn-primary" style="flex:1;">Đăng nhập</button>
                <a href="<%=request.getContextPath()%>/register" class="btn btn-secondary">Đăng ký thành viên</a>
            </div>
        </form>

        <div style="margin-top: 20px; padding-top: 14px; border-top: 1px solid var(--border);">
            <div class="muted">
                <strong>Tài khoản mẫu (mật khẩu: <code>123456</code>):</strong><br>
                • Quản lý: <code>manager</code> &nbsp;|&nbsp;
                • Kho: <code>warehouse</code> &nbsp;|&nbsp;
                • Bán hàng: <code>saler</code> &nbsp;|&nbsp;
                • Khách hàng: <code>customer1</code>
            </div>
        </div>
    </div>
</div>
</body>
</html>
