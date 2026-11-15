package model;

/**
 * Staff base model extending User4 with role attribute.
 */
public class Staff4 extends User4 {
    private static final long serialVersionUID = 1L;

    protected String role;

    public Staff4() {
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
