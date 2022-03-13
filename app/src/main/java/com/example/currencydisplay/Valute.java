package com.example.currencydisplay;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


class Valute {
    private int nominal;
    private String name;
    private Long value;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }


    public Valute(int id, int nominal, String name, Long value) {
        this.nominal = nominal;
        this.name = name;
        this.value = value;

    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Valute.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
//        sb.append("nominal");
//        sb.append('=');
//        sb.append(((this.nominal == null)?"<null>":this.nominal));
//        sb.append(',');
//        sb.append("name");
//        sb.append('=');
//        sb.append(((this.name == null)?"<null>":this.name));
//        sb.append(',');
//        sb.append("value");
//        sb.append('=');
//        sb.append(((this.value == null)?"<null>":this.value));
//        sb.append(',');
//        sb.append("previous");
//        sb.append('=');
//        sb.append(((this.previous == null)?"<null>":this.previous));
//        sb.append(',');
//        if (sb.charAt((sb.length()- 1)) == ',') {
//            sb.setCharAt((sb.length()- 1), ']');
//        } else {
//            sb.append(']');
//        }
//        return sb.toString();
//    }







}
