package web;

import dao.ItemDAO4;
import dao.SaleInvoiceDAO4;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Item4;
import model.SaleInvoice4;
import model.SaleInvoiceItem4;
import model.Saler4;

/**
 * Handles Saler counter POS flows: scanning/adding items, issuing bill, and confirming payment.
 */
@WebServlet(name = "SalerServlet", urlPatterns = {"/saler", "/saler/*"})
public class SalerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ItemDAO4 itemDAO = new ItemDAO4();
    private final SaleInvoiceDAO4 saleInvoiceDAO = new SaleInvoiceDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "Saler")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            showSalerHome(request, response);
        } else if ("/sale".equals(pathInfo)) {
            showSaleManage(request, response);
        } else if ("/bill".equals(pathInfo)) {
            showBillPay(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/saler");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "Saler")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if ("/sale".equals(pathInfo)) {
            handleSaleAction(request, response);
        } else if ("/bill".equals(pathInfo)) {
            confirmBillPay(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/saler");
        }
    }

    private Item4 findItem(int id) throws SQLException {
        for (Item4 item : itemDAO.getItem(String.valueOf(id))) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<SaleInvoiceItem4> getSaleBill(HttpSession session) {
        List<SaleInvoiceItem4> list = (List<SaleInvoiceItem4>) session.getAttribute("saleBill");
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute("saleBill", list);
        }
        return list;
    }

    private void showSalerHome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("recentInvoices", saleInvoiceDAO.getSaleItem());
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải hóa đơn gần đây: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/Saler/SalerView4.jsp").forward(request, response);
    }

    private void showSaleManage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        try {
            request.setAttribute("keyword", keyword);
            request.setAttribute("items", itemDAO.getItem(keyword));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách sản phẩm: " + e.getMessage());
        }
        List<SaleInvoiceItem4> saleBill = getSaleBill(request.getSession());
        int total = 0;
        for (SaleInvoiceItem4 sii : saleBill) {
            total += sii.getTotalAmount();
        }
        request.setAttribute("saleBill", saleBill);
        request.setAttribute("billTotal", total);
        request.getRequestDispatcher("/WEB-INF/Saler/SaleManageView4.jsp").forward(request, response);
    }

    private void handleSaleAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        List<SaleInvoiceItem4> saleBill = getSaleBill(request.getSession());

        if ("add".equals(action)) {
            int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
            int quantity = WebHelper.parseInt(request.getParameter("quantity"), 1);
            try {
                Item4 item = findItem(itemId);
                if (item == null) {
                    request.setAttribute("error", "Không tìm thấy mã sản phẩm #" + itemId);
                } else if (quantity <= 0 || quantity > item.getQuantity()) {
                    request.setAttribute("error", "Số lượng không hợp lệ hoặc vượt tồn kho (" + item.getQuantity() + ").");
                } else {
                    boolean found = false;
                    for (SaleInvoiceItem4 sii : saleBill) {
                        if (sii.getItem().getId() == itemId) {
                            int newQty = sii.getQuantity() + quantity;
                            if (newQty > item.getQuantity()) {
                                request.setAttribute("error", "Tổng số lượng vượt quá tồn kho (" + item.getQuantity() + ").");
                            } else {
                                sii.setQuantity(newQty);
                                sii.setTotalAmount(newQty * item.getPrice());
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        SaleInvoiceItem4 sii = new SaleInvoiceItem4();
                        sii.setItem(item);
                        sii.setQuantity(quantity);
                        sii.setTotalAmount(quantity * item.getPrice());
                        saleBill.add(sii);
                    }
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi thêm sản phẩm vào hóa đơn: " + e.getMessage());
            }
        } else if ("remove".equals(action)) {
            int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
            Iterator<SaleInvoiceItem4> it = saleBill.iterator();
            while (it.hasNext()) {
                if (it.next().getItem().getId() == itemId) {
                    it.remove();
                    break;
                }
            }
        } else if ("clear".equals(action)) {
            saleBill.clear();
        }

        showSaleManage(request, response);
    }

    private void showBillPay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<SaleInvoiceItem4> saleBill = getSaleBill(request.getSession());
        int total = 0;
        for (SaleInvoiceItem4 sii : saleBill) {
            total += sii.getTotalAmount();
        }
        request.setAttribute("saleBill", saleBill);
        request.setAttribute("billTotal", total);
        request.getRequestDispatcher("/WEB-INF/Saler/BillPayView4.jsp").forward(request, response);
    }

    private void confirmBillPay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<SaleInvoiceItem4> saleBill = getSaleBill(request.getSession());
        if (saleBill.isEmpty()) {
            request.setAttribute("error", "Chưa có sản phẩm nào trong hóa đơn để thanh toán.");
            showBillPay(request, response);
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        int total = 0;
        for (SaleInvoiceItem4 sii : saleBill) {
            total += sii.getTotalAmount();
        }

        try {
            Saler4 saler = (Saler4) WebHelper.getCurrentUser(request);
            SaleInvoice4 invoice = new SaleInvoice4();
            invoice.setDatetime(Date.valueOf(LocalDate.now()));
            invoice.setTotalAmount(total);
            invoice.setSaler(saler);
            invoice.setCustomer(null);
            invoice.setItems(new ArrayList<>(saleBill));

            saleInvoiceDAO.addSaleItem(invoice);
            saleBill.clear();

            request.setAttribute("completedInvoice", invoice);
            request.setAttribute("paymentMethod", paymentMethod != null ? paymentMethod : "Tiền mặt");
            request.setAttribute("success", "Hoàn tất giao dịch tại quầy! Mã hóa đơn: #" + invoice.getId());
            request.getRequestDispatcher("/WEB-INF/Saler/BillPayView4.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi thanh toán hóa đơn: " + e.getMessage());
            showBillPay(request, response);
        }
    }
}
