package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Customer4;
import model.Item4;
import model.SaleInvoice4;
import model.SaleInvoiceItem4;
import model.Saler4;

/**
 * DAO for counter Sale Invoices created by Saler staff.
 */
public class SaleInvoiceDAO4 extends DAO4 {

    private static final String INSERT_SALE_SQL =
            "INSERT INTO tblSaleInvoice4 (Datetime, totalAmount, tblSaler4id, tblCustomer4id) VALUES (?, ?, ?, ?)";

    private static final String INSERT_SALE_ITEM_SQL =
            "INSERT INTO tblSaleInvoiceItem4 (quantity, totalAmount, tblItem4id, tblSaleInvoice4id) VALUES (?, ?, ?, ?)";

    private static final String DECREMENT_STOCK_SQL =
            "UPDATE tblItem4 SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

    private static final String SELECT_SALES_BASE =
            "SELECT si.*, us.name AS saler_name, uc.name AS cust_name, c.AccCode "
            + "FROM tblSaleInvoice4 si "
            + "INNER JOIN tblUser4 us ON si.tblSaler4id = us.id "
            + "LEFT JOIN tblCustomer4 c ON si.tblCustomer4id = c.tblUser4id "
            + "LEFT JOIN tblUser4 uc ON c.tblUser4id = uc.id ";

    private static final String SELECT_SALE_ITEMS_SQL =
            "SELECT sii.*, i.name AS item_name, i.price AS item_price "
            + "FROM tblSaleInvoiceItem4 sii "
            + "INNER JOIN tblItem4 i ON sii.tblItem4id = i.id "
            + "WHERE sii.tblSaleInvoice4id = ?";

    public SaleInvoiceDAO4() {
        super();
    }

    /**
     * Persists a counter sale invoice, its line items, and decrements inventory atomically.
     */
    public boolean createSaleInvoice(SaleInvoice4 invoice) throws SQLException {
        try (Connection conn = openConnection()) {
            conn.setAutoCommit(false);
            try {
                int invoiceId;
                try (PreparedStatement ps = conn.prepareStatement(INSERT_SALE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, invoice.getDatetime());
                    ps.setInt(2, invoice.getTotalAmount());
                    ps.setInt(3, invoice.getSaler().getId());
                    if (invoice.getCustomer() != null && invoice.getCustomer().getId() > 0) {
                        ps.setInt(4, invoice.getCustomer().getId());
                    } else {
                        ps.setNull(4, Types.INTEGER);
                    }
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Failed to create sale invoice ID.");
                        }
                        invoiceId = keys.getInt(1);
                    }
                }

                try (PreparedStatement psItem = conn.prepareStatement(INSERT_SALE_ITEM_SQL);
                     PreparedStatement psStock = conn.prepareStatement(DECREMENT_STOCK_SQL)) {
                    for (SaleInvoiceItem4 sii : invoice.getItems()) {
                        psStock.setInt(1, sii.getQuantity());
                        psStock.setInt(2, sii.getItem().getId());
                        psStock.setInt(3, sii.getQuantity());
                        int updated = psStock.executeUpdate();
                        if (updated == 0) {
                            throw new SQLException("Sản phẩm '" + sii.getItem().getName() + "' không đủ tồn kho.");
                        }

                        psItem.setInt(1, sii.getQuantity());
                        psItem.setInt(2, sii.getTotalAmount());
                        psItem.setInt(3, sii.getItem().getId());
                        psItem.setInt(4, invoiceId);
                        psItem.executeUpdate();
                    }
                }

                invoice.setId(invoiceId);
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

    public List<SaleInvoice4> getRecentSaleInvoices() throws SQLException {
        List<SaleInvoice4> list = new ArrayList<>();
        String sql = SELECT_SALES_BASE + "ORDER BY si.id DESC LIMIT 20";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapSaleInvoice(rs));
            }
        }
        return list;
    }

    public SaleInvoice4 getSaleInvoiceById(int id) throws SQLException {
        SaleInvoice4 invoice = null;
        String sql = SELECT_SALES_BASE + "WHERE si.id = ?";
        try (Connection conn = openConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        invoice = mapSaleInvoice(rs);
                    }
                }
            }
            if (invoice != null) {
                List<SaleInvoiceItem4> items = new ArrayList<>();
                try (PreparedStatement psItems = conn.prepareStatement(SELECT_SALE_ITEMS_SQL)) {
                    psItems.setInt(1, id);
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        while (rsItems.next()) {
                            Item4 item = new Item4();
                            item.setId(rsItems.getInt("tblItem4id"));
                            item.setName(rsItems.getString("item_name"));
                            item.setPrice(rsItems.getInt("item_price"));

                            SaleInvoiceItem4 sii = new SaleInvoiceItem4();
                            sii.setId(rsItems.getInt("id"));
                            sii.setQuantity(rsItems.getInt("quantity"));
                            sii.setTotalAmount(rsItems.getInt("totalAmount"));
                            sii.setItem(item);
                            items.add(sii);
                        }
                    }
                }
                invoice.setItems(items);
            }
        }
        return invoice;
    }

    private SaleInvoice4 mapSaleInvoice(ResultSet rs) throws SQLException {
        Saler4 saler = new Saler4();
        saler.setId(rs.getInt("tblSaler4id"));
        saler.setName(rs.getString("saler_name"));

        SaleInvoice4 si = new SaleInvoice4();
        si.setId(rs.getInt("id"));
        si.setDatetime(rs.getDate("Datetime"));
        si.setTotalAmount(rs.getInt("totalAmount"));
        si.setSaler(saler);

        int custId = rs.getInt("tblCustomer4id");
        if (!rs.wasNull()) {
            Customer4 cust = new Customer4();
            cust.setId(custId);
            cust.setName(rs.getString("cust_name"));
            cust.setAccCode(rs.getString("AccCode"));
            si.setCustomer(cust);
        }
        return si;
    }
}
