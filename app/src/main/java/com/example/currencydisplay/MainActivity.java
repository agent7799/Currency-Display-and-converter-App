package com.example.currencydisplay;

import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG_VALUTE = "Valute";
    private static final int MSG_UPDATE_NONE = 0;
    private static final int MSG_UPDATE_IN_PROGRESS = 1;
    private static final int MSG_UPDATE_COMPLETED = 2;
    private final String currencies = "https://www.cbr-xml-daily.ru/daily_json.js";
    protected static List<Valute> valuteList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;



    private TextView infoTextView;
    private TextView dateTextView;
    private TextView timestampTextView;
    private ProgressBar progressBar;

    RecyclerView valuteRecycler;
    ValuteAdapter valuteAdapter;

    Handler mHandler;

    private void updateData() {
        //Start new thread
        Thread secThread = new Thread(updater);
        secThread.start();
        //Same:   new Thread(updater).start();

    }

    private Runnable updater = new Runnable(){
            @Override
            public void run() {

                parseJsonToValutesList(readJsonDataFromWeb(currencies));

            }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = findViewById(R.id.infoTextView);
        dateTextView = findViewById(R.id.dateTextView);
        timestampTextView = findViewById(R.id.timestampTextView);
        progressBar = findViewById(R.id.progressbar);

        setValuteRecycler(valuteList);

        Button updateButton = findViewById(R.id.updateButton);
        //Button removeButton = findViewById(R.id.removeButton);
        //Button insertButton = findViewById(R.id.insertButton);
        //Button converterButton = findViewById(R.id.converterButton);



        //start heavy task in not main thread
        GetURLData getURLData = new GetURLData();
        getURLData.execute(currencies);

        mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                    switch (message.what) {
                        case MSG_UPDATE_NONE:
                            updateButton.setEnabled(true);
                            updateButton.setVisibility(View.VISIBLE);
                            break;
                        case MSG_UPDATE_IN_PROGRESS:
                            updateButton.setEnabled(false);
                            updateButton.setAlpha(0.1f);
                            updateButton.setVisibility(View.VISIBLE);
                            break;
                        case MSG_UPDATE_COMPLETED:
                            updateButton.setEnabled(true);
                            updateButton.setAlpha(1f);
                            valuteAdapter.notifyDataSetChanged();
                            break;
                    }
                mHandler.sendEmptyMessage(MSG_UPDATE_NONE);
                return true;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(MSG_UPDATE_IN_PROGRESS);
                Log.d("MyLog", "update button clicked... " + mHandler.obtainMessage());
                updateData();
                try {
                    takePause(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(MSG_UPDATE_COMPLETED);
                }
        });
//        removeButton.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
//                int index = 0;
//                valuteList.remove(index);
//                valuteAdapter.notifyItemRemoved(index);
//                valuteAdapter.notifyDataSetChanged();
//            }
//        });
//        insertButton.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
//                int insertIndex = 0;
//                valuteList.add(insertIndex, new Valute(
//                        insertIndex,
//                        11,
//                        "Nominal",
//                        (long) (1234678)));
//                valuteAdapter.notifyItemInserted(insertIndex);
//                valuteAdapter.notifyDataSetChanged();
//            }
//        });
//        converterButton.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ConverterActivity.class);
//                startActivity(intent);
//            }
//        });

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

            //publishProgress(progress);
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

        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (InputStream in = url.openStream()) {

            JsonElement root = parseReader(new BufferedReader(new InputStreamReader(in)));
            jsonObject = root.getAsJsonObject();

            String jsonDateString = jsonObject.get("Date").toString();
            String timestampString = jsonObject.get("Timestamp").toString();

            LocalDateTime jsonDate = LocalDateTime.parse(jsonDateString.substring(1,jsonDateString.length()-1), ISO_OFFSET_DATE_TIME);
            LocalDateTime timestamp = LocalDateTime.parse(timestampString.substring(1,timestampString.length()-1), ISO_OFFSET_DATE_TIME);

            DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("H:mm  dd-MM-yyyy ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            dateTextView.setText("на : " + jsonDate.format(formatter));
            timestampTextView.setText("Последнее обновление : " + timestamp.format(formatterHMS));

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
        Log.d("MyLog", jsonArray.size() + " elements of JsonArray created");



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
                    obj.get("Value").getAsDouble()));
            try {
                takePause(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.d("MyLog", " list of " + valuteList.size() + " valutes created by createValuteList...");
    }

    private void takePause(int step) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(step);
    }

}