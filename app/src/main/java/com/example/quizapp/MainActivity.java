package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // global variables
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    public static List<String[]> MyQuestions = new ArrayList<>();
    private TextView textViewHighscore;
    public static int highscore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate GUI objects
        TextView name = findViewById(R.id.textViewName);
        textViewHighscore = findViewById(R.id.text_view_highscore);
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);

        // Google sign in builder object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Check if someone is already signed in with a google account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName(); // we are just using this property
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            name.setText("Hello, " + personName); // showing on the textview
        }

        // Read CSV/PSV file and return a List of strings
        MyQuestions = ReadCSV();

        // Load the highest score on the screen
        loadHighscore();

        // If start button is pushed then load new QuizActivity
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
    }

    // start QuizActivity
    private void startQuiz() {
        QuizActivity.score = 0;
        QuizActivity.userAnswers.clear(); // clear previous answers List
        QuizActivity.questionCounter = 0;

        Intent firstIntent = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(firstIntent);
        overridePendingTransition(R.transition.slide_in_right,R.transition.slide_out_left); // Add transition effect from XML

}
    // Get information stored in the local device SharedPrefs
    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0); // get data from associative array
        textViewHighscore.setText("Highscore: " + highscore);
    }

    // Read CSV/PSV file and return a List
    private List<String[]> ReadCSV(){

        List<String[]> resultList = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.questions); // file handler
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); // file reader

        try{
            String csvLine;

            while((csvLine = reader.readLine()) != null){ // while not the last line read and insert in the array
                String[] row = csvLine.split("\\|"); // split fields and store in row string array
                resultList.add(row);
            }

        }catch(IOException ex){
            throw new RuntimeException("Error reading CSV file: " + ex);
        }finally {
            try{
                inputStream.close(); // close reader
            }catch(IOException ex){
                throw new RuntimeException("Error while closing input stream: " + ex);
            }

        }
        return resultList;
    }
}

