package model;

import java.io.Serializable;

/**
 * Supplier entity.
 */
public class Supplier4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String name;
    protected String address;
    protected String email;
    protected String phonenumber;
    protected String note;

    public Supplier4() {
    }

    public Supplier4(int id, String name, String address, String email, String phonenumber, String note) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phonenumber = phonenumber;
        this.note = note;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
