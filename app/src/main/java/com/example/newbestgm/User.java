package com.example.newbestgm;

import android.util.ArrayMap;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String user_full_name;
    private String user_group_id;
    private String user_company_role;
    private String user_max_salary;
    private String phone;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    private ArrayList<Shift> past_shifts;
    private ArrayList<Shift> week_shifts;


    public User() {

    }

    public User(String user_full_name, String user_email, String user_password,
                String user_group_id, String user_company_role, String user_max_salary,
                ArrayList<Shift> past_shifts, ArrayList<Shift> week_shifts) {
        this.user_full_name = user_full_name;
        this.user_group_id = user_group_id;
        this.user_company_role = user_company_role;
        this.user_max_salary = user_max_salary;
        this.past_shifts = past_shifts;
        this.week_shifts = week_shifts;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getUser_group_id() {
        return user_group_id;
    }

    public void setUser_group_id(String user_group_id) {
        this.user_group_id = user_group_id;
    }

    public String getUser_company_role() {
        return user_company_role;
    }

    public void setUser_company_role(String user_company_role) {
        this.user_company_role = user_company_role;
    }

    public String getUser_max_salary() {
        return user_max_salary;
    }

    public void setUser_max_salary(String user_max_salary) {
        this.user_max_salary = user_max_salary;
    }

    public ArrayList<Shift> getPast_shifts() {
        return past_shifts;
    }

    public void setPast_shifts(ArrayList<Shift> past_shifts) {
        this.past_shifts = past_shifts;
    }

    public ArrayList<Shift> getWeek_shifts() {
        return week_shifts;
    }

    public void setWeek_shifts(ArrayList<Shift> week_shifts) {
        this.week_shifts = week_shifts;
    }
}
