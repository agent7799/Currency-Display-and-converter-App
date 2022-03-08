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


public class MainActivity extends AppCompatActivity {

    private Document doc;
    private static URL url;

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

    private void init(){
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

            JsonObject valute = (JsonObject) jsonObject.get("Valute");
            Log.d("MyLog","Valute JSON: " + valute);

            String val = jsonObject.get("Valute").toString();
            String vval = val.substring(1,val.length()-1);
            Log.d("MyLog","Valute string: " + vval);

            String[] res = vval.split("\\},");

            JsonArray jsonArray = new JsonArray();
            for (String s : res){
                //Log.d("MyLog","res string: " + s);
                jsonArray.add(s);
            }


             //get array elements
                    for( int i = 0; i < jsonArray.size(); i++){
                        Log.d("MyLog","i: " + jsonArray.get(i));
                    }
            //get every value from array
//            Iterator i = jsonValute.iterator();
//                    while (i.hasNext()){
//                        JsonObject innerObject = (JsonObject) i.next();
//                        System.out.println(innerObject.get("ID"));
//                    }



//            String date = jsonObject.get("Date").toString();
//            String valute = jsonObject.get("Valute").toString();

//            Log.d("MyLog","Valute: " + valute);
//            Log.d("MyLog","Date: " + date);

//            try (InputStream is = new URL(currencies).openStream();
//                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
//
//                Gson gson = new Gson();
//                String td = gson.fromJson(reader, String.class);
//
//                System.out.println(td);
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}