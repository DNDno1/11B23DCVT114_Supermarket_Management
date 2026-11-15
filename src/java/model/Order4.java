package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Online Order entity.
 */
public class Order4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected Date datetime;
    protected String status;
    protected int totalAmount;
    protected Customer4 customer;
    protected WarehouseStaff4 wStaff;
    protected List<OrderItem4> items = new ArrayList<>();

    public Order4() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer4 getCustomer() {
        return customer;
    }

    public void setCustomer(Customer4 customer) {
        this.customer = customer;
    }

    public WarehouseStaff4 getwStaff() {
        return wStaff;
    }

    public void setwStaff(WarehouseStaff4 wStaff) {
        this.wStaff = wStaff;
    }

    public List<OrderItem4> getItems() {
        return items;
    }

    public void setItems(List<OrderItem4> items) {
        this.items = items;
    }
}
