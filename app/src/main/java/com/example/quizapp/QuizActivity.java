package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//import android.support.v7.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore"; // used with SharedPrefs
    public static List<Integer> userAnswers = new ArrayList<>(); // store user choices
    private static final long COUNTDOWN_IN_MILLIS = 20000; // used with countdown timer

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    public static List<Question> questionList;
    public static int questionCounter;
    public static int questionCountTotal;
    private Question currentQuestion;

    public static int score;
    private boolean answered;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Instantiate GUI objects
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        TextView txtScore = findViewById(R.id.textViewTitle);

        // set properties
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        // DBHelper to access database
        QuizDbHelper dbHelper = new QuizDbHelper(this);

        // Retrieve questions from database
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();

        // Shuffle order of questions each time game start
        //Collections.shuffle(questionList);

        userAnswers.clear(); // clear list
        showNextQuestion(); // go to next question if is not the last one

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                showNextQuestion();
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) { // if is not the last question load the next one
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Next");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else { // else go to results activity
            startActivity(new Intent(QuizActivity.this, ResultsActivity.class));
        }
    }


    // Check the answer and increment some counters used for statistics
    private void checkAnswer() {
        answered = true;
        int answerNr = -1;

        countDownTimer.cancel(); // stop countdown

        // Check if one of the answers were checked
        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

            RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
            answerNr = rbGroup.indexOfChild(rbSelected) + 1; // store user answer to further comparation
        } else{
            answerNr = -1; // if the user don't select nothing set this default value
        }

        userAnswers.add(answerNr); // list store user answered options

        // If is correct than add counters and update correct database column
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++; // activity textview counter
            currentQuestion.sumCorrectCounter(); // increase this question object field for correct answers
            QuizDbHelper.sumCorrectTotal(currentQuestion); // UPDATE database row for this question
            textViewScore.setText("Score: " + score); // show actual score
        }else{
            currentQuestion.sumWrongCounter(); // increase this question object field for wrong answers
            QuizDbHelper.sumWrongTotal(currentQuestion); // UPDATE database for this question
        }
    }

    // Countdown timer
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) { // set timer duration
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            // On countdown finish check answer automatically and go to next or to report activity
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
                showNextQuestion();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) { // if the countdown is less than 10 seconds change color
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) { // to go to previous screen you must press 2 times within less than 2 seconds
            finish();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}