package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG_VALUTE = "Valute";
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static List<Valute> valuteList = new ArrayList<>();
    private static int progress;
    private Handler mHandler = new Handler();


    private Thread secThread;

    private RecyclerView.LayoutManager layoutManager;

    private void updateData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                parseJsonToValutesList(readJsonDataFromWeb(currencies));
//                GetURLData getURLData = new GetURLData();
//                getURLData.execute(currencies);

            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private TextView infoTextView;
    private TextView dateOfUpdateTextView;
    private ProgressBar progressBar;


    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = findViewById(R.id.infoTextView);
        dateOfUpdateTextView = findViewById(R.id.dateOfUpdate);
        progressBar = findViewById(R.id.progressbar);

        Button updateButton = findViewById(R.id.updateButton);
        Button removeButton = findViewById(R.id.removeButton);
        Button insertButton = findViewById(R.id.insertButton);


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
        removeButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                int index = 0;
                valuteList.remove(index);
                valuteAdapter.notifyItemRemoved(index);
                valuteAdapter.notifyDataSetChanged();
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                int insertIndex = 0;
                valuteList.add(insertIndex, new Valute(
                        insertIndex,
                        11,
                        "Nominal",
                        (long) (1234678)));
                valuteAdapter.notifyItemInserted(insertIndex);
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
            infoTextView.setText("Загрузка списка: ...");
            progressBar.setVisibility(View.VISIBLE); // показываем индикатор прогресса
        }

        @Override
        protected Void doInBackground(String... strings) {

            parseJsonToValutesList(readJsonDataFromWeb(strings[0]));
            publishProgress(progress);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            infoTextView.setText("Загрузка списка: " + progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            infoTextView.setText("Загрузка списка: завершено");
            infoTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE); // прячем бесконечный индикатор
            valuteAdapter.notifyDataSetChanged();
        }
    }

    protected JsonArray readJsonDataFromWeb(String link){
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] res;
        progress = 0;

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
            dateOfUpdateTextView.setText("Время обновления: " + formatter.format(date));

            String val = jsonObject.get(TAG_VALUTE).toString().substring(1, jsonObject.get(TAG_VALUTE).toString().length() - 1);
            res = val.split("\\},");
            for (int i = 0; i < res.length; i++) {      //add  JSON objects to JSON array from strings
                if (i < res.length - 1) {
                    res[i] = res[i].substring(6) + "}";
                } else {
                    res[i] = res[i].substring(6);
                }
                jsonArray.add(parseString(res[i]).getAsJsonObject());
                progress = i;
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

            try {
                takePause(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.d("MyLog", " list of " + valuteList.size() + " valutes created by createValuteList...");
    }

    private void takePause(int step) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

}