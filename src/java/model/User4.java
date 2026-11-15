package model;

import java.io.Serializable;

/**
 * Base User entity representing all system users.
 */
public class User4 implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String name;
    protected String username;
    protected String password;
    protected String birthdate;
    protected String address;
    protected String email;
    protected String phonenumber;
    protected String note;

    public User4() {
    }

    public User4(int id, String name, String username, String password, String birthdate,
                 String address, String email, String phonenumber, String note) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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

    public String getRole() {
        return "User";
    }
}
