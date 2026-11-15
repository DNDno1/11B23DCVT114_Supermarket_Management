<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDate, model.ImportInvoice4, model.Item4, util.HtmlUtil"%>
<%
    Item4 item = (Item4) request.getAttribute("item");
    ImportInvoice4 created = (ImportInvoice4) request.getAttribute("createdInvoice");
    String defaultDuetime = LocalDate.now().plusMonths(6).toString();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo đơn nhập hàng</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 720px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">NHẬP HÀNG &amp; LẬP HÓA ĐƠN NHẬP</h2>
            <a href="<%=request.getContextPath()%>/warehouse/import" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>

        <% if (item != null) { %>
            <table style="margin-bottom: 18px;">
                <tbody>
                <tr><th style="width:200px;">Mã sản phẩm</th><td><%=HtmlUtil.itemCode(item.getId())%></td></tr>
                <tr><th>Tên sản phẩm</th><td><strong><%=HtmlUtil.esc(item.getName())%></strong></td></tr>
                <tr><th>Nhà cung cấp</th><td><%=item.getSupplier() != null ? HtmlUtil.esc(item.getSupplier().getName()) : ""%></td></tr>
                <tr><th>Số lượng tồn hiện tại</th><td><strong><%=item.getQuantity()%></strong></td></tr>
                <tr><th>Giá nhập đơn vị</th><td><%=HtmlUtil.money(item.getImportPrice())%></td></tr>
                <tr><th>Giá bán lẻ</th><td><%=HtmlUtil.money(item.getPrice())%></td></tr>
                <tr><th>Ghi chú</th><td><%=HtmlUtil.esc(item.getNote())%></td></tr>
                </tbody>
            </table>

            <form action="<%=request.getContextPath()%>/warehouse/import/item" method="post">
                <input type="hidden" name="itemId" value="<%=item.getId()%>">
                <div class="grid">
                    <div class="form-group">
                        <label for="quantity">Số lượng cần nhập *</label>
                        <input id="quantity" type="number" name="quantity" class="form-control" min="1" value="10" required>
                    </div>
                    <div class="form-group">
                        <label for="duetime">Ngày tới hạn / Hạn sử dụng</label>
                        <input id="duetime" type="date" name="duetime" class="form-control" value="<%=defaultDuetime%>">
                    </div>
                </div>
                <div class="actions-row">
                    <button type="submit" class="btn btn-primary">Xác nhận nhập hàng</button>
                    <a href="<%=request.getContextPath()%>/warehouse/import" class="btn btn-secondary">Quay lại danh sách</a>
                </div>
            </form>

            <% if (created != null) { %>
                <div class="stat-card" style="margin-top: 20px;">
                    <div class="stat-label">Hóa đơn vừa tạo</div>
                    <div>Mã hóa đơn: <strong><%=HtmlUtil.invoiceCode(created.getId())%></strong> | Số lượng nhập: <strong><%=created.getQuantity()%></strong> | Tổng tiền: <strong><%=HtmlUtil.money(created.getTotalAmount())%></strong></div>
                </div>
            <% } %>
        <% } %>
    </div>
</div>
</body>
</html>
