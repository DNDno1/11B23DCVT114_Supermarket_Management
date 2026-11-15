<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Supplier4, util.HtmlUtil"%>
<%
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
    @SuppressWarnings("unchecked")
    List<Supplier4> suppliers = (List<Supplier4>) request.getAttribute("suppliers");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý nhà cung cấp</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
</head>
<body>
<%@include file="/WEB-INF/includes/navbar.jspf"%>
<div class="container">
    <div class="card">
        <div class="page-header">
            <h2 class="page-title">QUẢN LÝ THÔNG TIN NHÀ CUNG CẤP</h2>
            <div class="actions-row" style="margin:0;">
                <a href="<%=request.getContextPath()%>/warehouse/suppliers/add" class="btn btn-primary btn-sm">+ Thêm nhà cung cấp</a>
                <a href="<%=request.getContextPath()%>/warehouse/manage" class="btn btn-secondary btn-sm">Quay lại</a>
            </div>
        </div>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%=HtmlUtil.esc(request.getAttribute("success"))%></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%=HtmlUtil.esc(request.getAttribute("error"))%></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/warehouse/suppliers" method="get" class="form-inline">
            <div class="field" style="flex:1; min-width:220px;">
                <label for="keyword">Tìm kiếm nhà cung cấp</label>
                <input id="keyword" type="text" name="keyword" class="form-control"
                       placeholder="Nhập tên, địa chỉ hoặc số điện thoại..." value="<%=HtmlUtil.esc(keyword)%>">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Mã NCC</th>
                    <th>Tên nhà cung cấp</th>
                    <th>Địa chỉ</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (suppliers == null || suppliers.isEmpty()) { %>
                    <tr><td colspan="6" class="muted">Không có nhà cung cấp nào.</td></tr>
                <% } else { for (Supplier4 s : suppliers) { %>
                    <tr>
                        <td>SUP<%=s.getId()%></td>
                        <td><strong><%=HtmlUtil.esc(s.getName())%></strong></td>
                        <td><%=HtmlUtil.esc(s.getAddress())%></td>
                        <td><%=HtmlUtil.esc(s.getEmail())%></td>
                        <td><%=HtmlUtil.esc(s.getPhonenumber())%></td>
                        <td>
                            <a href="<%=request.getContextPath()%>/warehouse/suppliers/edit?id=<%=s.getId()%>" class="btn btn-warning btn-sm">Sửa</a>
                            <a href="<%=request.getContextPath()%>/warehouse/suppliers/delete?id=<%=s.getId()%>" class="btn btn-danger btn-sm">Xóa</a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
