package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseString;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {

    private Document doc;
    private static URL url;
    private static final String TAG_NAME = "Name";
    private static final String TAG_VALUE = "Value";
    private static final String TAG_NOMINAL = "Nominal";
    private static final String TAG_VALUTE = "Valute";

    private String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";


    {
        try {
            url = new URL(currencies);
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
        try {
//            doc = Jsoup.connect("url").get();

            //connecting url by Jsoup
            Connection connection = Jsoup.connect(currencies).ignoreContentType(true);
            connection.execute();
            //get json as string from url
            String strJson = connection.get().body().text();
            JsonElement jsonElement = parseString(strJson);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
//получили Valute JSON c сервера
            JsonObject valute = (JsonObject) jsonObject.get(TAG_VALUTE);
            Log.d("MyLog", "Valute JSON: " + valute);



//string from valute JSON
            //String val = jsonObject.get(TAG_VALUTE).toString();
            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            //Log.d("MyLog", "Valute string: " + val);
            String[] res = val.split("\\},");
            //JsonObject jobj = new JsonObject(res[0]);
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
                        ((JsonObject)jsonArray.get(i)).get(TAG_VALUE).getAsLong()
                );
            }

            //get every value from array
            Iterator i = jsonArray.iterator();
            while (i.hasNext()) {
                 JsonObject innerObj = (JsonObject) i.next();


                 Log.d("MyLog", innerObj.get(TAG_VALUE) + " рублей за " + innerObj.get(TAG_NOMINAL) + " " + innerObj.get(TAG_NAME));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}