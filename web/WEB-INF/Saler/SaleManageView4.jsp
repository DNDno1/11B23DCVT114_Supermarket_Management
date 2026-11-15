<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Item4, model.SaleInvoiceItem4, util.HtmlUtil"%>
<%
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<Item4> items = (List<Item4>) request.getAttribute("items");
    @SuppressWarnings("unchecked")
    List<SaleInvoiceItem4> saleBill = (List<SaleInvoiceItem4>) request.getAttribute("saleBill");
    Integer billTotal = (Integer) request.getAttribute("billTotal");
    if (billTotal == null) billTotal = 0;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bán hàng tại quầy</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">QUẦY THANH TOÁN - QUÉT SẢN PHẨM</h2>
            <a href="<%=request.getContextPath()%>/saler" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <div class="grid" style="grid-template-columns: 1.2fr 1fr; align-items: start;">
            <div>
                <h3 style="margin-top:0;">Quét mã / Chọn sản phẩm</h3>
                <form action="<%=request.getContextPath()%>/saler/sale" method="post" class="form-inline">
                    <input type="hidden" name="action" value="add">
                    <div class="field">
                        <label for="scanId">Mã ID sản phẩm</label>
                        <input id="scanId" type="number" name="itemId" class="form-control" placeholder="VD: 1" min="1" required style="width:120px;">
                    </div>
                    <div class="field">
                        <label for="scanQty">Số lượng</label>
                        <input id="scanQty" type="number" name="quantity" class="form-control" value="1" min="1" required style="width:90px;">
                    </div>
                    <button type="submit" class="btn btn-primary">Quét thêm</button>
                </form>

                <form action="<%=request.getContextPath()%>/saler/sale" method="get" class="form-inline">
                    <div class="field" style="flex:1;">
                        <label for="keyword">Tìm nhanh sản phẩm</label>
                        <input id="keyword" type="text" name="keyword" class="form-control" placeholder="Nhập tên sản phẩm..." value="<%=HtmlUtil.esc(keyword)%>">
                    </div>
                    <button type="submit" class="btn btn-secondary">Tìm</button>
                </form>

                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên SP</th>
                            <th>Giá</th>
                            <th>Tồn</th>
                            <th>Thêm</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% if (items != null) { for (Item4 item : items) { %>
                            <tr>
                                <td><%=item.getId()%></td>
                                <td><%=HtmlUtil.esc(item.getName())%></td>
                                <td><%=HtmlUtil.money(item.getPrice())%></td>
                                <td><%=item.getQuantity()%></td>
                                <td>
                                    <% if (item.getQuantity() > 0) { %>
                                        <form action="<%=request.getContextPath()%>/saler/sale" method="post" style="display:flex; gap:4px;">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="itemId" value="<%=item.getId()%>">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm">+1</button>
                                        </form>
                                    <% } else { %>
                                        <span class="badge badge-out">Hết</span>
                                    <% } %>
                                </td>
                            </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="card" style="background:#f8fafc; margin-bottom:0;">
                <h3 style="margin-top:0;">Danh sách hàng trên hóa đơn</h3>
                <% if (saleBill == null || saleBill.isEmpty()) { %>
                    <p class="muted">Chưa có sản phẩm nào được quét.</p>
                <% } else { %>
                    <table>
                        <thead>
                        <tr>
                            <th>Tên SP</th>
                            <th>SL</th>
                            <th>Thành tiền</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (SaleInvoiceItem4 sii : saleBill) { %>
                            <tr>
                                <td><%=HtmlUtil.esc(sii.getItem().getName())%></td>
                                <td><%=sii.getQuantity()%></td>
                                <td><%=HtmlUtil.money(sii.getTotalAmount())%></td>
                                <td>
                                    <form action="<%=request.getContextPath()%>/saler/sale" method="post">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="itemId" value="<%=sii.getItem().getId()%>">
                                        <button type="submit" class="btn btn-danger btn-sm">X</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                    <div style="margin-top: 14px; font-size:1.25rem; font-weight:700; color:var(--primary);">
                        Tổng cộng: <%=HtmlUtil.money(billTotal)%>
                    </div>
                    <div class="actions-row">
                        <a href="<%=request.getContextPath()%>/saler/bill" class="btn btn-primary">Xuất Bill thanh toán</a>
                        <form action="<%=request.getContextPath()%>/saler/sale" method="post">
                            <input type="hidden" name="action" value="clear">
                            <button type="submit" class="btn btn-secondary">Xóa hóa đơn</button>
                        </form>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html>
