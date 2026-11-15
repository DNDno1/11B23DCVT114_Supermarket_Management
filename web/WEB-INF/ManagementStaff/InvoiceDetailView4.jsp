<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.ImportInvoice4, util.HtmlUtil"%>
<%
    ImportInvoice4 inv = (ImportInvoice4) request.getAttribute("invoice");
    String backUrl = (String) request.getAttribute("backUrl");
    if (backUrl == null) backUrl = request.getContextPath() + "/manager/stats";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết hóa đơn nhập</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 760px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHI TIẾT HÓA ĐƠN NHẬP HÀNG</h2>
            <a href="<%=backUrl%>" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <% if (inv != null) { %>
            <table>
                <tbody>
                <tr>
                    <th style="width: 240px;">Mã hóa đơn</th>
                    <td><strong><%=HtmlUtil.invoiceCode(inv.getId())%></strong></td>
                </tr>
                <tr>
                    <th>Tên sản phẩm</th>
                    <td><%=HtmlUtil.esc(inv.getItem().getName())%></td>
                </tr>
                <tr>
                    <th>Mã sản phẩm</th>
                    <td><%=HtmlUtil.itemCode(inv.getItem().getId())%></td>
                </tr>
                <tr>
                    <th>Nhà cung cấp</th>
                    <td><%=inv.getItem().getSupplier() != null ? HtmlUtil.esc(inv.getItem().getSupplier().getName()) : ""%></td>
                </tr>
                <tr>
                    <th>Đơn giá nhập</th>
                    <td><%=HtmlUtil.money(inv.getItem().getImportPrice())%></td>
                </tr>
                <tr>
                    <th>Ngày nhập hàng</th>
                    <td><%=inv.getDatetime()%></td>
                </tr>
                <tr>
                    <th>Ngày tới hạn</th>
                    <td><%=inv.getDuetime() != null ? inv.getDuetime() : "Không xác định"%></td>
                </tr>
                <tr>
                    <th>Số lượng nhập</th>
                    <td><%=inv.getQuantity()%></td>
                </tr>
                <tr>
                    <th>Nhân viên kho thực hiện</th>
                    <td><%=inv.getwStaff() != null ? HtmlUtil.esc(inv.getwStaff().getName()) : ""%></td>
                </tr>
                <tr>
                    <th>Tổng tiền</th>
                    <td style="font-size:1.15rem; font-weight:700; color:var(--primary);"><%=HtmlUtil.money(inv.getTotalAmount())%></td>
                </tr>
                </tbody>
            </table>

            <div class="actions-row">
                <button type="button" onclick="window.print()" class="btn btn-primary">In hóa đơn</button>
                <a href="<%=backUrl%>" class="btn btn-secondary">Quay lại</a>
            </div>
        <% } %>
    </div>
</div>
</body>
</html>
