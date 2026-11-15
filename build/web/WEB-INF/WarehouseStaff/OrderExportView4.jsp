<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Order4, util.HtmlUtil"%>
<%
    @SuppressWarnings("unchecked")
    List<Order4> orders = (List<Order4>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giao diện duyệt đơn hàng - OrderExportView4</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">DUYỆT ĐƠN ĐẶT HÀNG TRỰC TUYẾN</h2>
            <a href="<%=request.getContextPath()%>/warehouse" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Mã đơn</th>
                    <th>Ngày đặt</th>
                    <th>Khách hàng</th>
                    <th>Địa chỉ / SĐT</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (orders == null || orders.isEmpty()) { %>
                    <tr><td colspan="7" class="muted">Không có đơn hàng trực tuyến nào.</td></tr>
                <% } else { for (Order4 o : orders) { %>
                    <tr>
                        <td><strong>#<%=o.getId()%></strong></td>
                        <td><%=o.getDatetime()%></td>
                        <td><%=HtmlUtil.esc(o.getCustomer().getName())%> (<%=HtmlUtil.esc(o.getCustomer().getAccCode())%>)</td>
                        <td><%=HtmlUtil.esc(o.getCustomer().getAddress())%> - <%=HtmlUtil.esc(o.getCustomer().getPhonenumber())%></td>
                        <td><%=HtmlUtil.money(o.getTotalAmount())%></td>
                        <td>
                            <span class="badge <%="Pending".equalsIgnoreCase(o.getStatus()) ? "badge-pending" : "badge-ok"%>">
                                <%=HtmlUtil.esc(o.getStatus())%>
                            </span>
                        </td>
                        <td>
                            <form action="<%=request.getContextPath()%>/warehouse/orders/detail" method="get" style="display:inline;">
                                <input type="hidden" name="id" value="<%=o.getId()%>">
                                <button type="submit" class="btn btn-info btn-sm">Xem chi tiết đơn hàng</button>
                            </form>
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
