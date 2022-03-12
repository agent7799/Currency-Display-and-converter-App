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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {

    private static URL link;
    private static final String TAG_NAME = "Name";
    private static final String TAG_RATE = "Value";
    private static final String TAG_NOMINAL = "Nominal";
    private static final String TAG_VALUTE = "Valute";
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";

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

        try (InputStream in =  link.openStream()){
            JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));//Convert the input stream to a json element
            JsonObject jsonObject = root.getAsJsonObject(); //May be an array, may be an object.

//string from valute JSON
            //String val = jsonObject.get(TAG_VALUTE).toString();

            JsonObject valuteJson = jsonObject.getAsJsonObject(TAG_VALUTE);
            Log.d("MyLog", "Valute js: " + valuteJson.toString());

            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            //Log.d("MyLog", "Valute string: " + val);
            String[] res = val.split("\\},");
            JsonArray jsonArray = new JsonArray();

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

            Map<String, Long> currencyMap = new TreeMap<String, Long>();
//add currency name and val to map
            String currencyName;
            Long currencyValue;
            for (int i = 0; i < jsonArray.size(); i++) {
                 //Log.d("MyLog","i: " + i + ": " +  jsonArray.get(i));
                 //Log.d("MyLog","i: " + i + ": " +  ((JsonObject)jsonArray.get(i)).get(TAG_NAME));

                currencyMap.put(
                        ((JsonObject)jsonArray.get(i)).get(TAG_NAME).toString(),
                        ((JsonObject)jsonArray.get(i)).get(TAG_RATE).getAsLong()
                );
            }

//get every value from array
            Iterator i = jsonArray.iterator();
            while (i.hasNext()) {
                 JsonObject innerObj = (JsonObject) i.next();


                 Log.d("MyLog", innerObj.get(TAG_RATE) + " рублей за " + innerObj.get(TAG_NOMINAL) + " " + innerObj.get(TAG_NAME));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}