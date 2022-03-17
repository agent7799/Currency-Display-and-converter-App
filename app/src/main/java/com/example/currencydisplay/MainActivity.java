package com.example.currencydisplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.google.gson.JsonArray;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    protected static final String TAG_VALUTE = "Valute";
    protected final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static Date date;
    private static Date previousDate;
    private static URL previousURL;
    private static Time timestamp;
    protected static JsonArray jsonArray = new JsonArray();
    protected static List<Valute> valuteList = new ArrayList<>();



    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GetURLData getURLData = new GetURLData();
        getURLData.execute(currencies);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        setValuteRecycler(valuteList);


    }

    protected void setValuteRecycler(List<Valute> valuteList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        valuteRecycler = findViewById(R.id.valuteRecycler);
        valuteRecycler.setLayoutManager(layoutManager);
        valuteAdapter = new ValuteAdapter(this, valuteList);
        valuteRecycler.setAdapter(valuteAdapter);
    }


}