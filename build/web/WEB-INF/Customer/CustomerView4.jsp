<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Customer4, model.Order4, model.OrderItem4, util.HtmlUtil"%>
<%
    Customer4 customer = (Customer4) session.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<OrderItem4> cart = (List<OrderItem4>) session.getAttribute("cart");
    int cartCount = cart != null ? cart.size() : 0;
    @SuppressWarnings("unchecked")
    List<Order4> orders = (List<Order4>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Khách hàng - Siêu thị Trực tuyến</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">TRANG CHỦ KHÁCH HÀNG</h2>
            <span class="badge badge-ok">Mã thành viên: <%=HtmlUtil.esc(customer.getAccCode())%></span>
        </div>
        <p class="muted">Xin chào <strong><%=HtmlUtil.esc(customer.getName())%></strong>! Chọn chức năng bên dưới để tìm kiếm sản phẩm hoặc đặt hàng trực tuyến.</p>

        <div class="grid" style="margin-top: 16px;">
            <a href="<%=request.getContextPath()%>/customer/search" class="menu-card">
                <h3>Tìm kiếm sản phẩm</h3>
                <p>Tra cứu hàng hóa theo tên hoặc từ khóa, xem giá bán, tình trạng tồn kho và thông tin nhà sản xuất.</p>
            </a>
            <a href="<%=request.getContextPath()%>/customer/order" class="menu-card">
                <h3>Giỏ hàng &amp; Đặt hàng online (<%=cartCount%> món)</h3>
                <p>Kiểm tra giỏ hàng hiện tại, điều chỉnh số lượng và xác nhận đặt mua hàng trực tuyến.</p>
            </a>
        </div>
    </div>

    <div class="card">
        <h3 style="margin-top: 0;">Đơn hàng trực tuyến của bạn</h3>
        <% if (orders == null || orders.isEmpty()) { %>
            <p class="muted">Bạn chưa có đơn hàng trực tuyến nào.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Ngày đặt</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Order4 o : orders) { %>
                        <tr>
                            <td>#<%=o.getId()%></td>
                            <td><%=o.getDatetime()%></td>
                            <td><%=HtmlUtil.money(o.getTotalAmount())%></td>
                            <td>
                                <span class="badge <%="Pending".equalsIgnoreCase(o.getStatus()) ? "badge-pending" : "badge-ok"%>">
                                    <%=HtmlUtil.esc(o.getStatus())%>
                                </span>
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
