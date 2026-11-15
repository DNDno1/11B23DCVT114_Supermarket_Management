package model;

/**
 * Customer entity extending User4 with member account code.
 */
public class Customer4 extends User4 {
    private static final long serialVersionUID = 1L;

    protected String accCode;

    public Customer4() {
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
