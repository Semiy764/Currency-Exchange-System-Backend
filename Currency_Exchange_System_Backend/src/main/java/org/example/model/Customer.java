package org.example.model;

public class Customer {

    private long id;
    private String fullname;
    private String nationalId;
    private String phoneNumber;

    public Customer(String fullname, long id, String nationalId, String phoneNumber) {
        this.fullname = fullname;
        this.id = id;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
