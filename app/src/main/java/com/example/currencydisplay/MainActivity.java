package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final String TAG_NAME = "Name";
    private static final String TAG_RATE = "Value";
    private static final String TAG_NOMINAL = "Nominal";
    private static final String TAG_VALUTE = "Valute";
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static Date date;
    private static Date previousDate;
    private static URL previousURL;
    private static Time timestamp;

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
    }

    private void init() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();

            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb() {
        JsonArray jsonArray = new JsonArray();

        try (InputStream in =  link.openStream()){
//Convert the input stream to a json element
            JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));
            JsonObject jsonObject = root.getAsJsonObject(); //May be an array, may be an object.
//string from valute JSON
            //String val = jsonObject.get(TAG_VALUTE).toString();
            JsonObject valuteJson = jsonObject.getAsJsonObject(TAG_VALUTE);

            Log.d("MyLog", "array js: " + valuteJson.isJsonArray());

            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            //Log.d("MyLog", "Valute string: " + val);
            String[] res = val.split("\\},");


//add  JSON objects to JSON array  from strings
            for (int i = 0; i < res.length; i++) {
                if (i< res.length-1){
                    res[i] = res[i].substring(6) + "}";
                } else {
                    res[i] =  res[i].substring(6);
                }
                //Log.d("MyLog","res string: " + res[i]);
                jsonArray.add(parseString(res[i]).getAsJsonObject());
            }

            createValuteList(jsonArray);
            Log.d("MyLog","res string: " + createValuteList(jsonArray).size() + " vallutes created");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



private List<Valute> createValuteList(JsonArray jsonArray){
        List<Valute> valuteList = new ArrayList<>();
    //get every value from array
    Iterator i = jsonArray.iterator();
    while (i.hasNext()) {
        JsonObject obj = (JsonObject) i.next();
        valuteList.add(new Valute(
                obj.get("Nominal").getAsInt(),
                obj.get("Name").getAsString(),
                (long) (obj.get("Value").getAsDouble()*100)));
    }

    for (int j = 0; j < valuteList.size(); j++) {
        Log.d("MyLog", "createValuteList result: " + valuteList.get(j).getValue() + " рублей за " + valuteList.get(j).getNominal() + " " + valuteList.get(j).getName());
    }

        return valuteList;
}


}