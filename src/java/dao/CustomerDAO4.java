package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Customer4;

/**
 * DAO for Customer registration and lookup.
 */
public class CustomerDAO4 extends DAO4 {

    private static final String CHECK_USERNAME_SQL = "SELECT id FROM tblUser4 WHERE username = ?";
    private static final String INSERT_USER_SQL =
            "INSERT INTO tblUser4 (name, username, password, birthdate, address, email, phonenumber, note) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO tblCustomer4 (tblUser4id, AccCode) VALUES (?, ?)";
    private static final String UPDATE_ACCCODE_SQL =
            "UPDATE tblCustomer4 SET AccCode = ? WHERE tblUser4id = ?";
    private static final String SELECT_CUSTOMER_BY_ID_SQL =
            "SELECT u.*, c.AccCode FROM tblCustomer4 c "
            + "INNER JOIN tblUser4 u ON c.tblUser4id = u.id WHERE u.id = ?";
    private static final String SELECT_CUSTOMER_BY_ACCCODE_SQL =
            "SELECT u.*, c.AccCode FROM tblCustomer4 c "
            + "INNER JOIN tblUser4 u ON c.tblUser4id = u.id WHERE c.AccCode = ? OR u.username = ?";
    private static final String SELECT_ALL_CUSTOMERS_SQL =
            "SELECT u.*, c.AccCode FROM tblCustomer4 c "
            + "INNER JOIN tblUser4 u ON c.tblUser4id = u.id ORDER BY u.id DESC";

    public CustomerDAO4() {
        super();
    }

    public boolean existsUsername(String username) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_USERNAME_SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Registers a new customer in tblUser4 and tblCustomer4 inside a single transaction.
     */
    public boolean addCustomer(Customer4 customer) throws SQLException {
        try (Connection conn = openConnection()) {
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

                // Ensure AccCode reflects userId if temporary placeholder was ever used
                try (PreparedStatement psUpd = conn.prepareStatement(UPDATE_ACCCODE_SQL)) {
                    psUpd.setString(1, accCode);
                    psUpd.setInt(2, userId);
                    psUpd.executeUpdate();
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

    public Customer4 getCustomerById(int id) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CUSTOMER_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        }
        return null;
    }

    public Customer4 findCustomerByCodeOrUsername(String key) throws SQLException {
        if (key == null || key.isBlank()) {
            return null;
        }
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CUSTOMER_BY_ACCCODE_SQL)) {
            ps.setString(1, key.trim());
            ps.setString(2, key.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        }
        return null;
    }

    public List<Customer4> getAllCustomers() throws SQLException {
        List<Customer4> list = new ArrayList<>();
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CUSTOMERS_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
        }
        return list;
    }

    private Customer4 mapCustomer(ResultSet rs) throws SQLException {
        Customer4 c = new Customer4();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setUsername(rs.getString("username"));
        c.setPassword(rs.getString("password"));
        c.setBirthdate(rs.getString("birthdate"));
        c.setAddress(rs.getString("address"));
        c.setEmail(rs.getString("email"));
        c.setPhonenumber(rs.getString("phonenumber"));
        c.setNote(rs.getString("note"));
        c.setAccCode(rs.getString("AccCode"));
        return c;
    }
}
