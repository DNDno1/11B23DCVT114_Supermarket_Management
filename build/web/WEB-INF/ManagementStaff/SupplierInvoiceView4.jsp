<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.ImportInvoice4, model.SupplierStatistic4, util.HtmlUtil"%>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    SupplierStatistic4 sup = (SupplierStatistic4) request.getAttribute("supplierStat");
    @SuppressWarnings("unchecked")
    List<ImportInvoice4> invoices = (List<ImportInvoice4>) request.getAttribute("invoices");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết thống kê nhà cung cấp</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHI TIẾT NHÀ CUNG CẤP &amp; HÓA ĐƠN NHẬP</h2>
            <a href="<%=request.getContextPath()%>/manager/stats/suppliers?startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
               class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <% if (sup != null) { %>
            <table style="margin-bottom: 18px;">
                <tbody>
                <tr>
                    <th style="width: 220px;">Mã nhà cung cấp</th>
                    <td>SUP<%=sup.getId()%></td>
                    <th style="width: 200px;">Số điện thoại</th>
                    <td><%=HtmlUtil.esc(sup.getPhonenumber())%></td>
                </tr>
                <tr>
                    <th>Tên nhà cung cấp</th>
                    <td><strong><%=HtmlUtil.esc(sup.getName())%></strong></td>
                    <th>Email</th>
                    <td><%=HtmlUtil.esc(sup.getEmail())%></td>
                </tr>
                <tr>
                    <th>Địa chỉ</th>
                    <td><%=HtmlUtil.esc(sup.getAddress())%></td>
                    <th>Doanh thu tổng (<%=HtmlUtil.esc(startDate)%> → <%=HtmlUtil.esc(endDate)%>)</th>
                    <td style="font-weight:700; color:var(--primary);"><%=HtmlUtil.money(sup.getRevenue())%></td>
                </tr>
                <tr>
                    <th>Ghi chú</th>
                    <td colspan="3"><%=HtmlUtil.esc(sup.getNote())%></td>
                </tr>
                </tbody>
            </table>
        <% } %>

        <h3>Danh sách hóa đơn nhập hàng của nhà cung cấp</h3>
        <% if (invoices == null || invoices.isEmpty()) { %>
            <p class="muted">Không có hóa đơn nhập hàng nào trong khoảng thời gian đã chọn.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã hóa đơn</th>
                        <th>Ngày nhập</th>
                        <th>Mặt hàng</th>
                        <th>SL nhập</th>
                        <th>Tổng tiền nhập</th>
                        <th>Ngày hết hạn</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% int idx = 1; for (ImportInvoice4 inv : invoices) { %>
                        <tr>
                            <td><%=idx++%></td>
                            <td><%=HtmlUtil.invoiceCode(inv.getId())%></td>
                            <td><%=inv.getDatetime()%></td>
                            <td><%=HtmlUtil.esc(inv.getItem().getName())%></td>
                            <td><%=inv.getQuantity()%></td>
                            <td><%=HtmlUtil.money(inv.getTotalAmount())%></td>
                            <td><%=inv.getDuetime() != null ? inv.getDuetime() : ""%></td>
                            <td>
                                <a href="<%=request.getContextPath()%>/manager/stats/invoice-detail?id=<%=inv.getId()%>&from=supplier&refId=<%=sup != null ? sup.getId() : 0%>&startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
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
