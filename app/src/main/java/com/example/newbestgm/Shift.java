package com.example.newbestgm;

public class Shift {
    private String date;
    private String user;
    private String time;
    private int money;

    public Shift(){
    }

    public Shift(String date, String user, String time,int money) {
        this.date = date;
        this.user = user;
        this.time = time;
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

