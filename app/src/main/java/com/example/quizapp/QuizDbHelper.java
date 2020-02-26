package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quizapp.QuizContract.QuestionsTable;

import java.util.ArrayList;
import java.util.List;



// class to deal with database, create tables, insert questions
public class QuizDbHelper extends SQLiteOpenHelper {

    // set database name
    private static final String DATABASE_NAME = "NetworkSecurityQuiz.db";
    private static final int DATABASE_VERSION = 1;

    // "db" variable will store the database object
    public static SQLiteDatabase db;

    // constructor
    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // copying db method parameter to local db
        this.db = db;

        // create table statement/query
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER," +
                QuestionsTable.COLUMN_COUNT_WRONG + " INTEGER," +
                QuestionsTable.COLUMN_COUNT_CORRECT + " INTEGER" +
                ")";

        // execute the query
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        // local method to populate the table
        fillQuestionsTable();

    }

    // populate table/database
    public static void fillQuestionsTable() {

        List<String[]> MyQuestions = MainActivity.MyQuestions;

        for(int i=0;i<MyQuestions.size();i++){
            String question = MyQuestions.get(i)[0];
            String option1 = MyQuestions.get(i)[1];
            String option2 = MyQuestions.get(i)[2];
            String option3 = MyQuestions.get(i)[3];
            String option4 = MyQuestions.get(i)[4];
            int answerNr = Integer.parseInt(MyQuestions.get(i)[5]);
            int wrongCounter = Integer.parseInt(MyQuestions.get(i)[6]);
            int correctCounter = Integer.parseInt(MyQuestions.get(i)[7]);

            // assemble the object
            Question q = new Question(question,option1,option2,option3,option4,answerNr,wrongCounter,correctCounter);

            // INSERT in database
            addQuestion(q);
        }

    }

    // Insert the questions in database
    public static void addQuestion(Question question) {

        ContentValues cv = new ContentValues();

        // create an object that keep questions into the correct column fields
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_COUNT_WRONG, question.getWrongCounter());
        cv.put(QuestionsTable.COLUMN_COUNT_CORRECT, question.getCorrectCounter());

        // insert questions in database
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    // Retrieve all questions with options, counters and ID
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setWrongCounter(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_COUNT_WRONG)));
                question.setCorrectCounter(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_COUNT_CORRECT)));
                question.qID = (c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    // Increment value of correct answer given from the user to each question in the database
    public static void sumCorrectTotal(Question question) {

        ContentValues cv = new ContentValues();

        // create an object that keep questions into the correct column fields
        cv.put(QuestionsTable.COLUMN_COUNT_CORRECT, question.getCorrectCounter());

        // UPDATE question in database
        db.update(QuestionsTable.TABLE_NAME, cv, "_id = " + question.qID, null);
    }

    // Increment value of wrong answer given from the user to each question in the database
    public static void sumWrongTotal(Question question) {

        ContentValues cv = new ContentValues();

        // create an object that keep questions into the correct column fields
        cv.put(QuestionsTable.COLUMN_COUNT_WRONG, question.getWrongCounter());

        // UPDATE question in database
        db.update(QuestionsTable.TABLE_NAME, cv, "_id = " + question.qID, null);
    }
}
