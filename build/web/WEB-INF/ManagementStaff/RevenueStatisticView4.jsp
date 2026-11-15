<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.ImportInvoice4, model.RevenueStatistic4, util.HtmlUtil"%>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    RevenueStatistic4 revStat = (RevenueStatistic4) request.getAttribute("revenueStat");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống kê theo doanh thu</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">THỐNG KÊ THEO DOANH THU</h2>
            <a href="<%=request.getContextPath()%>/manager/stats" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/manager/stats/revenue" method="get" class="form-inline">
            <div class="field">
                <label for="startDate">Ngày bắt đầu</label>
                <input id="startDate" type="date" name="startDate" class="form-control" value="<%=HtmlUtil.esc(startDate)%>" required>
            </div>
            <div class="field">
                <label for="endDate">Ngày kết thúc</label>
                <input id="endDate" type="date" name="endDate" class="form-control" value="<%=HtmlUtil.esc(endDate)%>" required>
            </div>
            <button type="submit" class="btn btn-primary">Xác nhận</button>
        </form>

        <% if (revStat != null) { %>
            <div class="grid" style="margin-bottom: 20px;">
                <div class="stat-card">
                    <div class="stat-label">Doanh thu bán trực tiếp (Quầy)</div>
                    <div class="stat-value"><%=HtmlUtil.money(revStat.getDirectRevenue())%></div>
                </div>
                <div class="stat-card">
                    <div class="stat-label">Doanh thu bán online</div>
                    <div class="stat-value"><%=HtmlUtil.money(revStat.getOnlineRevenue())%></div>
                </div>
                <div class="stat-card">
                    <div class="stat-label">Tổng doanh thu</div>
                    <div class="stat-value" style="color:var(--primary);"><%=HtmlUtil.money(revStat.getRevenue())%></div>
                </div>
                <div class="stat-card" style="border-left-color: var(--danger);">
                    <div class="stat-label">Tổng chi nhập hàng</div>
                    <div class="stat-value" style="color:var(--danger);"><%=HtmlUtil.money(revStat.getImportCost())%></div>
                </div>
            </div>

            <h3>Danh sách các lượt nhập hàng trong khoảng thời gian</h3>
            <% List<ImportInvoice4> invoices = revStat.getImportInvoices(); %>
            <% if (invoices == null || invoices.isEmpty()) { %>
                <p class="muted">Không có hóa đơn nhập hàng nào trong khoảng thời gian này.</p>
            <% } else { %>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã hóa đơn</th>
                            <th>Sản phẩm</th>
                            <th>Nhà cung cấp</th>
                            <th>Ngày nhập</th>
                            <th>Số lượng</th>
                            <th>Tổng tiền nhập</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (ImportInvoice4 inv : invoices) { %>
                            <tr>
                                <td><%=HtmlUtil.invoiceCode(inv.getId())%></td>
                                <td><%=HtmlUtil.esc(inv.getItem().getName())%></td>
                                <td><%=HtmlUtil.esc(inv.getItem().getSupplier().getName())%></td>
                                <td><%=inv.getDatetime()%></td>
                                <td><%=inv.getQuantity()%></td>
                                <td><%=HtmlUtil.money(inv.getTotalAmount())%></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/manager/stats/invoice-detail?id=<%=inv.getId()%>&from=revenue&startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
                                       class="btn btn-info btn-sm">Xem chi tiết</a>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } %>
    </div>
</div>
</body>
</html>
