package com.ns.quenfutsalbooking.Model;

public class Users {

    public String fullname, username, password, email, no_hp;

    public Users() {
    }

    public Users(String fullname, String username, String password, String email, String no_hp) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.no_hp = no_hp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}
