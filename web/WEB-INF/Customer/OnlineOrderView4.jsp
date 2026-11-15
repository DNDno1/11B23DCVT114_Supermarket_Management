<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Order4, model.OrderItem4, util.HtmlUtil"%>
<%
    @SuppressWarnings("unchecked")
    List<OrderItem4> cart = (List<OrderItem4>) request.getAttribute("cart");
    Integer totalAmount = (Integer) request.getAttribute("totalAmount");
    if (totalAmount == null) totalAmount = 0;
    @SuppressWarnings("unchecked")
    List<Order4> orders = (List<Order4>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt hàng trực tuyến</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">GIỎ HÀNG &amp; ĐẶT HÀNG TRỰC TUYẾN</h2>
            <a href="<%=request.getContextPath()%>/customer/search" class="btn btn-secondary btn-sm">Tiếp tục tìm kiếm</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>

        <% if (cart == null || cart.isEmpty()) { %>
            <p class="muted">Giỏ hàng của bạn đang trống. Hãy tìm kiếm sản phẩm để thêm vào giỏ.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Tên sản phẩm</th>
                        <th>Đơn giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (OrderItem4 oi : cart) { %>
                        <tr>
                            <td><%=HtmlUtil.itemCode(oi.getItem().getId())%></td>
                            <td><%=HtmlUtil.esc(oi.getItem().getName())%></td>
                            <td><%=HtmlUtil.money(oi.getItem().getPrice())%></td>
                            <td>
                                <form action="<%=request.getContextPath()%>/customer/order" method="post" style="display:flex; gap:6px; align-items:center;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="itemId" value="<%=oi.getItem().getId()%>">
                                    <input type="number" name="quantity" value="<%=oi.getQuantity()%>" min="1"
                                           class="form-control" style="width: 80px;">
                                    <button type="submit" class="btn btn-secondary btn-sm">Cập nhật</button>
                                </form>
                            </td>
                            <td><strong><%=HtmlUtil.money(oi.getTotalAmount())%></strong></td>
                            <td>
                                <form action="<%=request.getContextPath()%>/customer/order" method="post">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="itemId" value="<%=oi.getItem().getId()%>">
                                    <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div style="margin-top: 18px; display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:12px;">
                <div>
                    <span class="muted">Tổng tiền tạm tính:</span>
                    <div style="font-size:1.45rem; font-weight:700; color:var(--primary);"><%=HtmlUtil.money(totalAmount)%></div>
                </div>
                <form action="<%=request.getContextPath()%>/customer/order" method="post" class="form-inline" style="margin:0;">
                    <input type="hidden" name="action" value="confirm">
                    <div class="field">
                        <label for="paymentMethod">Phương thức thanh toán</label>
                        <select id="paymentMethod" name="paymentMethod" class="form-control">
                            <option value="COD">Thanh toán khi nhận hàng (COD)</option>
                            <option value="EWallet">Ví điện tử / Chuyển khoản</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Xác nhận đặt hàng</button>
                </form>
            </div>
        <% } %>
    </div>

    <div class="card">
        <h3 style="margin-top:0;">Lịch sử đơn hàng trực tuyến</h3>
        <% if (orders == null || orders.isEmpty()) { %>
            <p class="muted">Chưa có đơn hàng nào.</p>
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
