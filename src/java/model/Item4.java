package model;

import java.io.Serializable;

/**
 * Supermarket Item / Product entity.
 */
public class Item4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String name;
    protected int quantity;
    protected int price;
    protected int importPrice;
    protected String note;
    protected Supplier4 supplier;

    public Item4() {
    }

    public Item4(int id, String name, int quantity, int price, int importPrice, String note, Supplier4 supplier) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.importPrice = importPrice;
        this.note = note;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Supplier4 getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier4 supplier) {
        this.supplier = supplier;
    }
}
