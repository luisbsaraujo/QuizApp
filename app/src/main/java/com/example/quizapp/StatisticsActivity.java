package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// GraphView library
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class StatisticsActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    QuizDbHelper myHelper;
    BarGraphSeries<DataPoint> series;
    GraphView graph;
    Button buttonStartOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        graph = (GraphView) findViewById(R.id.graph);
        myHelper = new QuizDbHelper(this);
        sqLiteDatabase=myHelper.getReadableDatabase();

        //Read DB and retrieve counter
        series = new BarGraphSeries<DataPoint>(getData());
        //Draw values in Red on top of the graph
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.GRAY);
        //Add animated effect
        series.setAnimated(true);
        //Plot data and Load graph with data
        graph.addSeries(series);

        //Styling Graph properties
        graph.setTitle("Wrong Answers / Question");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Question Number");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Wrong Answers Total");
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH.NONE);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(10d);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setDrawBorder(true);

        // Change bars colors depending on the datapoint value
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        // Start a new game
        buttonStartOver = findViewById(R.id.buttonStartOverFromStats);
        buttonStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackToMainActIntent = new Intent(StatisticsActivity.this, MainActivity.class);
                startActivity(BackToMainActIntent);
            }
        });
    }

    // This method retrieve information about wrong total for questions in the database
    private DataPoint[] getData(){

        //name of the columns on the table
        String[] columns={QuizContract.QuestionsTable._ID,QuizContract.QuestionsTable.COLUMN_COUNT_WRONG};

        // run the query
        Cursor cursor = sqLiteDatabase.query(QuizContract.QuestionsTable.TABLE_NAME,columns,null,null,null,null,null);

        // create a DataPoint array to the size of the results count
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        // populate DataPoint array (x and y axis values) with
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getInt(0),cursor.getInt(1)); // retrieve columns information to dp array
        }
        return dp;
    }
}