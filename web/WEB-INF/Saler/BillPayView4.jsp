<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.SaleInvoice4, model.SaleInvoiceItem4, util.HtmlUtil"%>
<%
    @SuppressWarnings("unchecked")
    List<SaleInvoiceItem4> saleBill = (List<SaleInvoiceItem4>) request.getAttribute("saleBill");
    Integer billTotal = (Integer) request.getAttribute("billTotal");
    if (billTotal == null) billTotal = 0;
    SaleInvoice4 completed = (SaleInvoice4) request.getAttribute("completedInvoice");
    String paymentMethod = (String) request.getAttribute("paymentMethod");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán hóa đơn tại quầy</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 780px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">XÁC NHẬN THANH TOÁN HÓA ĐƠN</h2>
            <a href="<%=request.getContextPath()%>/saler/sale" class="btn btn-secondary btn-sm">Quay lại quầy</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>

        <% if (completed != null) { %>
            <h3>Hóa đơn thanh toán #<%=completed.getId()%></h3>
            <p>
                <strong>Ngày lập:</strong> <%=completed.getDatetime()%> |
                <strong>Thu ngân:</strong> <%=HtmlUtil.esc(completed.getSaler().getName())%> |
                <strong>Phương thức:</strong> <%=HtmlUtil.esc(paymentMethod)%> |
                <strong>Khách hàng:</strong> <%=completed.getCustomer() != null ? HtmlUtil.esc(completed.getCustomer().getName()) : "Khách lẻ"%>
            </p>
            <table>
                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>SL</th>
                    <th>Thành tiền</th>
                </tr>
                </thead>
                <tbody>
                <% for (SaleInvoiceItem4 sii : completed.getItems()) { %>
                    <tr>
                        <td><%=HtmlUtil.esc(sii.getItem().getName())%></td>
                        <td><%=HtmlUtil.money(sii.getItem().getPrice())%></td>
                        <td><%=sii.getQuantity()%></td>
                        <td><%=HtmlUtil.money(sii.getTotalAmount())%></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            <div style="margin-top:16px; font-size:1.3rem; font-weight:700; color:var(--primary);">
                Tổng tiền đã thanh toán: <%=HtmlUtil.money(completed.getTotalAmount())%>
            </div>
            <div class="actions-row">
                <button type="button" onclick="window.print()" class="btn btn-primary">In hóa đơn</button>
                <a href="<%=request.getContextPath()%>/saler/sale" class="btn btn-secondary">Tạo giao dịch mới</a>
                <a href="<%=request.getContextPath()%>/saler" class="btn btn-secondary">Về trang chủ</a>
            </div>
        <% } else if (saleBill == null || saleBill.isEmpty()) { %>
            <p class="muted">Không có sản phẩm nào trong hóa đơn hiện tại.</p>
            <a href="<%=request.getContextPath()%>/saler/sale" class="btn btn-primary">Quay lại quét sản phẩm</a>
        <% } else { %>
            <table>
                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>SL</th>
                    <th>Thành tiền</th>
                </tr>
                </thead>
                <tbody>
                <% for (SaleInvoiceItem4 sii : saleBill) { %>
                    <tr>
                        <td><%=HtmlUtil.esc(sii.getItem().getName())%></td>
                        <td><%=HtmlUtil.money(sii.getItem().getPrice())%></td>
                        <td><%=sii.getQuantity()%></td>
                        <td><%=HtmlUtil.money(sii.getTotalAmount())%></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            <div style="margin-top:16px; font-size:1.3rem; font-weight:700; color:var(--primary);">
                Tổng thanh toán: <%=HtmlUtil.money(billTotal)%>
            </div>

            <form action="<%=request.getContextPath()%>/saler/bill" method="post" style="margin-top:18px;">
                <div class="form-group">
                    <label for="paymentMethod">Phương thức thanh toán</label>
                    <select id="paymentMethod" name="paymentMethod" class="form-control">
                        <option value="Tiền mặt">Tiền mặt</option>
                        <option value="Ví điện tử / QR">Ví điện tử / QR</option>
                    </select>
                </div>
                <div class="actions-row">
                    <button type="submit" class="btn btn-primary">Xác nhận hoàn tất giao dịch</button>
                    <a href="<%=request.getContextPath()%>/saler/sale" class="btn btn-secondary">Quay lại chỉnh sửa</a>
                </div>
            </form>
        <% } %>
    </div>
</div>
</body>
</html>
