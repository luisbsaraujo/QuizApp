package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private List<Question> questionList;
    private List<Question> correctAnswerList;
    private Button buttonStartOver;
    private Button statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // dbHelper to access database
        QuizDbHelper dbHelper = new QuizDbHelper(this);

        questionList = QuizActivity.questionList;
        correctAnswerList = QuizActivity.questionList;

        // Instantiate GUI objects
        buttonStartOver = findViewById(R.id.buttonStartOver);
        statistics = findViewById(R.id.buttonStatistics);
        ListView listViewItems = findViewById(R.id.listViewItems);
        TextView txtScore = findViewById(R.id.textViewTitle);

        txtScore.setText("SCORE: " + QuizActivity.score + "/" + questionList.size()); // total score for the actual game

        // use a custom adapter to show a scrollable ListView
        listViewItems.setAdapter(new MyCustomAdapter(ResultsActivity.this, questionList, correctAnswerList));

        // Start a new game
        buttonStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHighScore(QuizActivity.score); // save the score on sharedPrefs
                Intent BackToMainActIntent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(BackToMainActIntent);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHighScore(QuizActivity.score);

                // Start Statistics activity with GraphView
                Intent statsIntent = new Intent(ResultsActivity.this, StatisticsActivity.class);
                startActivity(statsIntent);
            }
        });

    }

    public void UpdateHighScore(int highscore){

        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (QuizActivity.score > MainActivity.highscore)
        {
            editor.putInt(MainActivity.KEY_HIGHSCORE, highscore);
        }
        editor.apply();
    }
}
