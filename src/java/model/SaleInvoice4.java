package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Counter Sale Invoice entity.
 */
public class SaleInvoice4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected Date datetime;
    protected int totalAmount;
    protected Saler4 saler;
    protected Customer4 customer;
    protected List<SaleInvoiceItem4> items = new ArrayList<>();

    public SaleInvoice4() {
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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Saler4 getSaler() {
        return saler;
    }

    public void setSaler(Saler4 saler) {
        this.saler = saler;
    }

    public Customer4 getCustomer() {
        return customer;
    }

    public void setCustomer(Customer4 customer) {
        this.customer = customer;
    }

    public List<SaleInvoiceItem4> getItems() {
        return items;
    }

    public void setItems(List<SaleInvoiceItem4> items) {
        this.items = items;
    }
}
