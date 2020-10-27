package Defines;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Question {

    private String id;

    private String question;
    private String correctAnswer;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String questionType; // text | picture
    private String level; // hard | medium | easy
    private long created;


    public Question(String id, String question, String correctAnswer, String answerA, String answerB, String answerC, String answerD, String questionType, String level, long created) {
        this.setId(id);
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
        if (created <= 0 ){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp);
            this.created = (this.created > 0) ? this.created : timestamp.getTime();
        }
        else this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static Question getQuestionBySnapshot(DocumentSnapshot queryDocumentSnapshot){
        if (queryDocumentSnapshot != null){
            String id = queryDocumentSnapshot.getId();
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
            Question qt  = new Question(id, question, correctAnswer, answerA, answerB, answerC, answerD, questionType, level, created);
            return qt;
        }else return null;
    }

    public HashMap<String, Object> getDocData(){
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("question", getQuestion());
        docData.put("answerA", getAnswerA());
        docData.put("answerB", getAnswerB());
        docData.put("answerC", getAnswerC());
        docData.put("answerD", getAnswerD());
        docData.put("correctAnswer", getCorrectAnswer());
        docData.put("questionType", getQuestionType());
        docData.put("level", getLevel());
        docData.put("created", getCreated()+"");
        return docData;
    }

    public String getInfo(){
        return getId()+ ", "+getQuestion()+", "+getQuestionType()+", "+getLevel()+", "+getCorrectAnswer()+", "+getAnswerA()+", "+getAnswerB()+", "+getAnswerC()+", "+getAnswerD()+", "+getCreated();
    }

    public boolean getIsImageQuestion(){
        return false;
    }
}
