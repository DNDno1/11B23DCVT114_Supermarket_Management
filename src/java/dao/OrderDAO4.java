package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Customer4;
import model.Item4;
import model.Order4;
import model.OrderItem4;
import model.WarehouseStaff4;

/**
 * DAO for Online Orders: addOrderItem(), getOrder(), updateOrder().
 */
public class OrderDAO4 extends DAO4 {

    private static final String INSERT_ORDER_SQL =
            "INSERT INTO tblOrder4 (Datetime, status, totalAmount, tblCustomer4id, tblWarehouseStaff4id) "
            + "VALUES (?, ?, ?, ?, NULL)";

    private static final String INSERT_ORDER_ITEM_SQL =
            "INSERT INTO tblOrderItem4 (quantity, totalAmount, tblItem4id, tblOrder4id) VALUES (?, ?, ?, ?)";

    private static final String DECREMENT_ITEM_STOCK_SQL =
            "UPDATE tblItem4 SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

    private static final String SELECT_ORDERS_BASE =
            "SELECT o.*, uc.name AS cust_name, uc.address AS cust_address, uc.phonenumber AS cust_phone, "
            + "c.AccCode, uw.name AS staff_name "
            + "FROM tblOrder4 o "
            + "INNER JOIN tblCustomer4 c ON o.tblCustomer4id = c.tblUser4id "
            + "INNER JOIN tblUser4 uc ON c.tblUser4id = uc.id "
            + "LEFT JOIN tblUser4 uw ON o.tblWarehouseStaff4id = uw.id ";

    private static final String SELECT_ORDER_ITEMS_SQL =
            "SELECT oi.*, i.name AS item_name, i.price AS item_price, i.quantity AS item_stock "
            + "FROM tblOrderItem4 oi "
            + "INNER JOIN tblItem4 i ON oi.tblItem4id = i.id "
            + "WHERE oi.tblOrder4id = ?";

    private static final String UPDATE_ORDER_SQL =
            "UPDATE tblOrder4 SET status = ?, tblWarehouseStaff4id = ? WHERE id = ?";

    public OrderDAO4() {
        super();
    }

    /**
     * Creates an online order, saves its order items, and reserves stock in a single transaction.
     */
    public boolean addOrderItem(Order4 order) throws SQLException {
        try (Connection conn = openConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, order.getDatetime());
                    ps.setString(2, order.getStatus() != null ? order.getStatus() : "Pending");
                    ps.setInt(3, order.getTotalAmount());
                    ps.setInt(4, order.getCustomer().getId());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Failed to create order ID.");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                try (PreparedStatement psItem = conn.prepareStatement(INSERT_ORDER_ITEM_SQL);
                     PreparedStatement psStock = conn.prepareStatement(DECREMENT_ITEM_STOCK_SQL)) {
                    for (OrderItem4 oi : order.getItems()) {
                        psStock.setInt(1, oi.getQuantity());
                        psStock.setInt(2, oi.getItem().getId());
                        psStock.setInt(3, oi.getQuantity());
                        int updated = psStock.executeUpdate();
                        if (updated == 0) {
                            throw new SQLException("Sản phẩm '" + oi.getItem().getName() + "' không đủ số lượng tồn kho.");
                        }

                        psItem.setInt(1, oi.getQuantity());
                        psItem.setInt(2, oi.getTotalAmount());
                        psItem.setInt(3, oi.getItem().getId());
                        psItem.setInt(4, orderId);
                        psItem.executeUpdate();
                    }
                }

                order.setId(orderId);
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
     * Returns online orders for a specific customer.
     */
    public List<Order4> getOrder(int customerId) throws SQLException {
        List<Order4> list = new ArrayList<>();
        String sql = SELECT_ORDERS_BASE + "WHERE o.tblCustomer4id = ? ORDER BY o.id DESC";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
        }
        return list;
    }

    /**
     * Returns all online orders with their items for warehouse staff browsing and exporting.
     */
    public List<Order4> getOrder() throws SQLException {
        List<Order4> list = new ArrayList<>();
        String sql = SELECT_ORDERS_BASE + "ORDER BY CASE WHEN o.status = 'Pending' THEN 0 ELSE 1 END, o.id DESC";
        try (Connection conn = openConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
            for (Order4 order : list) {
                List<OrderItem4> items = new ArrayList<>();
                try (PreparedStatement psItems = conn.prepareStatement(SELECT_ORDER_ITEMS_SQL)) {
                    psItems.setInt(1, order.getId());
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        while (rsItems.next()) {
                            Item4 item = new Item4();
                            item.setId(rsItems.getInt("tblItem4id"));
                            item.setName(rsItems.getString("item_name"));
                            item.setPrice(rsItems.getInt("item_price"));
                            item.setQuantity(rsItems.getInt("item_stock"));

                            OrderItem4 oi = new OrderItem4();
                            oi.setId(rsItems.getInt("id"));
                            oi.setQuantity(rsItems.getInt("quantity"));
                            oi.setTotalAmount(rsItems.getInt("totalAmount"));
                            oi.setItem(item);
                            items.add(oi);
                        }
                    }
                }
                order.setItems(items);
            }
        }
        return list;
    }

    /**
     * Updates order status and warehouse staff when confirming order export (updateOrder).
     */
    public boolean updateOrder(int orderId, int warehouseStaffId, String status) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ORDER_SQL)) {
            ps.setString(1, status);
            ps.setInt(2, warehouseStaffId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order4 mapOrder(ResultSet rs) throws SQLException {
        Customer4 c = new Customer4();
        c.setId(rs.getInt("tblCustomer4id"));
        c.setName(rs.getString("cust_name"));
        c.setAddress(rs.getString("cust_address"));
        c.setPhonenumber(rs.getString("cust_phone"));
        c.setAccCode(rs.getString("AccCode"));

        Order4 o = new Order4();
        o.setId(rs.getInt("id"));
        o.setDatetime(rs.getDate("Datetime"));
        o.setStatus(rs.getString("status"));
        o.setTotalAmount(rs.getInt("totalAmount"));
        o.setCustomer(c);

        int staffId = rs.getInt("tblWarehouseStaff4id");
        if (!rs.wasNull()) {
            WarehouseStaff4 ws = new WarehouseStaff4();
            ws.setId(staffId);
            ws.setName(rs.getString("staff_name"));
            o.setwStaff(ws);
        }
        return o;
    }
}
