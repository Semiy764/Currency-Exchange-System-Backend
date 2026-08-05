package org.example.model;

public class Customer extends UsersInfo {

    public Customer(){}

    public Customer(String fullname, long id, String nationalId,long userId, String phoneNumber) {
        this.fullname = fullname;
        this.id = id;
        this.userId = userId;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

    public Customer(String fullname, String nationalId,long userId, String phoneNumber) {
        this.fullname = fullname;
        this.id = id;
        this.userId = userId;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

}
