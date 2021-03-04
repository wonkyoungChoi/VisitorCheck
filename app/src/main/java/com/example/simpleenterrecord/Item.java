package com.example.simpleenterrecord;

public class Item {
    String num;
    String time;
    String phoneNum;
    String city;
    String temperature;

    public Item(String num, String time, String phoneNum, String city, String temperature) {
        this.num = num;
        this.time = time;
        this.phoneNum = phoneNum;
        this.city = city;
        this.temperature = temperature;
    }
    public Item(){};

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() { return temperature; }

    public void setTemperature(String temperature) { this.temperature = temperature; }
}
