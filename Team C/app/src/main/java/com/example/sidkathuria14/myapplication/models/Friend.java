package com.example.sidkathuria14.myapplication.models;

/**
 * Created by sidkathuria14 on 15/1/18.
 */

public class Friend {
   private String name;
    private String email;
    private String uid;
    int id;

    public Friend(String name, String email, String uid, int id) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.id = id;
    }

    public Friend(String name, String uid, int id) {
        this.name = name;

        this.uid = uid;
        this.id = id;
    }
public Friend(){

}
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }


    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
}
