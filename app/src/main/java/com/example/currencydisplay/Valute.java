package com.example.currencydisplay;

import java.text.DecimalFormat;

class Valute {
    private int id;
    private int nominal;
    private String name;
    private double value;
    private DecimalFormat df = new DecimalFormat("0.00");

    public Valute(int id, int nominal, String name, double value) {
        this.id = id;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

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

    public double getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }


}
