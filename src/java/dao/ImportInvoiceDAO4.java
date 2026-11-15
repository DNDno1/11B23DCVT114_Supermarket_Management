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
import model.ImportInvoice4;
import model.Item4;
import model.ItemStatistic4;
import model.Supplier4;
import model.SupplierStatistic4;
import model.WarehouseStaff4;

/**
 * DAO for managing Import Invoices: addImportInvoice(), getImportInvoice().
 */
public class ImportInvoiceDAO4 extends DAO4 {

    private static final String BASE_SELECT =
            "SELECT imp.*, i.name AS item_name, i.price AS item_price, i.ImportPrice AS item_import_price, "
            + "i.quantity AS item_qty, i.note AS item_note, "
            + "s.id AS sup_id, s.name AS sup_name, s.address AS sup_address, s.email AS sup_email, s.phonenumber AS sup_phone, "
            + "u.name AS staff_name, u.username AS staff_username "
            + "FROM tblImportInvoice4 imp "
            + "INNER JOIN tblItem4 i ON imp.tblItem4id = i.id "
            + "INNER JOIN tblSupplier4 s ON i.tblSupplier4id = s.id "
            + "LEFT JOIN tblUser4 u ON imp.tblWarehouseStaff4id = u.id ";

    private static final String INSERT_IMPORT_SQL =
            "INSERT INTO tblImportInvoice4 (Datetime, quantity, Duetime, totalAmount, tblWarehouseStaff4id, tblItem4id) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String INCREMENT_ITEM_STOCK_SQL =
            "UPDATE tblItem4 SET quantity = quantity + ? WHERE id = ?";

    public ImportInvoiceDAO4() {
        super();
    }

    /**
     * Records an import invoice and updates item inventory quantity atomically.
     */
    public boolean addImportInvoice(ImportInvoice4 invoice) throws SQLException {
        try (Connection conn = openConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(INSERT_IMPORT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, invoice.getDatetime());
                    ps.setInt(2, invoice.getQuantity());
                    if (invoice.getDuetime() != null) {
                        ps.setDate(3, invoice.getDuetime());
                    } else {
                        ps.setNull(3, Types.DATE);
                    }
                    ps.setInt(4, invoice.getTotalAmount());
                    ps.setInt(5, invoice.getwStaff().getId());
                    ps.setInt(6, invoice.getItem().getId());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            invoice.setId(keys.getInt(1));
                        }
                    }
                }

                try (PreparedStatement psStock = conn.prepareStatement(INCREMENT_ITEM_STOCK_SQL)) {
                    psStock.setInt(1, invoice.getQuantity());
                    psStock.setInt(2, invoice.getItem().getId());
                    psStock.executeUpdate();
                }

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

    /**
     * Returns import invoices for a SupplierStatistic4 in a date range.
     */
    public List<ImportInvoice4> getImportInvoice(SupplierStatistic4 s, String startDate, String endDate)
            throws SQLException {
        String sql = BASE_SELECT
                + "WHERE s.id = ? AND imp.Datetime BETWEEN ? AND ? "
                + "ORDER BY imp.Datetime DESC, imp.id DESC";
        return queryListWithDateRange(sql, s.getId(), startDate, endDate);
    }

    /**
     * Returns import invoices for an ItemStatistic4 in a date range.
     */
    public List<ImportInvoice4> getImportInvoice(ItemStatistic4 item, String startDate, String endDate)
            throws SQLException {
        String sql = BASE_SELECT
                + "WHERE i.id = ? AND imp.Datetime BETWEEN ? AND ? "
                + "ORDER BY imp.Datetime DESC, imp.id DESC";
        return queryListWithDateRange(sql, item.getId(), startDate, endDate);
    }

    /**
     * Returns all import invoices in a date range (for RevenueStatisticView4 and InvoiceDetailView4).
     */
    public List<ImportInvoice4> getImportInvoice(String startDate, String endDate)
            throws SQLException {
        List<ImportInvoice4> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE imp.Datetime BETWEEN ? AND ? ORDER BY imp.Datetime DESC, imp.id DESC";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapImportInvoice(rs));
                }
            }
        }
        return list;
    }

    private List<ImportInvoice4> queryListWithDateRange(String sql, int targetId, String startDate, String endDate)
            throws SQLException {
        List<ImportInvoice4> list = new ArrayList<>();
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapImportInvoice(rs));
                }
            }
        }
        return list;
    }

    private ImportInvoice4 mapImportInvoice(ResultSet rs) throws SQLException {
        Supplier4 sup = new Supplier4();
        sup.setId(rs.getInt("sup_id"));
        sup.setName(rs.getString("sup_name"));
        sup.setAddress(rs.getString("sup_address"));
        sup.setEmail(rs.getString("sup_email"));
        sup.setPhonenumber(rs.getString("sup_phone"));

        Item4 item = new Item4();
        item.setId(rs.getInt("tblItem4id"));
        item.setName(rs.getString("item_name"));
        item.setPrice(rs.getInt("item_price"));
        item.setImportPrice(rs.getInt("item_import_price"));
        item.setQuantity(rs.getInt("item_qty"));
        item.setNote(rs.getString("item_note"));
        item.setSupplier(sup);

        WarehouseStaff4 staff = new WarehouseStaff4();
        staff.setId(rs.getInt("tblWarehouseStaff4id"));
        staff.setName(rs.getString("staff_name"));
        staff.setUsername(rs.getString("staff_username"));

        ImportInvoice4 inv = new ImportInvoice4();
        inv.setId(rs.getInt("id"));
        inv.setDatetime(rs.getDate("Datetime"));
        inv.setQuantity(rs.getInt("quantity"));
        inv.setDuetime(rs.getDate("Duetime"));
        inv.setTotalAmount(rs.getInt("totalAmount"));
        inv.setwStaff(staff);
        inv.setItem(item);
        return inv;
    }
}
