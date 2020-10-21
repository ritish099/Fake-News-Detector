package com.example.fnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class ScoreActivity extends AppCompatActivity {

    String f,r;
    float fake, real;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            f = extras.getString("fake");
            r = extras.getString("real");

            real=Float.parseFloat(r)*100;
            fake=Float.parseFloat(f)*100;

            //The key argument here must match that used in the other activity
        }

        PieChart mPieChart = findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Fake", fake, Color.parseColor("#FF0000")));
        mPieChart.addPieSlice(new PieModel("Real", real, Color.parseColor("#7FFF00")));

        mPieChart.startAnimation();
    }

    public void backToMain(View view) {
        Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
        startActivity(intent);
    }
}