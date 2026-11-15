<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Item4, util.HtmlUtil"%>
<%
    Item4 item = (Item4) request.getAttribute("item");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sản phẩm</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 760px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHI TIẾT SẢN PHẨM</h2>
            <a href="<%=request.getContextPath()%>/customer/search?keyword=<%=java.net.URLEncoder.encode(keyword, "UTF-8")%>"
               class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>

        <% if (item != null) { %>
            <table>
                <tbody>
                <tr>
                    <th style="width: 220px;">Mã sản phẩm</th>
                    <td><%=HtmlUtil.itemCode(item.getId())%></td>
                </tr>
                <tr>
                    <th>Tên sản phẩm</th>
                    <td><strong><%=HtmlUtil.esc(item.getName())%></strong></td>
                </tr>
                <tr>
                    <th>Giá bán</th>
                    <td style="font-weight:700; color:var(--primary);"><%=HtmlUtil.money(item.getPrice())%></td>
                </tr>
                <tr>
                    <th>Số lượng hiện tại</th>
                    <td>
                        <%=item.getQuantity()%>
                        <% if (item.getQuantity() > 0) { %>
                            <span class="badge badge-ok">Còn hàng</span>
                        <% } else { %>
                            <span class="badge badge-out">Hết hàng</span>
                        <% } %>
                    </td>
                </tr>
                <tr>
                    <th>Nhà sản xuất / Cung cấp</th>
                    <td><%=item.getSupplier() != null ? HtmlUtil.esc(item.getSupplier().getName()) : ""%></td>
                </tr>
                <tr>
                    <th>Ghi chú / Giới thiệu</th>
                    <td><%=HtmlUtil.esc(item.getNote())%></td>
                </tr>
                </tbody>
            </table>

            <% if (item.getQuantity() > 0) { %>
                <form action="<%=request.getContextPath()%>/customer/cart/add" method="post" style="margin-top: 20px;">
                    <input type="hidden" name="itemId" value="<%=item.getId()%>">
                    <input type="hidden" name="keyword" value="<%=HtmlUtil.esc(keyword)%>">
                    <div class="form-inline">
                        <div class="field">
                            <label for="quantity">Nhập số lượng mua</label>
                            <input id="quantity" type="number" name="quantity" class="form-control"
                                   value="1" min="1" max="<%=item.getQuantity()%>" required style="width: 140px;">
                        </div>
                        <button type="submit" class="btn btn-primary">Thêm hàng vào giỏ</button>
                        <button type="submit" name="goOrder" value="1" class="btn btn-warning">Mua ngay</button>
                    </div>
                </form>
            <% } else { %>
                <div class="alert alert-error" style="margin-top: 16px;">Sản phẩm này hiện đang hết hàng.</div>
            <% } %>
        <% } %>
    </div>
</div>
</body>
</html>
