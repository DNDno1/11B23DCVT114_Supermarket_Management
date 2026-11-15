<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@page import="util.HtmlUtil"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lỗi hệ thống</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<div class="container auth-box">
    <div class="card">
        <h2 class="page-title">Đã xảy ra lỗi</h2>
        <div class="alert alert-error" style="margin-top: 14px;">
            <%= exception != null ? HtmlUtil.esc(exception.getMessage()) : "Yêu cầu không hợp lệ hoặc trang không tồn tại." %>
        </div>
        <a href="<%=request.getContextPath()%>/login" class="btn btn-primary">Quay về trang chủ</a>
    </div>
</div>
</body>
</html>
