package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends BaseAdapter {

    List<Question> myQuestions = new ArrayList<>();
    List<Question> correctAns = new ArrayList<>();
    Context context;

    // default constructor, retrieve all the important information to display on the listView
    public MyCustomAdapter(Context anyContext, List<Question> questions, List<Question> correctAnswers) {
        myQuestions = questions;
        correctAns = correctAnswers;
        context = anyContext;
    }

    @Override
    public int getCount() {
        return myQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        return myQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) // if have no items loaded than inflate
        {
            LayoutInflater myLayoutInflater = LayoutInflater.from(context);
            convertView = myLayoutInflater.inflate(
                    R.layout.external_layout,
                    parent,
                    false);
        }

        // create "container"
        TextView txtViewQuestion = convertView.findViewById(R.id.textViewQuestion);
        TextView txtViewMyAns = convertView.findViewById(R.id.textViewYourAns);
        TextView txtViewCorrectAns = convertView.findViewById(R.id.textViewCorrectAns);

        //Textview titles Labels:
        TextView textViewQuestionLabel = convertView.findViewById(R.id.textViewQuestionLabel);
        TextView textViewYourAnsLabel = convertView.findViewById(R.id.textViewYourAnsLabel);
        TextView textViewCorrectAnsLabel = convertView.findViewById(R.id.textViewCorrectAnsLabel);

        //TextView title settings/styling
        int questionNr = position +1;
        textViewQuestionLabel.setText("Question No."+ questionNr);
        textViewQuestionLabel.setTypeface(null, Typeface.BOLD);
        textViewYourAnsLabel.setTypeface(null, Typeface.BOLD);
        textViewCorrectAnsLabel.setTypeface(null, Typeface.BOLD);

        // Load information on each "container" with these 3 elements
        txtViewQuestion.setText(myQuestions.get(position).getQuestion());
        int answerID = correctAns.get(position).getAnswerNr();
        int myanswerID = QuizActivity.userAnswers.get(position);

    // Programmatic styling
    if (myanswerID != answerID) { // if its wrong answer then show user answer in bold red
        txtViewMyAns.setText(correctAns.get(position).getCorrectAnswerText(QuizActivity.userAnswers.get(position)));
        txtViewMyAns.setTextColor(Color.RED);
        txtViewMyAns.setTypeface(null, Typeface.BOLD);
        txtViewCorrectAns.setText(correctAns.get(position).getCorrectAnswerText(answerID));
        txtViewCorrectAns.setBackgroundColor(Color.YELLOW);
        txtViewMyAns.setBackgroundColor(Color.YELLOW);

    } else { // if its correct answer the show user answer in bold blue
        txtViewMyAns.setText(correctAns.get(position).getCorrectAnswerText(QuizActivity.userAnswers.get(position)));
        txtViewMyAns.setTextColor(Color.BLUE);
        txtViewMyAns.setTypeface(null, Typeface.BOLD);
        txtViewCorrectAns.setText(correctAns.get(position).getCorrectAnswerText(answerID));
    }
        return convertView;

    }
}
