package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ItemStatistic4;
import model.Supplier4;

/**
 * DAO for Item Statistics: getItemStat().
 */
public class ItemStatDAO4 extends DAO4 {

    private static final String ITEM_STAT_SQL =
            "SELECT i.*, s.name AS sup_name, "
            + "COALESCE(ord.online_rev, 0) + COALESCE(sal.direct_rev, 0) AS revenue, "
            + "COALESCE(ord.online_qty, 0) + COALESCE(sal.direct_qty, 0) AS sold_qty "
            + "FROM tblItem4 i "
            + "LEFT JOIN tblSupplier4 s ON i.tblSupplier4id = s.id "
            + "LEFT JOIN ( "
            + "    SELECT oi.tblItem4id, SUM(oi.totalAmount) AS online_rev, SUM(oi.quantity) AS online_qty "
            + "    FROM tblOrderItem4 oi "
            + "    INNER JOIN tblOrder4 o ON oi.tblOrder4id = o.id "
            + "    WHERE o.Datetime BETWEEN ? AND ? AND o.status <> 'Cancelled' "
            + "    GROUP BY oi.tblItem4id "
            + ") ord ON i.id = ord.tblItem4id "
            + "LEFT JOIN ( "
            + "    SELECT sii.tblItem4id, SUM(sii.totalAmount) AS direct_rev, SUM(sii.quantity) AS direct_qty "
            + "    FROM tblSaleInvoiceItem4 sii "
            + "    INNER JOIN tblSaleInvoice4 si ON sii.tblSaleInvoice4id = si.id "
            + "    WHERE si.Datetime BETWEEN ? AND ? "
            + "    GROUP BY sii.tblItem4id "
            + ") sal ON i.id = sal.tblItem4id "
            + "WHERE i.name LIKE ? "
            + "ORDER BY revenue DESC, i.id ASC";

    public ItemStatDAO4() {
        super();
    }

    public List<ItemStatistic4> getItemStat(String key, String startDate, String endDate)
            throws SQLException {
        List<ItemStatistic4> list = new ArrayList<>();
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);
        String pattern = "%" + (key == null ? "" : key.trim()) + "%";

        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(ITEM_STAT_SQL)) {
            ps.setDate(1, start);
            ps.setDate(2, end);
            ps.setDate(3, start);
            ps.setDate(4, end);
            ps.setString(5, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapItemStat(rs));
                }
            }
        }
        return list;
    }

    private ItemStatistic4 mapItemStat(ResultSet rs) throws SQLException {
        Supplier4 sup = new Supplier4();
        sup.setId(rs.getInt("tblSupplier4id"));
        sup.setName(rs.getString("sup_name"));

        ItemStatistic4 stat = new ItemStatistic4();
        stat.setId(rs.getInt("id"));
        stat.setName(rs.getString("name"));
        stat.setQuantity(rs.getInt("quantity"));
        stat.setPrice(rs.getInt("price"));
        stat.setImportPrice(rs.getInt("ImportPrice"));
        stat.setNote(rs.getString("note"));
        stat.setSupplier(sup);
        stat.setRevenue(rs.getLong("revenue"));
        stat.setSoldQuantity(rs.getInt("sold_qty"));
        return stat;
    }
}
