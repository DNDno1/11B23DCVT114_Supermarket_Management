package model;

import java.io.Serializable;

/**
 * Line item inside an Online Order.
 */
public class OrderItem4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected int quantity;
    protected int totalAmount;
    protected Item4 item;

    public OrderItem4() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Item4 getItem() {
        return item;
    }

    public void setItem(Item4 item) {
        this.item = item;
    }
}
