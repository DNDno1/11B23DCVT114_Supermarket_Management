package model;

/**
 * Supplier statistics model extending Supplier4 with revenue and import/sales metrics.
 */
public class SupplierStatistic4 extends Supplier4 {
    private static final long serialVersionUID = 1L;

    private long revenue;
    private long totalImportAmount;
    private int totalImportedQty;
    private int totalSoldQty;

    public SupplierStatistic4() {
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getTotalImportAmount() {
        return totalImportAmount;
    }

    public void setTotalImportAmount(long totalImportAmount) {
        this.totalImportAmount = totalImportAmount;
    }

    public int getTotalImportedQty() {
        return totalImportedQty;
    }

    public void setTotalImportedQty(int totalImportedQty) {
        this.totalImportedQty = totalImportedQty;
    }

    public int getTotalSoldQty() {
        return totalSoldQty;
    }

    public void setTotalSoldQty(int totalSoldQty) {
        this.totalSoldQty = totalSoldQty;
    }
}
