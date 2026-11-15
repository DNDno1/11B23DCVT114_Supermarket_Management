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
    <title>Nhập hàng từ nhà cung cấp</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">NHẬP HÀNG TỪ NHÀ CUNG CẤP</h2>
            <a href="<%=request.getContextPath()%>/warehouse" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <form action="<%=request.getContextPath()%>/warehouse/import" method="get" class="form-inline">
            <div class="field" style="flex:1; min-width:220px;">
                <label for="keyword">Tìm sản phẩm cần nhập</label>
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
                    <th>Tên sản phẩm</th>
                    <th>Nhà cung cấp</th>
                    <th>Tồn kho hiện tại</th>
                    <th>Giá nhập</th>
                    <th>Giá bán</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (items == null || items.isEmpty()) { %>
                    <tr><td colspan="7" class="muted">Không tìm thấy sản phẩm nào.</td></tr>
                <% } else { for (Item4 item : items) { %>
                    <tr>
                        <td><%=HtmlUtil.itemCode(item.getId())%></td>
                        <td><strong><%=HtmlUtil.esc(item.getName())%></strong></td>
                        <td><%=item.getSupplier() != null ? HtmlUtil.esc(item.getSupplier().getName()) : ""%></td>
                        <td><%=item.getQuantity()%></td>
                        <td><%=HtmlUtil.money(item.getImportPrice())%></td>
                        <td><%=HtmlUtil.money(item.getPrice())%></td>
                        <td>
                            <a href="<%=request.getContextPath()%>/warehouse/import/item?id=<%=item.getId()%>"
                               class="btn btn-primary btn-sm">Nhập hàng</a>
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
