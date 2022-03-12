package com.example.currencydisplay;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Valute {


    private Date date;
    private Date previousDate;
    private URL previousURL;
    private Time timestamp;
    private String id;
    private String numCode;
    private String charCode;
    private Integer nominal;
    private String name;
    private Double value;
    private Double previous;
    private Map<String, Object> Valute = new HashMap<String, Object>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Valute.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("numCode");
        sb.append('=');
        sb.append(((this.numCode == null)?"<null>":this.numCode));
        sb.append(',');
        sb.append("charCode");
        sb.append('=');
        sb.append(((this.charCode == null)?"<null>":this.charCode));
        sb.append(',');
        sb.append("nominal");
        sb.append('=');
        sb.append(((this.nominal == null)?"<null>":this.nominal));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        sb.append("previous");
        sb.append('=');
        sb.append(((this.previous == null)?"<null>":this.previous));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }







}
