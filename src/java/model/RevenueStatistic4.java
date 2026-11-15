package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Revenue statistics summary across direct sales, online orders, and import expenses.
 */
public class RevenueStatistic4 implements Serializable {
    private static final long serialVersionUID = 1L;

    private long revenue;
    private long directRevenue;
    private long onlineRevenue;
    private long importCost;
    private List<ImportInvoice4> importInvoices = new ArrayList<>();

    public RevenueStatistic4() {
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getDirectRevenue() {
        return directRevenue;
    }

    public void setDirectRevenue(long directRevenue) {
        this.directRevenue = directRevenue;
    }

    public long getOnlineRevenue() {
        return onlineRevenue;
    }

    public void setOnlineRevenue(long onlineRevenue) {
        this.onlineRevenue = onlineRevenue;
    }

    public long getImportCost() {
        return importCost;
    }

    public void setImportCost(long importCost) {
        this.importCost = importCost;
    }

    public long getNetProfit() {
        return revenue - importCost;
    }

    public List<ImportInvoice4> getImportInvoices() {
        return importInvoices;
    }

    public void setImportInvoices(List<ImportInvoice4> importInvoices) {
        this.importInvoices = importInvoices;
    }
}
