<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.SaleInvoice4, model.User4, util.HtmlUtil"%>
<%
    User4 user = (User4) session.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<SaleInvoice4> recentInvoices = (List<SaleInvoice4>) request.getAttribute("recentInvoices");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Nhân viên Bán hàng</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">TRANG CHỦ NHÂN VIÊN BÁN HÀNG</h2>
        </div>
        <p class="muted">Xin chào <strong><%=HtmlUtil.esc(user.getName())%></strong>. Chọn chức năng bán hàng tại quầy để quét sản phẩm và lập hóa đơn cho khách.</p>

        <div class="grid" style="margin-top: 16px;">
            <a href="<%=request.getContextPath()%>/saler/sale" class="menu-card">
                <h3>Bán hàng tại quầy (POS)</h3>
                <p>Quét mã / tìm sản phẩm, cập nhật hóa đơn tạm và xuất bill thanh toán.</p>
            </a>
        </div>
    </div>

    <div class="card">
        <h3 style="margin-top:0;">Hóa đơn bán hàng gần đây</h3>
        <% if (recentInvoices == null || recentInvoices.isEmpty()) { %>
            <p class="muted">Chưa có hóa đơn bán hàng nào.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mã hóa đơn</th>
                        <th>Ngày bán</th>
                        <th>Nhân viên</th>
                        <th>Khách hàng</th>
                        <th>Tổng tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (SaleInvoice4 si : recentInvoices) { %>
                        <tr>
                            <td>#<%=si.getId()%></td>
                            <td><%=si.getDatetime()%></td>
                            <td><%=si.getSaler() != null ? HtmlUtil.esc(si.getSaler().getName()) : ""%></td>
                            <td><%=si.getCustomer() != null ? HtmlUtil.esc(si.getCustomer().getName()) + " (" + HtmlUtil.esc(si.getCustomer().getAccCode()) + ")" : "Khách lẻ"%></td>
                            <td><strong><%=HtmlUtil.money(si.getTotalAmount())%></strong></td>
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
