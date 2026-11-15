<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Item4, model.Supplier4, util.HtmlUtil"%>
<%
    Item4 item = (Item4) request.getAttribute("item");
    if (item == null) item = new Item4();
    @SuppressWarnings("unchecked")
    List<Supplier4> suppliers = (List<Supplier4>) request.getAttribute("suppliers");
    int selectedSupId = item.getSupplier() != null ? item.getSupplier().getId() : 0;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm hàng hóa mới</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container" style="max-width: 680px;">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">THÊM HÀNG HÓA MỚI</h2>
            <a href="<%=request.getContextPath()%>/warehouse/items" class="btn btn-secondary btn-sm">Quay lại</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/warehouse/items/prepare" method="post">
            <input type="hidden" name="mode" value="ADD">
            <div class="form-group">
                <label for="name">Tên sản phẩm *</label>
                <input id="name" type="text" name="name" class="form-control" value="<%=HtmlUtil.esc(item.getName())%>" required>
            </div>
            <div class="grid">
                <div class="form-group">
                    <label for="importPrice">Giá nhập (VNĐ) *</label>
                    <input id="importPrice" type="number" name="importPrice" class="form-control"
                           value="<%=item.getImportPrice() > 0 ? item.getImportPrice() : ""%>" min="1" required>
                </div>
                <div class="form-group">
                    <label for="price">Giá bán (VNĐ) *</label>
                    <input id="price" type="number" name="price" class="form-control"
                           value="<%=item.getPrice() > 0 ? item.getPrice() : ""%>" min="1" required>
                </div>
                <div class="form-group">
                    <label for="quantity">Số lượng ban đầu</label>
                    <input id="quantity" type="number" name="quantity" class="form-control"
                           value="<%=item.getQuantity()%>" min="0" required>
                </div>
            </div>
            <div class="form-group">
                <label for="supplierId">Nhà cung cấp *</label>
                <select id="supplierId" name="supplierId" class="form-control" required>
                    <option value="">-- Chọn nhà cung cấp --</option>
                    <% if (suppliers != null) { for (Supplier4 s : suppliers) { %>
                        <option value="<%=s.getId()%>" <%=s.getId() == selectedSupId ? "selected" : ""%>>
                            <%=HtmlUtil.esc(s.getName())%>
                        </option>
                    <% } } %>
                </select>
            </div>
            <div class="form-group">
                <label for="note">Ghi chú / Mô tả</label>
                <textarea id="note" name="note" rows="3" class="form-control"><%=HtmlUtil.esc(item.getNote())%></textarea>
            </div>
            <div class="actions-row">
                <button type="submit" class="btn btn-primary">Lưu thông tin</button>
                <a href="<%=request.getContextPath()%>/warehouse/items" class="btn btn-secondary">Quay lại</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
