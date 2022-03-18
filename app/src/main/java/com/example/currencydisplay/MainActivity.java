package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG_VALUTE = "Valute";
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static List<Valute> valuteList = new ArrayList<>();

    private Thread secThread;

    private RecyclerView.LayoutManager layoutManager;

    private void updateData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                parseJsonToValutesList(readJsonDataFromWeb(currencies));

            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }


    private TextView infoTextView;
    private TextView dateOfUpdate;
    private ProgressBar progressBar;
    private Button updateButton;

    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = findViewById(R.id.infoTextView);
        dateOfUpdate = findViewById(R.id.dateOfUpdate);

        progressBar = findViewById(R.id.progressbar);
        updateButton = findViewById(R.id.updateButton);

        GetURLData getURLData = new GetURLData();
        getURLData.execute(currencies);
        setValuteRecycler(valuteList);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                updateData();

                valuteAdapter.notifyDataSetChanged();
                }
        });
    }

    protected void setValuteRecycler(List<Valute> valuteList) {
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        valuteRecycler = findViewById(R.id.valuteRecycler);
        valuteRecycler.setLayoutManager(layoutManager);
        valuteAdapter = new ValuteAdapter(this, valuteList);
        valuteRecycler.setAdapter(valuteAdapter);
    }

    private class GetURLData extends AsyncTask<String, Integer, Void> {

        private int counter = 0;

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

            parseJsonToValutesList(readJsonDataFromWeb(strings[0]));

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            infoTextView.setText("Loading Currency: " + progress[0]);
        }

//        private void getFloor(int floor) throws InterruptedException {
//            TimeUnit.MILLISECONDS.sleep(100);
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            infoTextView.setText("Кот залез на крышу");
            infoTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE); // прячем бесконечный индикатор
            valuteAdapter.notifyDataSetChanged();
        }
    }

    protected JsonArray readJsonDataFromWeb(String link){
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] res;

//        for(int i = 0 ; i < jsonArray.size() ; i++){
//            jsonArray.remove(i);
//        }
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (InputStream in = url.openStream()) {

            JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));
            jsonObject = root.getAsJsonObject();

            //date = jsonObject.get("Date").toString();
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            dateOfUpdate.setText("Время обновления: " + formatter.format(date));

            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            res = val.split("\\},");
            for (int i = 0; i < res.length; i++) {      //add  JSON objects to JSON array from strings
                if (i < res.length - 1) {
                    res[i] = res[i].substring(6) + "}";
                } else {
                    res[i] = res[i].substring(6);
                }
                jsonArray.add(parseString(res[i]).getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("MyLog", jsonArray.size() + " elements of JsonArray created: " + jsonArray);
        return jsonArray;

    }

    protected void parseJsonToValutesList(JsonArray jsonArray){
        valuteList.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = (JsonObject) jsonArray.get(i);
            valuteList.add(new Valute(
                    i,
                    obj.get("Nominal").getAsInt(),
                    obj.get("Name").getAsString(),
                    (long) (obj.get("Value").getAsDouble() * 100)));
            //getFloor(counter);
            //publishProgress(++counter);
        }
        Log.d("MyLog", " list of " + valuteList.size() + " valutes created by createValuteList...");
    }


}