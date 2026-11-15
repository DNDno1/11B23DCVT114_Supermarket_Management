<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Order4, model.OrderItem4, util.HtmlUtil"%>
<%
    Order4 selectedOrder = (Order4) request.getAttribute("selectedOrder");
    boolean allInStock = true;
    if (selectedOrder != null && selectedOrder.getItems() != null) {
        for (OrderItem4 oi : selectedOrder.getItems()) {
            // Lưu ý: Tồn kho đã được giữ/trừ khi khách đặt hàng (hoặc kiểm tra tồn kho >= 0)
            if (oi.getItem().getQuantity() < 0) {
                allInStock = false;
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng - OrderDetailView4</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHI TIẾT ĐƠN ĐẶT HÀNG <%= selectedOrder != null ? ("#" + selectedOrder.getId()) : "" %></h2>
            <a href="<%=request.getContextPath()%>/warehouse/orders" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <% if (selectedOrder == null) { %>
            <p class="muted">Không tìm thấy thông tin đơn hàng.</p>
        <% } else { %>
            <div style="margin-bottom: 16px;">
                <p>
                    <strong>Mã đơn hàng:</strong> #<%=selectedOrder.getId()%> |
                    <strong>Ngày đặt:</strong> <%=selectedOrder.getDatetime()%> |
                    <strong>Trạng thái:</strong>
                    <span class="badge <%="Pending".equalsIgnoreCase(selectedOrder.getStatus()) ? "badge-pending" : "badge-ok"%>">
                        <%=HtmlUtil.esc(selectedOrder.getStatus())%>
                    </span>
                </p>
                <p>
                    <strong>Khách hàng:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getName())%> (<%=HtmlUtil.esc(selectedOrder.getCustomer().getAccCode())%>) |
                    <strong>SĐT:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getPhonenumber())%> |
                    <strong>Địa chỉ giao hàng:</strong> <%=HtmlUtil.esc(selectedOrder.getCustomer().getAddress())%>
                </p>
            </div>

            <div class="alert <%= allInStock ? "alert-success" : "alert-error" %>" style="margin-bottom: 16px;">
                <strong>Hệ thống tự động kiểm tra tồn kho:</strong>
                <%= allInStock
                        ? "Tất cả sản phẩm được yêu cầu trong đơn hàng đều còn đủ hàng trong kho và sẵn sàng xuất đơn."
                        : "Có sản phẩm trong đơn hàng không đủ số lượng tồn kho!" %>
            </div>

            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng yêu cầu</th>
                        <th>Tồn kho hiện tại</th>
                        <th>Trạng thái hàng trong kho</th>
                        <th>Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (OrderItem4 oi : selectedOrder.getItems()) {
                        boolean itemAvailable = oi.getItem().getQuantity() >= 0;
                    %>
                        <tr>
                            <td><%=HtmlUtil.itemCode(oi.getItem().getId())%></td>
                            <td><%=HtmlUtil.esc(oi.getItem().getName())%></td>
                            <td><%=oi.getQuantity()%></td>
                            <td><%=oi.getItem().getQuantity()%></td>
                            <td>
                                <span class="badge <%= itemAvailable ? "badge-ok" : "badge-pending" %>">
                                    <%= itemAvailable ? "Còn hàng (Đủ điều kiện xuất)" : "Hết hàng" %>
                                </span>
                            </td>
                            <td><%=HtmlUtil.money(oi.getTotalAmount())%></td>
                        </tr>
                    <% } %>
                    </tbody>
                    <tfoot>
                    <tr>
                        <th colspan="5" style="text-align:right;">Tổng tiền đơn hàng:</th>
                        <th><%=HtmlUtil.money(selectedOrder.getTotalAmount())%></th>
                    </tr>
                    </tfoot>
                </table>
            </div>

            <div class="actions-row" style="margin-top: 20px;">
                <% if ("Pending".equalsIgnoreCase(selectedOrder.getStatus()) && allInStock) { %>
                    <form action="<%=request.getContextPath()%>/warehouse/orders/export" method="post" style="display:inline;">
                        <input type="hidden" name="orderId" value="<%=selectedOrder.getId()%>">
                        <input type="hidden" name="status" value="Exported">
                        <button type="submit" class="btn btn-primary">Xác nhận xuất đơn</button>
                    </form>
                <% } %>
                <a href="<%=request.getContextPath()%>/warehouse/orders" class="btn btn-secondary">Quay lại danh sách đơn hàng</a>
            </div>
        <% } %>
    </div>
</div>
</body>
</html>
