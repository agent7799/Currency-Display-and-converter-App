package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Time;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    protected static final String TAG_VALUTE = "Valute";
    protected final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static Date date;
    private static Date previousDate;
    private static URL previousURL;
    private static Time timestamp;
    protected static JsonArray jsonArray = new JsonArray();
    protected static List<Valute> valuteList = new ArrayList<>();

    private TextView infoTextView;
    private ProgressBar progressBar;
    private ProgressBar horizontalProgressBar;

    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = findViewById(R.id.infoTextView);
        progressBar = findViewById(R.id.progressbar);
        horizontalProgressBar = findViewById(androidx.appcompat.R.id.progress_horizontal);


        GetURLData getURLData = new GetURLData();
        getURLData.execute(currencies);


        setValuteRecycler(valuteList);

    }

    protected void setValuteRecycler(List<Valute> valuteList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        valuteRecycler = findViewById(R.id.valuteRecycler);
        valuteRecycler.setLayoutManager(layoutManager);
        valuteAdapter = new ValuteAdapter(this, valuteList);
        valuteRecycler.setAdapter(valuteAdapter);
    }

    class GetURLData extends AsyncTask<String, Integer, Void> {

        //int counter = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoTextView.setVisibility(View.VISIBLE);
            infoTextView.setText("Loading Currency List ...");
            progressBar.setVisibility(View.VISIBLE); // показываем индикатор прогресса
            //horizontalProgressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(String... strings) {

            URL link = null;
            try {
                link = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try (InputStream in = link.openStream()) {
                int counter = 0;
                JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));
                JsonObject jsonObject = root.getAsJsonObject();
                String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
                String[] res = val.split("\\},");
                for (int i = 0; i < res.length; i++) {      //add  JSON objects to JSON array from strings
                    if (i < res.length - 1) {
                        res[i] = res[i].substring(6) + "}";
                    } else {
                        res[i] = res[i].substring(6);
                    }
                    jsonArray.add(parseString(res[i]).getAsJsonObject());
                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject obj = (JsonObject) jsonArray.get(i);
                    valuteList.add(new Valute(
                            i,
                            obj.get("Nominal").getAsInt(),
                            obj.get("Name").getAsString(),
                            (long) (obj.get("Value").getAsDouble() * 100)));
                    //getFloor(counter);
                    publishProgress(++counter);
                }
                Log.d("MyLog", " list of " + valuteList.size() + " valutes created by createValuteList...");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("MyLog", "JsonArr created: " + jsonArray);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            infoTextView.setText("Loading Currency: " + progress[0]);
        }

        private void getFloor(int floor) throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            infoTextView.setText("Кот залез на крышу");
            infoTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE); // прячем бесконечный индикатор
        }

    }
}