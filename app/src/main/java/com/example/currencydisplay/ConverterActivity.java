package com.example.currencydisplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ConverterActivity extends Activity {

    private EditText sum_field;
    double sum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_layout);

        Button backButton = findViewById(R.id.backButton);
        Button calculateButton = findViewById(R.id.calculateButton);
        EditText sum_field = findViewById(R.id.editSumText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent = new Intent(ConverterActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (sum_field.getText().toString().trim().equals("")) {
                    Toast.makeText(ConverterActivity.this, R.string.sum_roubles, Toast.LENGTH_LONG).show();
                } else {
                    sum = Double.parseDouble(String.valueOf(sum_field.getText()));
                }
            }
        });



    }
}
