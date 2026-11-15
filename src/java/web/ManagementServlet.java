package web;

import dao.ImportInvoiceDAO4;
import dao.ItemStatDAO4;
import dao.RevenueStatDAO4;
import dao.SupplierStatDAO4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.ImportInvoice4;
import model.ItemStatistic4;
import model.RevenueStatistic4;
import model.SupplierStatistic4;

/**
 * Handles Management Staff statistics modules: Item stats, Supplier stats, Revenue stats, and Invoice details.
 */
@WebServlet(name = "ManagementServlet", urlPatterns = {"/manager", "/manager/*"})
public class ManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ItemStatDAO4 itemStatDAO = new ItemStatDAO4();
    private final SupplierStatDAO4 supplierStatDAO = new SupplierStatDAO4();
    private final RevenueStatDAO4 revenueStatDAO = new RevenueStatDAO4();
    private final ImportInvoiceDAO4 importInvoiceDAO = new ImportInvoiceDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "ManagementStaff")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/ManagementStaff/ManageStaffView4.jsp").forward(request, response);
        } else if ("/stats".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/ManagementStaff/StatisticChoiceView4.jsp").forward(request, response);
        } else if ("/stats/items".equals(pathInfo)) {
            showItemStats(request, response);
        } else if ("/stats/item-invoices".equals(pathInfo)) {
            showItemInvoices(request, response);
        } else if ("/stats/suppliers".equals(pathInfo)) {
            showSupplierStats(request, response);
        } else if ("/stats/supplier-invoices".equals(pathInfo)) {
            showSupplierInvoices(request, response);
        } else if ("/stats/revenue".equals(pathInfo)) {
            showRevenueStats(request, response);
        } else if ("/stats/invoice-detail".equals(pathInfo)) {
            showInvoiceDetail(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/manager");
        }
    }

    private void showItemStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] range = WebHelper.resolveDateRange(request);
        String startDate = range[0];
        String endDate = range[1];
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("keyword", keyword);

        if (request.getAttribute("error") == null) {
            try {
                List<ItemStatistic4> list = itemStatDAO.getItemStat(keyword, startDate, endDate);
                request.setAttribute("itemStats", list);
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi truy vấn thống kê sản phẩm: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/ItemStatisticView4.jsp").forward(request, response);
    }

    private void showItemInvoices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
        String[] range = WebHelper.resolveDateRange(request);
        String startDate = range[0];
        String endDate = range[1];
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        if (itemId <= 0) {
            response.sendRedirect(request.getContextPath() + "/manager/stats/items");
            return;
        }
        try {
            ItemStatistic4 itemStat = null;
            for (ItemStatistic4 st : itemStatDAO.getItemStat("", startDate, endDate)) {
                if (st.getId() == itemId) {
                    itemStat = st;
                    break;
                }
            }
            List<ImportInvoice4> invoices = itemStat != null
                    ? importInvoiceDAO.getImportInvoice(itemStat, startDate, endDate)
                    : List.of();
            request.setAttribute("itemStat", itemStat);
            request.setAttribute("invoices", invoices);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải hóa đơn nhập của sản phẩm: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/ItemInvoiceView4.jsp").forward(request, response);
    }

    private void showSupplierStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] range = WebHelper.resolveDateRange(request);
        String startDate = range[0];
        String endDate = range[1];
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("keyword", keyword);

        if (request.getAttribute("error") == null) {
            try {
                List<SupplierStatistic4> list = supplierStatDAO.getSupplierStat(keyword, startDate, endDate);
                request.setAttribute("supplierStats", list);
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi truy vấn thống kê nhà cung cấp: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/SupplierStatisticView4.jsp").forward(request, response);
    }

    private void showSupplierInvoices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int supplierId = WebHelper.parseInt(request.getParameter("supplierId"), -1);
        String[] range = WebHelper.resolveDateRange(request);
        String startDate = range[0];
        String endDate = range[1];
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        if (supplierId <= 0) {
            response.sendRedirect(request.getContextPath() + "/manager/stats/suppliers");
            return;
        }
        try {
            SupplierStatistic4 supStat = null;
            for (SupplierStatistic4 st : supplierStatDAO.getSupplierStat("", startDate, endDate)) {
                if (st.getId() == supplierId) {
                    supStat = st;
                    break;
                }
            }
            List<ImportInvoice4> invoices = supStat != null
                    ? importInvoiceDAO.getImportInvoice(supStat, startDate, endDate)
                    : List.of();
            request.setAttribute("supplierStat", supStat);
            request.setAttribute("invoices", invoices);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách hóa đơn nhà cung cấp: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/SupplierInvoiceView4.jsp").forward(request, response);
    }

    private void showRevenueStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] range = WebHelper.resolveDateRange(request);
        String startDate = range[0];
        String endDate = range[1];
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        if (request.getAttribute("error") == null) {
            try {
                RevenueStatistic4 revStat = revenueStatDAO.getRevenueStat(startDate, endDate);
                request.setAttribute("revenueStat", revStat);
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi truy vấn thống kê doanh thu: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/RevenueStatisticView4.jsp").forward(request, response);
    }

    private void showInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        String from = request.getParameter("from");
        String refId = request.getParameter("refId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        String backUrl = request.getContextPath() + "/manager/stats";
        if ("supplier".equals(from) && refId != null) {
            backUrl = request.getContextPath() + "/manager/stats/supplier-invoices?supplierId=" + refId
                    + "&startDate=" + (startDate != null ? startDate : "")
                    + "&endDate=" + (endDate != null ? endDate : "");
        } else if ("item".equals(from) && refId != null) {
            backUrl = request.getContextPath() + "/manager/stats/item-invoices?itemId=" + refId
                    + "&startDate=" + (startDate != null ? startDate : "")
                    + "&endDate=" + (endDate != null ? endDate : "");
        } else if ("revenue".equals(from)) {
            backUrl = request.getContextPath() + "/manager/stats/revenue?startDate="
                    + (startDate != null ? startDate : "")
                    + "&endDate=" + (endDate != null ? endDate : "");
        }
        request.setAttribute("backUrl", backUrl);

        if (id > 0) {
            try {
                String rangeStart = (startDate != null && !startDate.isBlank()) ? startDate : "1970-01-01";
                String rangeEnd = (endDate != null && !endDate.isBlank()) ? endDate : "2099-12-31";
                ImportInvoice4 invoice = null;
                for (ImportInvoice4 inv : importInvoiceDAO.getImportInvoice(rangeStart, rangeEnd)) {
                    if (inv.getId() == id) {
                        invoice = inv;
                        break;
                    }
                }
                request.setAttribute("invoice", invoice);
                if (invoice == null) {
                    request.setAttribute("error", "Không tìm thấy hóa đơn nhập #" + id);
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi tải chi tiết hóa đơn: " + e.getMessage());
            }
        } else {
            request.setAttribute("error", "Mã hóa đơn không hợp lệ.");
        }
        request.getRequestDispatcher("/WEB-INF/ManagementStaff/InvoiceDetailView4.jsp").forward(request, response);
    }
}
