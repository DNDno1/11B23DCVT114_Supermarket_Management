package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.RevenueStatistic4;

/**
 * DAO for overall Revenue Statistics: getRevenueStat().
 */
public class RevenueStatDAO4 extends DAO4 {

    private static final String DIRECT_REV_SQL =
            "SELECT COALESCE(SUM(totalAmount), 0) FROM tblSaleInvoice4 WHERE Datetime BETWEEN ? AND ?";

    private static final String ONLINE_REV_SQL =
            "SELECT COALESCE(SUM(totalAmount), 0) FROM tblOrder4 WHERE Datetime BETWEEN ? AND ? AND status <> 'Cancelled'";

    private static final String IMPORT_COST_SQL =
            "SELECT COALESCE(SUM(totalAmount), 0) FROM tblImportInvoice4 WHERE Datetime BETWEEN ? AND ?";

    public RevenueStatDAO4() {
        super();
    }

    public RevenueStatistic4 getRevenueStat(String startDate, String endDate) throws SQLException {
        RevenueStatistic4 stat = new RevenueStatistic4();
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);

        try (Connection conn = openConnection()) {
            long directRev = sumInRange(conn, DIRECT_REV_SQL, start, end);
            long onlineRev = sumInRange(conn, ONLINE_REV_SQL, start, end);
            long importCost = sumInRange(conn, IMPORT_COST_SQL, start, end);

            stat.setDirectRevenue(directRev);
            stat.setOnlineRevenue(onlineRev);
            stat.setRevenue(directRev + onlineRev);
            stat.setImportCost(importCost);
        }

        ImportInvoiceDAO4 importDao = new ImportInvoiceDAO4();
        stat.setImportInvoices(importDao.getImportInvoice(startDate, endDate));
        return stat;
    }

    private long sumInRange(Connection conn, String sql, Date start, Date end) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, start);
            ps.setDate(2, end);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0L;
    }
}
