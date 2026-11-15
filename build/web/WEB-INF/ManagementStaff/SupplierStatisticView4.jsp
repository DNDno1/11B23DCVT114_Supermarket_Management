<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.SupplierStatistic4, util.HtmlUtil"%>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<SupplierStatistic4> supplierStats = (List<SupplierStatistic4>) request.getAttribute("supplierStats");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống kê nhà cung cấp theo doanh thu</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">THỐNG KÊ NHÀ CUNG CẤP THEO DOANH THU</h2>
            <a href="<%=request.getContextPath()%>/manager/stats" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/manager/stats/suppliers" method="get" class="form-inline">
            <div class="field">
                <label for="startDate">Ngày bắt đầu</label>
                <input id="startDate" type="date" name="startDate" class="form-control" value="<%=HtmlUtil.esc(startDate)%>" required>
            </div>
            <div class="field">
                <label for="endDate">Ngày kết thúc</label>
                <input id="endDate" type="date" name="endDate" class="form-control" value="<%=HtmlUtil.esc(endDate)%>" required>
            </div>
            <div class="field" style="flex:1; min-width: 200px;">
                <label for="keyword">Tên nhà cung cấp</label>
                <input id="keyword" type="text" name="keyword" class="form-control" placeholder="Tìm nhà cung cấp..." value="<%=HtmlUtil.esc(keyword)%>">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>

        <% if (supplierStats != null) { %>
            <% if (supplierStats.isEmpty()) { %>
                <p class="muted">Không có nhà cung cấp nào phù hợp trong khoảng thời gian đã chọn.</p>
            <% } else { %>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã NCC</th>
                            <th>Tên nhà cung cấp</th>
                            <th>Địa chỉ</th>
                            <th>Số điện thoại</th>
                            <th>SL đã bán</th>
                            <th>Tổng chi nhập</th>
                            <th>Doanh thu tổng</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (SupplierStatistic4 s : supplierStats) { %>
                            <tr>
                                <td>SUP<%=s.getId()%></td>
                                <td><strong><%=HtmlUtil.esc(s.getName())%></strong></td>
                                <td><%=HtmlUtil.esc(s.getAddress())%></td>
                                <td><%=HtmlUtil.esc(s.getPhonenumber())%></td>
                                <td><%=s.getTotalSoldQty()%></td>
                                <td><%=HtmlUtil.money(s.getTotalImportAmount())%></td>
                                <td><strong><%=HtmlUtil.money(s.getRevenue())%></strong></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/manager/stats/supplier-invoices?supplierId=<%=s.getId()%>&startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
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
