package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseString;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


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

            String valute = jsonObject.get("Valute").toString();
            Log.d("MyLog","Valute: " + valute);

            JsonElement jsonElementValute = parseString(valute);
           // JsonArray jsonObjectValute = jsonObject.getAsJsonArray("Valute");


//            Map<String, Long> currMap = new TreeMap<>();
//            for (int i = 0; i < jsonObjectValute.size(); i++) {
//                Log.d("MyLog","Date: " + jsonObjectValute.get(i));
//            }
            String date = jsonObject.get("Date").toString();
            Log.d("MyLog","Date: " + date);




//            Log.d("MyLog","Title: " + doc.title());
//            Log.d("MyLog","Title: " + doc.text());
//            Log.d("MyLog","Title: " + doc.getAllElements());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}