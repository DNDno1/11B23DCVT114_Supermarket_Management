<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Item4, model.Supplier4, util.HtmlUtil"%>
<%
    String confirmTitle = (String) request.getAttribute("confirmTitle");
    String confirmOp = (String) request.getAttribute("confirmOp");
    Item4 item = (Item4) request.getAttribute("pendingItem");
    Supplier4 sup = (Supplier4) request.getAttribute("pendingSupplier");
    boolean isDelete = confirmOp != null && confirmOp.startsWith("DELETE_");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác nhận thao tác</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 680px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title"><%=HtmlUtil.esc(confirmTitle != null ? confirmTitle : "XÁC NHẬN THAO TÁC")%></h2>
        </div>

        <% if (isDelete) { %>
            <div class="alert alert-error">Bạn có chắc chắn muốn xóa bản ghi sau khỏi hệ thống không?</div>
        <% } else { %>
            <div class="alert alert-success">Vui lòng kiểm tra kỹ thông tin trước khi xác nhận lưu vào cơ sở dữ liệu.</div>
        <% } %>

        <% if (item != null) { %>
            <table>
                <tbody>
                <% if (item.getId() > 0) { %>
                    <tr><th style="width:180px;">Mã sản phẩm</th><td><%=HtmlUtil.itemCode(item.getId())%></td></tr>
                <% } %>
                <tr><th>Tên sản phẩm</th><td><strong><%=HtmlUtil.esc(item.getName())%></strong></td></tr>
                <tr><th>Giá nhập</th><td><%=HtmlUtil.money(item.getImportPrice())%></td></tr>
                <tr><th>Giá bán</th><td><%=HtmlUtil.money(item.getPrice())%></td></tr>
                <tr><th>Số lượng tồn</th><td><%=item.getQuantity()%></td></tr>
                <tr><th>Nhà cung cấp</th><td><%=item.getSupplier() != null ? HtmlUtil.esc(item.getSupplier().getName()) : ""%></td></tr>
                <tr><th>Ghi chú</th><td><%=HtmlUtil.esc(item.getNote())%></td></tr>
                </tbody>
            </table>
        <% } else if (sup != null) { %>
            <table>
                <tbody>
                <% if (sup.getId() > 0) { %>
                    <tr><th style="width:180px;">Mã nhà cung cấp</th><td>SUP<%=sup.getId()%></td></tr>
                <% } %>
                <tr><th>Tên nhà cung cấp</th><td><strong><%=HtmlUtil.esc(sup.getName())%></strong></td></tr>
                <tr><th>Địa chỉ</th><td><%=HtmlUtil.esc(sup.getAddress())%></td></tr>
                <tr><th>Email</th><td><%=HtmlUtil.esc(sup.getEmail())%></td></tr>
                <tr><th>Số điện thoại</th><td><%=HtmlUtil.esc(sup.getPhonenumber())%></td></tr>
                <tr><th>Ghi chú</th><td><%=HtmlUtil.esc(sup.getNote())%></td></tr>
                </tbody>
            </table>
        <% } %>

        <form action="<%=request.getContextPath()%>/warehouse/confirm" method="post" class="actions-row" style="margin-top:20px;">
            <button type="submit" name="decision" value="confirm" class="btn <%=isDelete ? "btn-danger" : "btn-primary"%>">
                Xác nhận
            </button>
            <% if (!isDelete) { %>
                <button type="submit" name="decision" value="back" class="btn btn-secondary">Quay lại chỉnh sửa</button>
            <% } %>
            <button type="submit" name="decision" value="cancel" class="btn btn-secondary">Hủy</button>
        </form>
    </div>
</div>
</body>
</html>
