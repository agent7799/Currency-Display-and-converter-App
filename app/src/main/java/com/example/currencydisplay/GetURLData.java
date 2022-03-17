package com.example.currencydisplay;

import static com.example.currencydisplay.MainActivity.TAG_VALUTE;
import static com.example.currencydisplay.MainActivity.jsonArray;
import static com.example.currencydisplay.MainActivity.valuteList;
import static com.google.gson.JsonParser.parseReader;
import static com.google.gson.JsonParser.parseString;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLData extends AsyncTask<String, Integer, Void> {

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
                counter++;
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
        // [... Обновите индикатор хода выполнения, уведомления или другой
        // элемент пользовательского интерфейса ...]
    }

}