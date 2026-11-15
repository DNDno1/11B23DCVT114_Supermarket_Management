package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Supplier4;

/**
 * DAO for CRUD operations on Suppliers: getSupplier(), addSupplier(), updateSupplier(), deleteSupplier().
 */
public class SupplierDAO4 extends DAO4 {

    private static final String SEARCH_SUPPLIERS_SQL =
            "SELECT * FROM tblSupplier4 WHERE name LIKE ? OR address LIKE ? OR phonenumber LIKE ? ORDER BY id ASC";
    private static final String INSERT_SUPPLIER_SQL =
            "INSERT INTO tblSupplier4 (name, address, email, phonenumber, note) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SUPPLIER_SQL =
            "UPDATE tblSupplier4 SET name = ?, address = ?, email = ?, phonenumber = ?, note = ? WHERE id = ?";
    private static final String DELETE_SUPPLIER_SQL =
            "DELETE FROM tblSupplier4 WHERE id = ?";

    public SupplierDAO4() {
        super();
    }

    public List<Supplier4> getSupplier(String key) throws SQLException {
        List<Supplier4> list = new ArrayList<>();
        String pattern = "%" + (key == null ? "" : key.trim()) + "%";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_SUPPLIERS_SQL)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSupplier(rs));
                }
            }
        }
        return list;
    }

    public boolean addSupplier(Supplier4 supplier) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SUPPLIER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhonenumber());
            ps.setString(5, supplier.getNote());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        supplier.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateSupplier(Supplier4 supplier) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SUPPLIER_SQL)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhonenumber());
            ps.setString(5, supplier.getNote());
            ps.setInt(6, supplier.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSupplier(int id) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SUPPLIER_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Supplier4 mapSupplier(ResultSet rs) throws SQLException {
        Supplier4 s = new Supplier4();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setAddress(rs.getString("address"));
        s.setEmail(rs.getString("email"));
        s.setPhonenumber(rs.getString("phonenumber"));
        s.setNote(rs.getString("note"));
        return s;
    }
}
