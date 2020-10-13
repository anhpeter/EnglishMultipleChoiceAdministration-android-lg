package Defines;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Question {
    private String question;
    private String correctAnswer;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String questionType; // text | picture
    private String level; // hard | medium | easy
    private long created;


    public Question(String question, String correctAnswer, String answerA, String answerB, String answerC, String answerD, String questionType, String level, long created) {
        this.setQuestion(question);
        this.setCorrectAnswer(correctAnswer);
        this.setAnswerA(answerA);
        this.setAnswerB(answerB);
        this.setAnswerC(answerC);
        this.setAnswerD(answerD);
        this.setQuestionType(questionType);
        this.setLevel(level);
        this.setCreated(created);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        if (created < 0 ) this.created = System.currentTimeMillis();
        else this.created = created;
    }

    public static Question getQuestionByQueryDoc(QueryDocumentSnapshot queryDocumentSnapshot){
        String question = queryDocumentSnapshot.getString("question");
        String correctAnswer = queryDocumentSnapshot.getString("correctAnswer");
        String answerA = queryDocumentSnapshot.getString("answerA");
        String answerB = queryDocumentSnapshot.getString("answerB");
        String answerC = queryDocumentSnapshot.getString("answerC");
        String answerD = queryDocumentSnapshot.getString("answerD");
        String questionType = queryDocumentSnapshot.getString("questionType");
        String level = queryDocumentSnapshot.getString("level");
        long created;
        try{
            created  = Integer.parseInt(queryDocumentSnapshot.getString("created"));
        }catch (Exception e){
            created = -1;
        }
        Question qt  = new Question(question, correctAnswer, answerA, answerB, answerC, answerD, questionType, level, created);
        return qt;
    }

    public HashMap<String, String> getDocData(){
        HashMap<String, String> docData = new HashMap<>();
        docData.put("question", getQuestion());
        docData.put("answerA", getAnswerA());
        docData.put("answerB", getAnswerB());
        docData.put("answerC", getAnswerC());
        docData.put("answerD", getAnswerD());
        docData.put("correctAnswer", getCorrectAnswer());
        docData.put("questionType", getQuestionType());
        docData.put("level", getLevel());
        return docData;
    }
}
