package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Item4;
import model.Supplier4;

/**
 * DAO for searching and managing Item entities: getItem(), addItem(), updateItem(), deleteItem().
 */
public class ItemDAO4 extends DAO4 {

    private static final String SEARCH_ITEMS_SQL =
            "SELECT i.*, s.name AS sup_name, s.address AS sup_address, s.email AS sup_email, "
            + "s.phonenumber AS sup_phone, s.note AS sup_note "
            + "FROM tblItem4 i "
            + "LEFT JOIN tblSupplier4 s ON i.tblSupplier4id = s.id "
            + "WHERE i.name LIKE ? OR CAST(i.id AS CHAR) LIKE ? OR i.note LIKE ? "
            + "ORDER BY i.id ASC";

    private static final String INSERT_ITEM_SQL =
            "INSERT INTO tblItem4 (name, quantity, price, ImportPrice, note, tblSupplier4id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ITEM_SQL =
            "UPDATE tblItem4 SET name = ?, quantity = ?, price = ?, ImportPrice = ?, note = ?, tblSupplier4id = ? WHERE id = ?";

    private static final String DELETE_ITEM_SQL =
            "DELETE FROM tblItem4 WHERE id = ?";

    public ItemDAO4() {
        super();
    }

    public List<Item4> getItem(String key) throws SQLException {
        List<Item4> list = new ArrayList<>();
        String trimmed = key == null ? "" : key.trim();
        String pattern = "%" + trimmed + "%";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_ITEMS_SQL)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapItem(rs));
                }
            }
        }
        return list;
    }

    public boolean addItem(Item4 item) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setInt(2, item.getQuantity());
            ps.setInt(3, item.getPrice());
            ps.setInt(4, item.getImportPrice());
            ps.setString(5, item.getNote());
            ps.setInt(6, item.getSupplier().getId());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        item.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateItem(Item4 item) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ITEM_SQL)) {
            ps.setString(1, item.getName());
            ps.setInt(2, item.getQuantity());
            ps.setInt(3, item.getPrice());
            ps.setInt(4, item.getImportPrice());
            ps.setString(5, item.getNote());
            ps.setInt(6, item.getSupplier().getId());
            ps.setInt(7, item.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteItem(int id) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ITEM_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Item4 mapItem(ResultSet rs) throws SQLException {
        Supplier4 sup = new Supplier4();
        sup.setId(rs.getInt("tblSupplier4id"));
        sup.setName(rs.getString("sup_name"));
        sup.setAddress(rs.getString("sup_address"));
        sup.setEmail(rs.getString("sup_email"));
        sup.setPhonenumber(rs.getString("sup_phone"));
        sup.setNote(rs.getString("sup_note"));

        Item4 item = new Item4();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getInt("price"));
        item.setImportPrice(rs.getInt("ImportPrice"));
        item.setNote(rs.getString("note"));
        item.setSupplier(sup);
        return item;
    }
}
