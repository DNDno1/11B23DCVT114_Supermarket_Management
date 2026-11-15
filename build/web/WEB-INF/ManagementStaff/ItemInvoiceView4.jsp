<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.ImportInvoice4, model.ItemStatistic4, util.HtmlUtil"%>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    ItemStatistic4 itemStat = (ItemStatistic4) request.getAttribute("itemStat");
    @SuppressWarnings("unchecked")
    List<ImportInvoice4> invoices = (List<ImportInvoice4>) request.getAttribute("invoices");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết thống kê sản phẩm</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">CHI TIẾT THỐNG KÊ SẢN PHẨM</h2>
            <a href="<%=request.getContextPath()%>/manager/stats/items?startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
               class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <% if (itemStat != null) { %>
            <div class="grid" style="margin-bottom: 18px;">
                <div class="stat-card">
                    <div class="stat-label">Sản phẩm</div>
                    <div class="stat-value" style="font-size:1.1rem;"><%=HtmlUtil.itemCode(itemStat.getId())%> - <%=HtmlUtil.esc(itemStat.getName())%></div>
                </div>
                <div class="stat-card">
                    <div class="stat-label">Giá bán / Tồn kho</div>
                    <div class="stat-value" style="font-size:1.1rem;"><%=HtmlUtil.money(itemStat.getPrice())%> (Tồn: <%=itemStat.getQuantity()%>)</div>
                </div>
                <div class="stat-card">
                    <div class="stat-label">Số lượng đã bán</div>
                    <div class="stat-value"><%=itemStat.getSoldQuantity()%></div>
                </div>
                <div class="stat-card">
                    <div class="stat-label">Tổng doanh thu (<%=HtmlUtil.esc(startDate)%> → <%=HtmlUtil.esc(endDate)%>)</div>
                    <div class="stat-value" style="color:var(--primary);"><%=HtmlUtil.money(itemStat.getRevenue())%></div>
                </div>
            </div>
        <% } %>

        <h3>Danh sách các lượt nhập hàng trong khoảng thời gian</h3>
        <% if (invoices == null || invoices.isEmpty()) { %>
            <p class="muted">Không có lượt nhập hàng nào trong khoảng thời gian đã chọn.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã hóa đơn</th>
                        <th>Ngày nhập</th>
                        <th>Ngày tới hạn</th>
                        <th>Số lượng nhập</th>
                        <th>Tổng tiền nhập</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% int idx = 1; for (ImportInvoice4 inv : invoices) { %>
                        <tr>
                            <td><%=idx++%></td>
                            <td><%=HtmlUtil.invoiceCode(inv.getId())%></td>
                            <td><%=inv.getDatetime()%></td>
                            <td><%=inv.getDuetime() != null ? inv.getDuetime() : ""%></td>
                            <td><%=inv.getQuantity()%></td>
                            <td><%=HtmlUtil.money(inv.getTotalAmount())%></td>
                            <td>
                                <a href="<%=request.getContextPath()%>/manager/stats/invoice-detail?id=<%=inv.getId()%>&from=item&refId=<%=itemStat != null ? itemStat.getId() : 0%>&startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
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
