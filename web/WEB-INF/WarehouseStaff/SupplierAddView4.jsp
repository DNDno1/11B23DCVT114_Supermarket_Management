<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Supplier4, util.HtmlUtil"%>
<%
    Supplier4 sup = (Supplier4) request.getAttribute("supplier");
    if (sup == null) sup = new Supplier4();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm nhà cung cấp mới</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 680px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">THÊM NHÀ CUNG CẤP MỚI</h2>
            <a href="<%=request.getContextPath()%>/warehouse/suppliers" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/warehouse/suppliers/prepare" method="post">
            <input type="hidden" name="mode" value="ADD">
            <div class="form-group">
                <label for="name">Tên nhà cung cấp *</label>
                <input id="name" type="text" name="name" class="form-control" value="<%=HtmlUtil.esc(sup.getName())%>" required>
            </div>
            <div class="form-group">
                <label for="address">Địa chỉ *</label>
                <input id="address" type="text" name="address" class="form-control" value="<%=HtmlUtil.esc(sup.getAddress())%>" required>
            </div>
            <div class="grid">
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input id="email" type="email" name="email" class="form-control" value="<%=HtmlUtil.esc(sup.getEmail())%>" required>
                </div>
                <div class="form-group">
                    <label for="phonenumber">Số điện thoại *</label>
                    <input id="phonenumber" type="text" name="phonenumber" class="form-control" value="<%=HtmlUtil.esc(sup.getPhonenumber())%>" required>
                </div>
            </div>
            <div class="form-group">
                <label for="note">Ghi chú</label>
                <textarea id="note" name="note" rows="3" class="form-control"><%=HtmlUtil.esc(sup.getNote())%></textarea>
            </div>
            <div class="actions-row">
                <button type="submit" class="btn btn-primary">Lưu thông tin</button>
                <a href="<%=request.getContextPath()%>/warehouse/suppliers" class="btn btn-secondary">Quay lại</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
