package org.example.model;
public class Teller extends UsersInfo {

    public Teller(){}

    public Teller (String fullname, long id, String nationalId,long userId, String phoneNumber) {
        this.fullname = fullname;
        this.id = id;
        this.userId = userId;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

    public Teller (String fullname, String nationalId,long userId, String phoneNumber) {
        this.fullname = fullname;
        this.id = id;
        this.userId = userId;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }
}
