<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Order4, model.OrderItem4, util.HtmlUtil"%>
<%
    @SuppressWarnings("unchecked")
    List<Order4> orders = (List<Order4>) request.getAttribute("orders");
    Order4 selectedOrder = (Order4) request.getAttribute("selectedOrder");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Duyệt đơn hàng online &amp; Xuất hàng</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">DUYỆT ĐƠN HÀNG TRỰC TUYẾN &amp; XUẤT GIAO HÀNG</h2>
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
                            <a href="<%=request.getContextPath()%>/warehouse/orders?id=<%=o.getId()%>" class="btn btn-info btn-sm">Xem chi tiết</a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>

    <% if (selectedOrder != null) { %>
        <div class="card">
            <div class="page-header">
                <h3 class="page-title">Chi tiết đơn hàng #<%=selectedOrder.getId()%></h3>
                <span class="badge <%="Pending".equalsIgnoreCase(selectedOrder.getStatus()) ? "badge-pending" : "badge-ok"%>">
                    <%=HtmlUtil.esc(selectedOrder.getStatus())%>
                </span>
            </div>
            <p>
                <strong>Khách hàng:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getName())%> |
                <strong>SĐT:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getPhonenumber())%> |
                <strong>Địa chỉ giao:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getAddress())%>
            </p>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Tên sản phẩm</th>
                        <th>SL đặt</th>
                        <th>Tồn kho hiện tại</th>
                        <th>Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (OrderItem4 oi : selectedOrder.getItems()) { %>
                        <tr>
                            <td><%=HtmlUtil.itemCode(oi.getItem().getId())%></td>
                            <td><%=HtmlUtil.esc(oi.getItem().getName())%></td>
                            <td><%=oi.getQuantity()%></td>
                            <td><%=oi.getItem().getQuantity()%></td>
                            <td><%=HtmlUtil.money(oi.getTotalAmount())%></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <% if ("Pending".equalsIgnoreCase(selectedOrder.getStatus())) { %>
                <form action="<%=request.getContextPath()%>/warehouse/orders/export" method="post" class="actions-row">
                    <input type="hidden" name="orderId" value="<%=selectedOrder.getId()%>">
                    <button type="submit" name="status" value="Exported" class="btn btn-primary">Đóng gói &amp; Xuất cho giao hàng</button>
                    <button type="submit" name="status" value="Completed" class="btn btn-warning">Hoàn thành đơn</button>
                </form>
            <% } %>
        </div>
    <% } %>
</div>
</body>
</html>
