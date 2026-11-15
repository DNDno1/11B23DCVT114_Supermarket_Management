package web;

import dao.ItemDAO4;
import dao.OrderDAO4;
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
import model.Customer4;
import model.Item4;
import model.Order4;
import model.OrderItem4;

/**
 * Handles Customer flows: home dashboard, item search, item details, cart, and online ordering.
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customer", "/customer/*"})
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ItemDAO4 itemDAO = new ItemDAO4();
    private final OrderDAO4 orderDAO = new OrderDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "Customer")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            showCustomerHome(request, response);
        } else if ("/search".equals(pathInfo)) {
            showSearch(request, response);
        } else if ("/item".equals(pathInfo)) {
            showItemDetail(request, response);
        } else if ("/order".equals(pathInfo)) {
            showOrderView(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/customer");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!WebHelper.requireRole(request, response, "Customer")) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if ("/cart/add".equals(pathInfo)) {
            addToCart(request, response);
        } else if ("/order".equals(pathInfo)) {
            handleOrderAction(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/customer");
        }
    }

    private void showCustomerHome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer4 customer = (Customer4) WebHelper.getCurrentUser(request);
        try {
            request.setAttribute("orders", orderDAO.getOrder(customer.getId()));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải lịch sử đơn hàng: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/Customer/CustomerView4.jsp").forward(request, response);
    }

    private void showSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        try {
            List<Item4> items = itemDAO.getItem(keyword);
            request.setAttribute("keyword", keyword);
            request.setAttribute("listItem", items);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/Customer/SearchView4.jsp").forward(request, response);
    }

    private Item4 findItem(int id) throws SQLException {
        for (Item4 item : itemDAO.getItem(String.valueOf(id))) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private void showItemDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebHelper.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/customer/search");
            return;
        }
        try {
            Item4 item = findItem(id);
            if (item == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm.");
            }
            request.setAttribute("item", item);
            request.setAttribute("keyword", request.getParameter("keyword"));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải thông tin sản phẩm: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/Customer/ItemDetailView4.jsp").forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private List<OrderItem4> getCart(HttpSession session) {
        List<OrderItem4> cart = (List<OrderItem4>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
        int quantity = WebHelper.parseInt(request.getParameter("quantity"), 0);
        String keyword = request.getParameter("keyword");
        String goOrder = request.getParameter("goOrder");

        try {
            Item4 item = findItem(itemId);
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/customer/search");
                return;
            }
            if (quantity <= 0 || quantity > item.getQuantity()) {
                request.setAttribute("item", item);
                request.setAttribute("keyword", keyword);
                request.setAttribute("error", "Số lượng đặt phải lớn hơn 0 và không vượt quá tồn kho (" + item.getQuantity() + ").");
                request.getRequestDispatcher("/WEB-INF/Customer/ItemDetailView4.jsp").forward(request, response);
                return;
            }

            List<OrderItem4> cart = getCart(request.getSession());
            boolean found = false;
            for (OrderItem4 oi : cart) {
                if (oi.getItem().getId() == itemId) {
                    int newQty = oi.getQuantity() + quantity;
                    if (newQty > item.getQuantity()) {
                        request.setAttribute("item", item);
                        request.setAttribute("keyword", keyword);
                        request.setAttribute("error", "Tổng số lượng trong giỏ vượt quá tồn kho (" + item.getQuantity() + ").");
                        request.getRequestDispatcher("/WEB-INF/Customer/ItemDetailView4.jsp").forward(request, response);
                        return;
                    }
                    oi.setQuantity(newQty);
                    oi.setTotalAmount(newQty * item.getPrice());
                    found = true;
                    break;
                }
            }
            if (!found) {
                OrderItem4 oi = new OrderItem4();
                oi.setItem(item);
                oi.setQuantity(quantity);
                oi.setTotalAmount(quantity * item.getPrice());
                cart.add(oi);
            }

            if ("1".equals(goOrder)) {
                response.sendRedirect(request.getContextPath() + "/customer/order");
                return;
            }
            request.setAttribute("item", item);
            request.setAttribute("keyword", keyword);
            request.setAttribute("success", "Đã thêm " + quantity + " '" + item.getName() + "' vào giỏ hàng.");
            request.getRequestDispatcher("/WEB-INF/Customer/ItemDetailView4.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi thêm vào giỏ hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/Customer/ItemDetailView4.jsp").forward(request, response);
        }
    }

    private void showOrderView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer4 customer = (Customer4) WebHelper.getCurrentUser(request);
        List<OrderItem4> cart = getCart(request.getSession());
        int totalAmount = 0;
        for (OrderItem4 oi : cart) {
            totalAmount += oi.getTotalAmount();
        }
        request.setAttribute("cart", cart);
        request.setAttribute("totalAmount", totalAmount);
        try {
            request.setAttribute("orders", orderDAO.getOrder(customer.getId()));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi tải danh sách đơn hàng: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/Customer/OnlineOrderView4.jsp").forward(request, response);
    }

    private void handleOrderAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        List<OrderItem4> cart = getCart(request.getSession());

        if ("remove".equals(action)) {
            int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
            Iterator<OrderItem4> it = cart.iterator();
            while (it.hasNext()) {
                if (it.next().getItem().getId() == itemId) {
                    it.remove();
                    break;
                }
            }
            showOrderView(request, response);
            return;
        }

        if ("update".equals(action)) {
            int itemId = WebHelper.parseInt(request.getParameter("itemId"), -1);
            int quantity = WebHelper.parseInt(request.getParameter("quantity"), 0);
            try {
                Item4 latest = findItem(itemId);
                for (OrderItem4 oi : cart) {
                    if (oi.getItem().getId() == itemId) {
                        if (quantity <= 0) {
                            cart.remove(oi);
                        } else if (latest != null && quantity <= latest.getQuantity()) {
                            oi.setItem(latest);
                            oi.setQuantity(quantity);
                            oi.setTotalAmount(quantity * latest.getPrice());
                        } else {
                            request.setAttribute("error", "Số lượng vượt quá tồn kho hiện tại.");
                        }
                        break;
                    }
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi cập nhật giỏ hàng: " + e.getMessage());
            }
            showOrderView(request, response);
            return;
        }

        if ("confirm".equals(action)) {
            if (cart.isEmpty()) {
                request.setAttribute("error", "Giỏ hàng đang trống, vui lòng chọn sản phẩm trước khi đặt hàng.");
                showOrderView(request, response);
                return;
            }

            Customer4 customer = (Customer4) WebHelper.getCurrentUser(request);
            int totalAmount = 0;
            for (OrderItem4 oi : cart) {
                totalAmount += oi.getTotalAmount();
            }

            Order4 order = new Order4();
            order.setDatetime(Date.valueOf(LocalDate.now()));
            order.setStatus("Pending");
            order.setTotalAmount(totalAmount);
            order.setCustomer(customer);
            order.setItems(new ArrayList<>(cart));

            try {
                orderDAO.addOrderItem(order);
                cart.clear();
                request.setAttribute("success", "Đặt hàng trực tuyến thành công! Mã đơn hàng: #" + order.getId()
                        + ". Đơn hàng đang chờ nhân viên kho duyệt và xuất hàng.");
            } catch (SQLException e) {
                request.setAttribute("error", "Không thể tạo đơn hàng: " + e.getMessage());
            }
            showOrderView(request, response);
            return;
        }

        showOrderView(request, response);
    }
}
