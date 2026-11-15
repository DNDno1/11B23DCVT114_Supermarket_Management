package web;

import dao.ImportInvoiceDAO4;
import dao.ItemDAO4;
import dao.OrderDAO4;
import dao.SupplierDAO4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.ImportInvoice4;
import model.Item4;
import model.Order4;
import model.Supplier4;
import model.WarehouseStaff4;

/**
 * Handles Warehouse Staff flows: Item/Supplier CRUD with Confirmation step,
 * Importing goods from suppliers, and Exporting online orders to delivery staff.
 */
@WebServlet(name = "WarehouseServlet", urlPatterns = {"/warehouse", "/warehouse/*"})
public class WarehouseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ItemDAO4 itemDAO = new ItemDAO4();
    private final SupplierDAO4 supplierDAO = new SupplierDAO4();
    private final ImportInvoiceDAO4 importInvoiceDAO = new ImportInvoiceDAO4();
    private final OrderDAO4 orderDAO = new OrderDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "WarehouseStaff")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/WarehouseStaffView4.jsp").forward(request, response);
        } else if ("/manage".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ManageView4.jsp").forward(request, response);
        } else if ("/items".equals(pathInfo)) {
            showItemManage(request, response);
        } else if ("/items/add".equals(pathInfo)) {
            showItemAdd(request, response);
        } else if ("/items/edit".equals(pathInfo)) {
            showItemEdit(request, response);
        } else if ("/items/delete".equals(pathInfo)) {
            prepareItemDelete(request, response);
        } else if ("/suppliers".equals(pathInfo)) {
            showSupplierManage(request, response);
        } else if ("/suppliers/add".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/SupplierAddView4.jsp").forward(request, response);
        } else if ("/suppliers/edit".equals(pathInfo)) {
            showSupplierEdit(request, response);
        } else if ("/suppliers/delete".equals(pathInfo)) {
            prepareSupplierDelete(request, response);
        } else if ("/import".equals(pathInfo)) {
            showImportSearch(request, response);
        } else if ("/import/item".equals(pathInfo)) {
            showItemImport(request, response);
        } else if ("/orders".equals(pathInfo)) {
            showOrders(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/warehouse");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "WarehouseStaff")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if ("/items/prepare".equals(pathInfo)) {
            prepareItemSave(request, response);
        } else if ("/suppliers/prepare".equals(pathInfo)) {
            prepareSupplierSave(request, response);
        } else if ("/confirm".equals(pathInfo)) {
            executeConfirm(request, response);
        } else if ("/import/item".equals(pathInfo)) {
            executeImport(request, response);
        } else if ("/orders/export".equals(pathInfo)) {
            exportOrder(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/warehouse");
        }
    }

    private void consumeFlash(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object msg = session.getAttribute("flashSuccess");
            if (msg != null) {
                request.setAttribute("success", msg);
                session.removeAttribute("flashSuccess");
            }
            Object err = session.getAttribute("flashError");
            if (err != null) {
                request.setAttribute("error", err);
                session.removeAttribute("flashError");
            }
        }
    }

    private void showItemManage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consumeFlash(request);
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        try {
            request.setAttribute("keyword", keyword);
            request.setAttribute("items", itemDAO.getItem(keyword));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách hàng hóa: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemManageView4.jsp").forward(request, response);
    }

    private void showItemAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("suppliers", supplierDAO.getSupplier(""));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải nhà cung cấp: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemAddView4.jsp").forward(request, response);
    }

    private void showItemEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            Item4 item = itemDAO.getItemById(id);
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/items");
                return;
            }
            request.setAttribute("item", item);
            request.setAttribute("suppliers", supplierDAO.getSupplier(""));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải thông tin hàng hóa: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemEditView4.jsp").forward(request, response);
    }

    private void prepareItemSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode"); // ADD or EDIT
        int id = WebHelper.parseInt(request.getParameter("id"), 0);
        String name = request.getParameter("name");
        int quantity = WebHelper.parseInt(request.getParameter("quantity"), 0);
        int price = WebHelper.parseInt(request.getParameter("price"), 0);
        int importPrice = WebHelper.parseInt(request.getParameter("importPrice"), 0);
        String note = request.getParameter("note");
        int supplierId = WebHelper.parseInt(request.getParameter("supplierId"), 0);

        Item4 item = new Item4();
        item.setId(id);
        item.setName(name != null ? name.trim() : "");
        item.setQuantity(Math.max(0, quantity));
        item.setPrice(Math.max(0, price));
        item.setImportPrice(Math.max(0, importPrice));
        item.setNote(note != null ? note.trim() : "");

        try {
            Supplier4 sup = supplierDAO.getSupplierById(supplierId);
            item.setSupplier(sup);

            if (item.getName().isEmpty() || sup == null || price <= 0 || importPrice <= 0) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ tên sản phẩm, giá bán, giá nhập (>0) và nhà cung cấp.");
                request.setAttribute("item", item);
                request.setAttribute("suppliers", supplierDAO.getSupplier(""));
                String targetJsp = "EDIT".equals(mode)
                        ? "/WEB-INF/WarehouseStaff/ItemEditView4.jsp"
                        : "/WEB-INF/WarehouseStaff/ItemAddView4.jsp";
                request.getRequestDispatcher(targetJsp).forward(request, response);
                return;
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi kiểm tra dữ liệu: " + e.getMessage());
        }

        HttpSession session = request.getSession();
        String op = "EDIT".equals(mode) ? "EDIT_ITEM" : "ADD_ITEM";
        session.setAttribute("pendingOp", op);
        session.setAttribute("pendingItem", item);
        request.setAttribute("confirmTitle", "EDIT".equals(mode) ? "Xác nhận sửa thông tin hàng hóa" : "Xác nhận thêm hàng hóa mới");
        request.setAttribute("confirmOp", op);
        request.setAttribute("pendingItem", item);
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ConfirmVIew4.jsp").forward(request, response);
    }

    private void prepareItemDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            Item4 item = itemDAO.getItemById(id);
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/items");
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("pendingOp", "DELETE_ITEM");
            session.setAttribute("pendingItem", item);
            request.setAttribute("confirmTitle", "Xác nhận xóa hàng hóa");
            request.setAttribute("confirmOp", "DELETE_ITEM");
            request.setAttribute("pendingItem", item);
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ConfirmVIew4.jsp").forward(request, response);
        } catch (SQLException e) {
            request.getSession().setAttribute("flashError", "Lỗi tải sản phẩm cần xóa: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/warehouse/items");
        }
    }

    private void showSupplierManage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consumeFlash(request);
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        try {
            request.setAttribute("keyword", keyword);
            request.setAttribute("suppliers", supplierDAO.getSupplier(keyword));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách nhà cung cấp: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/SupplierManageView4.jsp").forward(request, response);
    }

    private void showSupplierEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            Supplier4 supplier = supplierDAO.getSupplierById(id);
            if (supplier == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
                return;
            }
            request.setAttribute("supplier", supplier);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải thông tin nhà cung cấp: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/SupplierEditView4.jsp").forward(request, response);
    }

    private void prepareSupplierSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode");
        int id = WebHelper.parseInt(request.getParameter("id"), 0);
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phonenumber = request.getParameter("phonenumber");
        String note = request.getParameter("note");

        Supplier4 sup = new Supplier4();
        sup.setId(id);
        sup.setName(name != null ? name.trim() : "");
        sup.setAddress(address != null ? address.trim() : "");
        sup.setEmail(email != null ? email.trim() : "");
        sup.setPhonenumber(phonenumber != null ? phonenumber.trim() : "");
        sup.setNote(note != null ? note.trim() : "");

        if (sup.getName().isEmpty() || sup.getAddress().isEmpty() || sup.getEmail().isEmpty() || sup.getPhonenumber().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ tên, địa chỉ, email và số điện thoại nhà cung cấp.");
            request.setAttribute("supplier", sup);
            String targetJsp = "EDIT".equals(mode)
                    ? "/WEB-INF/WarehouseStaff/SupplierEditView4.jsp"
                    : "/WEB-INF/WarehouseStaff/SupplierAddView4.jsp";
            request.getRequestDispatcher(targetJsp).forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String op = "EDIT".equals(mode) ? "EDIT_SUPPLIER" : "ADD_SUPPLIER";
        session.setAttribute("pendingOp", op);
        session.setAttribute("pendingSupplier", sup);
        request.setAttribute("confirmTitle", "EDIT".equals(mode) ? "Xác nhận sửa nhà cung cấp" : "Xác nhận thêm nhà cung cấp mới");
        request.setAttribute("confirmOp", op);
        request.setAttribute("pendingSupplier", sup);
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ConfirmVIew4.jsp").forward(request, response);
    }

    private void prepareSupplierDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            Supplier4 sup = supplierDAO.getSupplierById(id);
            if (sup == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("pendingOp", "DELETE_SUPPLIER");
            session.setAttribute("pendingSupplier", sup);
            request.setAttribute("confirmTitle", "Xác nhận xóa nhà cung cấp");
            request.setAttribute("confirmOp", "DELETE_SUPPLIER");
            request.setAttribute("pendingSupplier", sup);
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ConfirmVIew4.jsp").forward(request, response);
        } catch (SQLException e) {
            request.getSession().setAttribute("flashError", "Lỗi tải nhà cung cấp: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
        }
    }

    private void executeConfirm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String decision = request.getParameter("decision"); // confirm, back, cancel
        HttpSession session = request.getSession();
        String op = (String) session.getAttribute("pendingOp");
        Item4 pendingItem = (Item4) session.getAttribute("pendingItem");
        Supplier4 pendingSup = (Supplier4) session.getAttribute("pendingSupplier");

        if (op == null) {
            response.sendRedirect(request.getContextPath() + "/warehouse/manage");
            return;
        }

        boolean isItemOp = op.endsWith("_ITEM");
        String listUrl = request.getContextPath() + (isItemOp ? "/warehouse/items" : "/warehouse/suppliers");

        if ("cancel".equals(decision)) {
            session.removeAttribute("pendingOp");
            session.removeAttribute("pendingItem");
            session.removeAttribute("pendingSupplier");
            response.sendRedirect(listUrl);
            return;
        }

        if ("back".equals(decision)) {
            if ("ADD_ITEM".equals(op) || "EDIT_ITEM".equals(op)) {
                request.setAttribute("item", pendingItem);
                try {
                    request.setAttribute("suppliers", supplierDAO.getSupplier(""));
                } catch (SQLException ignored) {
                }
                String jsp = "EDIT_ITEM".equals(op)
                        ? "/WEB-INF/WarehouseStaff/ItemEditView4.jsp"
                        : "/WEB-INF/WarehouseStaff/ItemAddView4.jsp";
                request.getRequestDispatcher(jsp).forward(request, response);
                return;
            } else if ("ADD_SUPPLIER".equals(op) || "EDIT_SUPPLIER".equals(op)) {
                request.setAttribute("supplier", pendingSup);
                String jsp = "EDIT_SUPPLIER".equals(op)
                        ? "/WEB-INF/WarehouseStaff/SupplierEditView4.jsp"
                        : "/WEB-INF/WarehouseStaff/SupplierAddView4.jsp";
                request.getRequestDispatcher(jsp).forward(request, response);
                return;
            }
            response.sendRedirect(listUrl);
            return;
        }

        // decision == "confirm"
        try {
            switch (op) {
                case "ADD_ITEM":
                    itemDAO.addItem(pendingItem);
                    session.setAttribute("flashSuccess", "Đã thêm hàng hóa '" + pendingItem.getName() + "' thành công.");
                    break;
                case "EDIT_ITEM":
                    itemDAO.updateItem(pendingItem);
                    session.setAttribute("flashSuccess", "Đã cập nhật hàng hóa '" + pendingItem.getName() + "' thành công.");
                    break;
                case "DELETE_ITEM":
                    itemDAO.deleteItem(pendingItem.getId());
                    session.setAttribute("flashSuccess", "Đã xóa hàng hóa '" + pendingItem.getName() + "' thành công.");
                    break;
                case "ADD_SUPPLIER":
                    supplierDAO.addSupplier(pendingSup);
                    session.setAttribute("flashSuccess", "Đã thêm nhà cung cấp '" + pendingSup.getName() + "' thành công.");
                    break;
                case "EDIT_SUPPLIER":
                    supplierDAO.updateSupplier(pendingSup);
                    session.setAttribute("flashSuccess", "Đã cập nhật nhà cung cấp '" + pendingSup.getName() + "' thành công.");
                    break;
                case "DELETE_SUPPLIER":
                    supplierDAO.deleteSupplier(pendingSup.getId());
                    session.setAttribute("flashSuccess", "Đã xóa nhà cung cấp '" + pendingSup.getName() + "' thành công.");
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            session.setAttribute("flashError", "Thao tác thất bại (có thể dữ liệu đang được tham chiếu bởi hóa đơn): " + e.getMessage());
        } finally {
            session.removeAttribute("pendingOp");
            session.removeAttribute("pendingItem");
            session.removeAttribute("pendingSupplier");
        }
        response.sendRedirect(listUrl);
    }

    private void showImportSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        try {
            request.setAttribute("keyword", keyword);
            request.setAttribute("items", itemDAO.getItem(keyword));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tìm kiếm hàng hóa: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ImportView4.jsp").forward(request, response);
    }

    private void showItemImport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            Item4 item = itemDAO.getItemById(id);
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/import");
                return;
            }
            request.setAttribute("item", item);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải thông tin sản phẩm: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemImportView4.jsp").forward(request, response);
    }

    private void executeImport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
        int qty = WebHelper.parseInt(request.getParameter("quantity"), 0);
        String duetimeStr = request.getParameter("duetime");

        try {
            Item4 item = itemDAO.getItemById(itemId);
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/warehouse/import");
                return;
            }
            if (qty <= 0) {
                request.setAttribute("item", item);
                request.setAttribute("error", "Số lượng nhập phải lớn hơn 0.");
                request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemImportView4.jsp").forward(request, response);
                return;
            }

            WarehouseStaff4 staff = (WarehouseStaff4) WebHelper.getCurrentUser(request);
            ImportInvoice4 invoice = new ImportInvoice4();
            invoice.setDatetime(Date.valueOf(LocalDate.now()));
            invoice.setQuantity(qty);
            if (duetimeStr != null && !duetimeStr.isBlank()) {
                invoice.setDuetime(Date.valueOf(duetimeStr));
            } else {
                invoice.setDuetime(Date.valueOf(LocalDate.now().plusMonths(6)));
            }
            invoice.setTotalAmount(qty * item.getImportPrice());
            invoice.setwStaff(staff);
            invoice.setItem(item);

            importInvoiceDAO.addImportInvoice(invoice);
            Item4 updatedItem = itemDAO.getItemById(itemId);
            request.setAttribute("item", updatedItem);
            request.setAttribute("createdInvoice", invoice);
            request.setAttribute("success", "Nhập hàng thành công! Đã tạo hóa đơn nhập #" + invoice.getId()
                    + " và gửi thông tin cho nhà cung cấp " + item.getSupplier().getName() + ".");
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemImportView4.jsp").forward(request, response);
        } catch (SQLException | IllegalArgumentException e) {
            request.setAttribute("error", "Lỗi khi tạo hóa đơn nhập: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/WarehouseStaff/ItemImportView4.jsp").forward(request, response);
        }
    }

    private void showOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consumeFlash(request);
        int selectedId = WebHelper.parseInt(request.getParameter("id"), -1);
        try {
            List<Order4> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            if (selectedId > 0) {
                request.setAttribute("selectedOrder", orderDAO.getOrderById(selectedId));
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách đơn hàng trực tuyến: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/WarehouseStaff/OrderExportView4.jsp").forward(request, response);
    }

    private void exportOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int orderId = WebHelper.parseInt(request.getParameter("orderId"), -1);
        String newStatus = request.getParameter("status");
        if (newStatus == null || newStatus.isBlank()) {
            newStatus = "Exported";
        }
        WarehouseStaff4 staff = (WarehouseStaff4) WebHelper.getCurrentUser(request);
        try {
            orderDAO.updateOrderStatus(orderId, staff.getId(), newStatus);
            request.getSession().setAttribute("flashSuccess",
                    "Đã xác nhận xuất đơn hàng #" + orderId + " cho nhân viên giao hàng (Trạng thái: " + newStatus + ").");
        } catch (SQLException e) {
            request.getSession().setAttribute("flashError", "Lỗi cập nhật đơn hàng: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/warehouse/orders");
    }
}
