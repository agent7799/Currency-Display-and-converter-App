package com.example.currencydisplay;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GsonParser {

    private static final String link = "https://www.cbr-xml-daily.ru/daily_json.js";

    public Valute parse() {
        Gson gson = new Gson();

        InputStream input = null;
        try {
            input = new URL(link).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));


        Valute root = gson.fromJson(reader, Valute.class);

        System.out.println("Valute " + root.toString());

        return root;
    }


}
