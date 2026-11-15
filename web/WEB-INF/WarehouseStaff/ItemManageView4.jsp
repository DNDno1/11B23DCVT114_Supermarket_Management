<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Item4, util.HtmlUtil"%>
<%
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<Item4> items = (List<Item4>) request.getAttribute("items");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý hàng hóa</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">QUẢN LÝ THÔNG TIN HÀNG HÓA</h2>
            <div class="actions-row" style="margin:0;">
                <a href="<%=request.getContextPath()%>/warehouse/items/add" class="btn btn-primary btn-sm">+ Thêm hàng hóa</a>
                <a href="<%=request.getContextPath()%>/warehouse/manage" class="btn btn-secondary btn-sm">Quay lại</a>
            </div>
        </div>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/warehouse/items" method="get" class="form-inline">
            <div class="field" style="flex:1; min-width:220px;">
                <label for="keyword">Tìm kiếm hàng hóa</label>
                <input id="keyword" type="text" name="keyword" class="form-control"
                       placeholder="Nhập tên hoặc mã sản phẩm..." value="<%=HtmlUtil.esc(keyword)%>">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Mã SP</th>
                    <th>Tên hàng hóa</th>
                    <th>Nhà cung cấp</th>
                    <th>Tồn kho</th>
                    <th>Giá nhập</th>
                    <th>Giá bán</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (items == null || items.isEmpty()) { %>
                    <tr><td colspan="7" class="muted">Không có hàng hóa nào.</td></tr>
                <% } else { for (Item4 item : items) { %>
                    <tr>
                        <td><%=HtmlUtil.itemCode(item.getId())%></td>
                        <td><strong><%=HtmlUtil.esc(item.getName())%></strong></td>
                        <td><%=item.getSupplier() != null ? HtmlUtil.esc(item.getSupplier().getName()) : ""%></td>
                        <td><%=item.getQuantity()%></td>
                        <td><%=HtmlUtil.money(item.getImportPrice())%></td>
                        <td><%=HtmlUtil.money(item.getPrice())%></td>
                        <td>
                            <a href="<%=request.getContextPath()%>/warehouse/items/edit?id=<%=item.getId()%>" class="btn btn-warning btn-sm">Sửa</a>
                            <a href="<%=request.getContextPath()%>/warehouse/items/delete?id=<%=item.getId()%>" class="btn btn-danger btn-sm">Xóa</a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
