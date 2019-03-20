package com.joung.vienna.admin.model;

import android.util.Base64;

import androidx.annotation.Keep;

@Keep
public class User {

    public static final int PERMISSION_SUPER_USER = 0;
    public static final int PERMISSION_WRITE_USER = 1;
    public static final int PERMISSION_NORMAL_USER = 2;

    private String name, password;
    private int permission;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.permission = 0;
    }

    public User(String name, String password, int permission) {
        this.name = name;
        this.password = password;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPermission() {
        return permission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    private void encodingPassword() {
        password = Base64.encodeToString(password.getBytes(), 0);
    }

    private void decodingPassword() {
        password = new String(Base64.decode(password, 0));
    }

    public boolean checkPassword(String password) {
        decodingPassword();
        return this.password.equals(password);
    }
}
