package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import model.Customer4;

/**
 * DAO for Customer registration (Modul khách hàng đăng kí người dùng).
 */
public class CustomerDAO4 extends DAO4 {

    private static final String CHECK_USERNAME_SQL = "SELECT id FROM tblUser4 WHERE username = ?";
    private static final String INSERT_USER_SQL =
            "INSERT INTO tblUser4 (name, username, password, birthdate, address, email, phonenumber, note) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO tblCustomer4 (tblUser4id, AccCode) VALUES (?, ?)";

    public CustomerDAO4() {
        super();
    }

    /**
     * Registers a new customer in tblUser4 and tblCustomer4 inside a single transaction.
     */
    public boolean addCustomer(Customer4 customer) throws SQLException {
        try (Connection conn = openConnection()) {
            try (PreparedStatement psCheck = conn.prepareStatement(CHECK_USERNAME_SQL)) {
                psCheck.setString(1, customer.getUsername());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException("Tên đăng nhập đã tồn tại, vui lòng chọn tên khác.");
                    }
                }
            }

            conn.setAutoCommit(false);
            try {
                int userId;
                try (PreparedStatement psUser = conn.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    psUser.setString(1, customer.getName());
                    psUser.setString(2, customer.getUsername());
                    psUser.setString(3, customer.getPassword());
                    if (customer.getBirthdate() != null && !customer.getBirthdate().isBlank()) {
                        psUser.setDate(4, Date.valueOf(customer.getBirthdate()));
                    } else {
                        psUser.setNull(4, Types.DATE);
                    }
                    psUser.setString(5, customer.getAddress());
                    psUser.setString(6, customer.getEmail());
                    psUser.setString(7, customer.getPhonenumber());
                    psUser.setString(8, customer.getNote() != null ? customer.getNote() : "Khách hàng thành viên");
                    psUser.executeUpdate();

                    try (ResultSet keys = psUser.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Failed to obtain generated user ID.");
                        }
                        userId = keys.getInt(1);
                    }
                }

                String accCode = customer.getAccCode();
                if (accCode == null || accCode.isBlank()) {
                    accCode = String.format("CUS-%04d", userId);
                }

                try (PreparedStatement psCust = conn.prepareStatement(INSERT_CUSTOMER_SQL)) {
                    psCust.setInt(1, userId);
                    psCust.setString(2, accCode);
                    psCust.executeUpdate();
                }

                customer.setId(userId);
                customer.setAccCode(accCode);
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
