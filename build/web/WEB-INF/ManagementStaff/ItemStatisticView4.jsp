<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.ItemStatistic4, util.HtmlUtil"%>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<ItemStatistic4> itemStats = (List<ItemStatistic4>) request.getAttribute("itemStats");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống kê sản phẩm theo doanh thu</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">THỐNG KÊ SẢN PHẨM THEO DOANH THU</h2>
            <a href="<%=request.getContextPath()%>/manager/stats" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/manager/stats/items" method="get" class="form-inline">
            <div class="field">
                <label for="startDate">Ngày bắt đầu</label>
                <input id="startDate" type="date" name="startDate" class="form-control" value="<%=HtmlUtil.esc(startDate)%>" required>
            </div>
            <div class="field">
                <label for="endDate">Ngày kết thúc</label>
                <input id="endDate" type="date" name="endDate" class="form-control" value="<%=HtmlUtil.esc(endDate)%>" required>
            </div>
            <div class="field" style="flex:1; min-width: 200px;">
                <label for="keyword">Từ khóa sản phẩm</label>
                <input id="keyword" type="text" name="keyword" class="form-control" placeholder="Tìm tên sản phẩm..." value="<%=HtmlUtil.esc(keyword)%>">
            </div>
            <button type="submit" class="btn btn-primary">Xác nhận &amp; Tìm kiếm</button>
        </form>

        <% if (itemStats != null) { %>
            <% if (itemStats.isEmpty()) { %>
                <p class="muted">Không có dữ liệu sản phẩm phù hợp trong khoảng thời gian này.</p>
            <% } else { %>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã SP</th>
                            <th>Tên sản phẩm</th>
                            <th>Nhà cung cấp</th>
                            <th>Giá bán</th>
                            <th>Đã bán</th>
                            <th>Tổng doanh thu</th>
                            <th>Chi tiết</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% int idx = 1; for (ItemStatistic4 st : itemStats) { %>
                            <tr>
                                <td><%=idx++%></td>
                                <td><%=HtmlUtil.itemCode(st.getId())%></td>
                                <td><strong><%=HtmlUtil.esc(st.getName())%></strong></td>
                                <td><%=st.getSupplier() != null ? HtmlUtil.esc(st.getSupplier().getName()) : ""%></td>
                                <td><%=HtmlUtil.money(st.getPrice())%></td>
                                <td><%=st.getSoldQuantity()%></td>
                                <td><strong><%=HtmlUtil.money(st.getRevenue())%></strong></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/manager/stats/item-invoices?itemId=<%=st.getId()%>&startDate=<%=HtmlUtil.esc(startDate)%>&endDate=<%=HtmlUtil.esc(endDate)%>"
                                       class="btn btn-info btn-sm">Xem lượt nhập</a>
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
