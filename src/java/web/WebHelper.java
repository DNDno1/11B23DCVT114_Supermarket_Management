package web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import model.User4;

/**
 * Shared request, session, role-guard, and parsing helpers for Servlets.
 */
public final class WebHelper {
    private WebHelper() {
    }

    public static User4 getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User4) session.getAttribute("currentUser");
    }

    public static boolean requireRole(HttpServletRequest request, HttpServletResponse response, String expectedRole)
            throws IOException {
        User4 user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (!expectedRole.equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + dashboardPathForRole(user.getRole()));
            return false;
        }
        return true;
    }

    public static String dashboardPathForRole(String role) {
        if ("ManagementStaff".equalsIgnoreCase(role)) {
            return "/manager";
        }
        if ("WarehouseStaff".equalsIgnoreCase(role)) {
            return "/warehouse";
        }
        if ("Saler".equalsIgnoreCase(role)) {
            return "/saler";
        }
        if ("Customer".equalsIgnoreCase(role)) {
            return "/customer";
        }
        return "/login";
    }

    public static int parseInt(String value, int defaultVal) {
        if (value == null || value.isBlank()) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static String[] resolveDateRange(HttpServletRequest request) {
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");
        LocalDate now = LocalDate.now();
        String defaultStart = now.withDayOfYear(1).toString();
        String defaultEnd = now.withMonth(12).withDayOfMonth(31).toString();

        if (start == null || start.isBlank()) {
            start = defaultStart;
        }
        if (end == null || end.isBlank()) {
            end = defaultEnd;
        }

        try {
            Date s = Date.valueOf(start);
            Date e = Date.valueOf(end);
            if (s.after(e)) {
                request.setAttribute("error", "Ngày bắt đầu không được muộn hơn ngày kết thúc.");
            }
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ.");
            start = defaultStart;
            end = defaultEnd;
        }
        return new String[] { start, end };
    }
}
