package web;

import dao.CustomerDAO4;
import dao.UserDAO4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Customer4;
import model.User4;

/**
 * Handles Login, Logout, Registration, and root entry routing.
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"", "/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserDAO4 userDAO = new UserDAO4();
    private final CustomerDAO4 customerDAO = new CustomerDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("/register".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/Customer/RegisterView4.jsp").forward(request, response);
            return;
        }

        User4 currentUser = WebHelper.getCurrentUser(request);
        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + WebHelper.dashboardPathForRole(currentUser.getRole()));
            return;
        }

        if ("1".equals(request.getParameter("registered"))) {
            request.setAttribute("success", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        }
        request.getRequestDispatcher("/WEB-INF/LoginVIew4.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        if ("/register".equals(path)) {
            handleRegister(request, response);
            return;
        }

        handleLogin(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            request.getRequestDispatcher("/WEB-INF/LoginVIew4.jsp").forward(request, response);
            return;
        }

        try {
            User4 user = userDAO.checkLogin(username.trim(), password);
            if (user == null) {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/WEB-INF/LoginVIew4.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);
            response.sendRedirect(request.getContextPath() + WebHelper.dashboardPathForRole(user.getRole()));
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/LoginVIew4.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String birthdate = request.getParameter("birthdate");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phonenumber = request.getParameter("phonenumber");

        Customer4 formCustomer = new Customer4();
        formCustomer.setName(name);
        formCustomer.setUsername(username);
        formCustomer.setBirthdate(birthdate);
        formCustomer.setAddress(address);
        formCustomer.setEmail(email);
        formCustomer.setPhonenumber(phonenumber);
        request.setAttribute("formCustomer", formCustomer);

        if (name == null || name.isBlank() || username == null || username.isBlank()
                || password == null || password.isBlank() || address == null || address.isBlank()
                || email == null || email.isBlank()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc.");
            request.getRequestDispatcher("/WEB-INF/Customer/RegisterView4.jsp").forward(request, response);
            return;
        }

        if (confirmPassword != null && !password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("/WEB-INF/Customer/RegisterView4.jsp").forward(request, response);
            return;
        }

        try {
            if (customerDAO.existsUsername(username.trim())) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác.");
                request.getRequestDispatcher("/WEB-INF/Customer/RegisterView4.jsp").forward(request, response);
                return;
            }

            formCustomer.setName(name.trim());
            formCustomer.setUsername(username.trim());
            formCustomer.setPassword(password);
            formCustomer.setAddress(address.trim());
            formCustomer.setEmail(email.trim());
            formCustomer.setPhonenumber(phonenumber != null ? phonenumber.trim() : "");
            customerDAO.addCustomer(formCustomer);

            response.sendRedirect(request.getContextPath() + "/login?registered=1");
        } catch (SQLException e) {
            request.setAttribute("error", "Không thể đăng ký tài khoản: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/Customer/RegisterView4.jsp").forward(request, response);
        }
    }
}
