package model;

/**
 * Item statistics model extending Item4 with revenue and sold quantity.
 */
public class ItemStatistic4 extends Item4 {
    private static final long serialVersionUID = 1L;

    private long revenue;
    private int soldQuantity;

    public ItemStatistic4() {
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
