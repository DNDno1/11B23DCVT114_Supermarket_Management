package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Import Invoice entity representing goods imported from a supplier.
 */
public class ImportInvoice4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected Date datetime;
    protected int quantity;
    protected Date duetime;
    protected int totalAmount;
    protected WarehouseStaff4 wStaff;
    protected Item4 item;

    public ImportInvoice4() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDuetime() {
        return duetime;
    }

    public void setDuetime(Date duetime) {
        this.duetime = duetime;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public WarehouseStaff4 getwStaff() {
        return wStaff;
    }

    public void setwStaff(WarehouseStaff4 wStaff) {
        this.wStaff = wStaff;
    }

    public Item4 getItem() {
        return item;
    }

    public void setItem(Item4 item) {
        this.item = item;
    }
}
