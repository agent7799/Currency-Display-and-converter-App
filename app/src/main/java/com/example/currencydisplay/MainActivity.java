package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static URL link;
    private static final String TAG_VALUTE = "Valute";
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static Date date;
    private static Date previousDate;
    private static URL previousURL;
    private static Time timestamp;
    private static JsonArray jsonArray = new JsonArray();
    private static List<Valute> valuteList = new ArrayList<>();


    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;


    {
        try {
            link = new URL(currencies);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Thread secThread;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setValuteRecycler(valuteList);


    }

    private void setValuteRecycler(List<Valute> valuteList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        valuteRecycler = findViewById(R.id.valuteRecycler);
        valuteRecycler.setLayoutManager(layoutManager);
        valuteAdapter = new ValuteAdapter(this, valuteList);
        valuteRecycler.setAdapter(valuteAdapter);
    }

    private void init() {
        runnable = new Runnable() {
            @Override
            public void run() {

                jsonArray = getWeb();

                valuteList = createValuteList(jsonArray);


            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private JsonArray getWeb() {
        JsonArray jsonArr = new JsonArray();
        try (InputStream in = link.openStream()) {
//Convert the input stream to a json element
            JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));
//May be an array, may be an object.
            JsonObject jsonObject = root.getAsJsonObject();
//string from valute JSON
            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            //Log.d("MyLog", "Valute string: " + val);
            String[] res = val.split("\\},");
//add  JSON objects to JSON array from strings
            for (int i = 0; i < res.length; i++) {
                if (i < res.length - 1) {
                    res[i] = res[i].substring(6) + "}";
                } else {
                    res[i] = res[i].substring(6);
                }
                jsonArr.add(parseString(res[i]).getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("MyLog", "JsonArr created: " + jsonArr);
        return jsonArr;
    }


    private List<Valute> createValuteList(JsonArray jsonArray) {
        List<Valute> list = new ArrayList<>();
        for (int i = 0 ; i < jsonArray.size(); i++){
            JsonObject obj = (JsonObject) jsonArray.get(i);
            list.add(new Valute(
                    i,
                    obj.get("Nominal").getAsInt(),
                    obj.get("Name").getAsString(),
                    (long) (obj.get("Value").getAsDouble() * 100)));
        }

        for (int j = 0; j < 3; j++) {
            Log.d("MyLog", "createValuteList result: "
                    + "id: " + list.get(j).getId() + ", "
                    + list.get(j).getValue() + " рублей за "
                    + list.get(j).getNominal() + " "
                    + list.get(j).getName());
        }
        Log.d("MyLog",  " list of " + list.size() + " valutes created by createValuteList...");
        return list;
    }
}