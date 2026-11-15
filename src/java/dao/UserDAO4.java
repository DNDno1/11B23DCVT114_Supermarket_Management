package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Customer4;
import model.ManagementStaff4;
import model.Saler4;
import model.User4;
import model.WarehouseStaff4;

/**
 * DAO handling user login authentication across Customer and Staff roles.
 */
public class UserDAO4 extends DAO4 {

    private static final String LOGIN_SQL =
            "SELECT u.*, s.role, c.AccCode "
            + "FROM tblUser4 u "
            + "LEFT JOIN tblStaff4 s ON u.id = s.tblUser4id "
            + "LEFT JOIN tblCustomer4 c ON u.id = c.tblUser4id "
            + "WHERE u.username = ? AND u.password = ?";

    public UserDAO4() {
        super();
    }

    /**
     * Authenticates a user and returns the concrete subclass matching their role.
     */
    public User4 checkLogin(String username, String password) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(LOGIN_SQL)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    private User4 mapUser(ResultSet rs) throws SQLException {
        String staffRole = rs.getString("role");
        String accCode = rs.getString("AccCode");
        User4 user;

        if (staffRole != null) {
            switch (staffRole.trim()) {
                case "ManagementStaff":
                case "Manager":
                    user = new ManagementStaff4();
                    break;
                case "WarehouseStaff":
                case "Warehouse":
                    user = new WarehouseStaff4();
                    break;
                case "Saler":
                    user = new Saler4();
                    break;
                default:
                    user = new ManagementStaff4();
                    break;
            }
        } else {
            Customer4 customer = new Customer4();
            customer.setAccCode(accCode != null ? accCode : String.format("CUS-%04d", rs.getInt("id")));
            user = customer;
        }

        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setBirthdate(rs.getString("birthdate"));
        user.setAddress(rs.getString("address"));
        user.setEmail(rs.getString("email"));
        user.setPhonenumber(rs.getString("phonenumber"));
        user.setNote(rs.getString("note"));
        return user;
    }
}
