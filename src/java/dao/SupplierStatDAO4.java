package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SupplierStatistic4;

/**
 * DAO for Supplier Statistics: getSupplierStat().
 */
public class SupplierStatDAO4 extends DAO4 {

    private static final String SUPPLIER_STAT_SQL =
            "SELECT s.id, s.name, s.address, s.email, s.phonenumber, s.note, "
            + "COALESCE(ord.online_rev, 0) + COALESCE(sal.direct_rev, 0) AS revenue, "
            + "COALESCE(ord.online_qty, 0) + COALESCE(sal.direct_qty, 0) AS sold_qty, "
            + "COALESCE(imp.import_amt, 0) AS import_amt, "
            + "COALESCE(imp.import_qty, 0) AS import_qty "
            + "FROM tblSupplier4 s "
            + "LEFT JOIN ( "
            + "    SELECT i.tblSupplier4id, SUM(oi.totalAmount) AS online_rev, SUM(oi.quantity) AS online_qty "
            + "    FROM tblOrderItem4 oi "
            + "    INNER JOIN tblOrder4 o ON oi.tblOrder4id = o.id "
            + "    INNER JOIN tblItem4 i ON oi.tblItem4id = i.id "
            + "    WHERE o.Datetime BETWEEN ? AND ? AND o.status <> 'Cancelled' "
            + "    GROUP BY i.tblSupplier4id "
            + ") ord ON s.id = ord.tblSupplier4id "
            + "LEFT JOIN ( "
            + "    SELECT i.tblSupplier4id, SUM(sii.totalAmount) AS direct_rev, SUM(sii.quantity) AS direct_qty "
            + "    FROM tblSaleInvoiceItem4 sii "
            + "    INNER JOIN tblSaleInvoice4 si ON sii.tblSaleInvoice4id = si.id "
            + "    INNER JOIN tblItem4 i ON sii.tblItem4id = i.id "
            + "    WHERE si.Datetime BETWEEN ? AND ? "
            + "    GROUP BY i.tblSupplier4id "
            + ") sal ON s.id = sal.tblSupplier4id "
            + "LEFT JOIN ( "
            + "    SELECT i.tblSupplier4id, SUM(ii.totalAmount) AS import_amt, SUM(ii.quantity) AS import_qty "
            + "    FROM tblImportInvoice4 ii "
            + "    INNER JOIN tblItem4 i ON ii.tblItem4id = i.id "
            + "    WHERE ii.Datetime BETWEEN ? AND ? "
            + "    GROUP BY i.tblSupplier4id "
            + ") imp ON s.id = imp.tblSupplier4id "
            + "WHERE s.name LIKE ? "
            + "ORDER BY revenue DESC, s.id ASC";

    public SupplierStatDAO4() {
        super();
    }

    public List<SupplierStatistic4> getSupplierStat(String key, String startDate, String endDate)
            throws SQLException {
        List<SupplierStatistic4> list = new ArrayList<>();
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);
        String pattern = "%" + (key == null ? "" : key.trim()) + "%";

        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(SUPPLIER_STAT_SQL)) {
            ps.setDate(1, start);
            ps.setDate(2, end);
            ps.setDate(3, start);
            ps.setDate(4, end);
            ps.setDate(5, start);
            ps.setDate(6, end);
            ps.setString(7, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSupplierStat(rs));
                }
            }
        }
        return list;
    }

    private SupplierStatistic4 mapSupplierStat(ResultSet rs) throws SQLException {
        SupplierStatistic4 stat = new SupplierStatistic4();
        stat.setId(rs.getInt("id"));
        stat.setName(rs.getString("name"));
        stat.setAddress(rs.getString("address"));
        stat.setEmail(rs.getString("email"));
        stat.setPhonenumber(rs.getString("phonenumber"));
        stat.setNote(rs.getString("note"));
        stat.setRevenue(rs.getLong("revenue"));
        stat.setTotalSoldQty(rs.getInt("sold_qty"));
        stat.setTotalImportAmount(rs.getLong("import_amt"));
        stat.setTotalImportedQty(rs.getInt("import_qty"));
        return stat;
    }
}
