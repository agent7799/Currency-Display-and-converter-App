package com.example.currencydisplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class ConverterActivity extends Activity {

    int clickedItemIndex;
    String name;
    double rate;
    int nominal;
    double sum;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_layout);

        Button backButton = findViewById(R.id.backButton);
        Button calculateButton = findViewById(R.id.calculateButton);
        EditText sum_field = findViewById(R.id.editSumText);
        TextView valuteName = findViewById(R.id.valuteName);
        TextView result = findViewById(R.id.result);

        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra("clickedPosition")){
            clickedItemIndex = parentIntent.getIntExtra("clickedPosition", 0);
        }

        name = MainActivity.valuteList.get(clickedItemIndex).getName();
        rate = MainActivity.valuteList.get(clickedItemIndex).getValue();
        nominal = MainActivity.valuteList.get(clickedItemIndex).getNominal();

        valuteName.setText(name);

        backButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (sum_field.getText().toString().trim().equals("")) {
                    Toast.makeText(ConverterActivity.this, R.string.sum_roubles, Toast.LENGTH_LONG).show();
                    } else {
                    sum = Double.parseDouble(String.valueOf(sum_field.getText())) / rate / nominal;
                    result.setText(df.format(sum));
                }
            }
        });


    }
}
