package com.example.quizapp;

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answerNr;
    private int correctCounter;
    private int wrongCounter;
    private static int questionID=1;
    public int qID;


    public Question() {}

    // Constructor
    public Question(String question, String option1, String option2, String option3, String option4, int answerNr,int wrongCounter,int correctCounter) {
        this.qID = getqID();
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerNr = answerNr;
        this.correctCounter = 0;
        this.wrongCounter = 0;
        sumID();
    }

    // ID getter
    public static int getqID(){return questionID;}

    // this was the way we found to set the ID for the objects
    static void sumID(){questionID++;}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public String getCorrectAnswerText(int answer){

        String ansText;

        switch (answer){
            case 1:
                ansText = this.getOption1();
                break;
            case 2:
                ansText = this.getOption2();
                break;
            case 3:
                ansText = this.getOption3();
                break;
            case 4:
                ansText = this.getOption4();
                break;
            default:
                ansText = "No answer selected!"; break;
        }

        return ansText;
    }

    public int getCorrectCounter() {
        return correctCounter;
    }

    public void setCorrectCounter(int count) {
        this.correctCounter = count;
    }

    public int getWrongCounter() {
        return wrongCounter;
    }

    public void setWrongCounter(int count) {
        this.wrongCounter = count;
    }

    // Counters

    public void sumWrongCounter() {
        this.wrongCounter++;
    }

    public void sumCorrectCounter() {
        this.correctCounter++;
    }

}
