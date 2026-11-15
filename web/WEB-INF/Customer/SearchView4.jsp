<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Item4, util.HtmlUtil"%>
<%
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<Item4> listItem = (List<Item4>) request.getAttribute("listItem");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tìm kiếm sản phẩm</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">TÌM KIẾM HÀNG HÓA</h2>
            <a href="<%=request.getContextPath()%>/customer" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/customer/search" method="get" class="form-inline">
            <div class="field" style="flex:1; min-width: 240px;">
                <label for="keyword">Nhập tên sản phẩm</label>
                <input id="keyword" type="text" name="keyword" class="form-control"
                       placeholder="Ví dụ: thịt, sữa, hạt điều..." value="<%=HtmlUtil.esc(keyword)%>">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>

        <% if (listItem == null || listItem.isEmpty()) { %>
            <div class="alert alert-error">Không tìm thấy sản phẩm nào phù hợp với từ khóa "<%=HtmlUtil.esc(keyword)%>".</div>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã SP</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá bán</th>
                        <th>Số lượng tồn</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% int idx = 1; for (Item4 item : listItem) { %>
                        <tr>
                            <td><%=idx++%></td>
                            <td><%=HtmlUtil.itemCode(item.getId())%></td>
                            <td><strong><%=HtmlUtil.esc(item.getName())%></strong></td>
                            <td><%=HtmlUtil.money(item.getPrice())%></td>
                            <td><%=item.getQuantity()%></td>
                            <td>
                                <% if (item.getQuantity() > 0) { %>
                                    <span class="badge badge-ok">Còn hàng</span>
                                <% } else { %>
                                    <span class="badge badge-out">Hết hàng</span>
                                <% } %>
                            </td>
                            <td>
                                <a href="<%=request.getContextPath()%>/customer/item?id=<%=item.getId()%>&keyword=<%=java.net.URLEncoder.encode(keyword, "UTF-8")%>"
                                   class="btn btn-info btn-sm">Xem chi tiết</a>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</div>
</body>
</html>
