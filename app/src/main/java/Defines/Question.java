package Defines;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Helpers.Helper;

public class Question {
    private static String[] levelArr = {"easy", "normal", "hard", "memes", "games"};
    private String id;
    private String question;
    private String correctAnswer;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String level; // hard | medium | easy

    // type
    private boolean isImageQuestion;
    private boolean isImageAnswer;
    private boolean isAudioQuestion;
    private boolean isVoiceAnswer;
    //
    private long created;
    private long lastInteracted;


    public Question(String id, String question, String correctAnswer, String answerA, String answerB, String answerC, String answerD, String level, long created, long lastInteracted, boolean isImageQuestion, boolean isAudioQuestion, boolean isVoiceAnswer, boolean isImageAnswer) {
        this.setId(id);
        this.setQuestion(question);
        this.setAnswerA(answerA);
        this.setAnswerB(answerB);
        this.setAnswerC(answerC);
        this.setAnswerD(answerD);
        this.setCorrectAnswer(correctAnswer);
        this.setLevel(level);
        this.setCreated(created);
        this.setLastInteracted(lastInteracted);
        this.setIsImageQuestion(isImageQuestion);
        this.setImageAnswer(isImageAnswer);
        this.setVoiceAnswer(isVoiceAnswer);
        this.setIsAudioQuestion(isAudioQuestion);
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
        if (created <= 0) {
            this.created = (this.created > 0) ? this.created : Helper.getTime();
            this.setLastInteracted(this.created);
        } else this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // CREATE QUESTION BY FIREBASE DATA SNAPSHOT
    public static Question getQuestionByDataSnapshot(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            try {

                String id = dataSnapshot.getKey();
                String question = dataSnapshot.child("Question").getValue().toString();
                String correctAnswer = dataSnapshot.child("CorrectAnswer").getValue().toString();
                String answerA = dataSnapshot.child("A").getValue().toString();
                String answerB = dataSnapshot.child("B").getValue().toString();
                String answerC = dataSnapshot.child("C").getValue().toString();
                String answerD = dataSnapshot.child("D").getValue().toString();
                boolean isImageAnswer = Helper.getBooleanByDataSnapshot(dataSnapshot, "IsImageAnswer", false);
                boolean isImageQuestion = Helper.getBooleanByDataSnapshot(dataSnapshot, "IsImageQuestion", false);
                boolean isVoiceAnswer = Helper.getBooleanByDataSnapshot(dataSnapshot, "IsVoiceAnswer", false);
                boolean isAudioQuestion = Helper.getBooleanByDataSnapshot(dataSnapshot, "IsAudioQuestion", false);
                String categoryId = dataSnapshot.child("CategoryId").getValue().toString();
                String level = getLevelByCategoryId(categoryId);
                long created = Long.parseLong(Helper.getStringByDataSnapshot(dataSnapshot, "Created", "-1"));
                long lastInteracted = Long.parseLong(Helper.getStringByDataSnapshot(dataSnapshot, "LastInteracted", "-1"));
                Question qt = new Question(id, question, correctAnswer, answerA, answerB, answerC, answerD, level, created, lastInteracted, isImageQuestion, isAudioQuestion, isVoiceAnswer, isImageAnswer);
                return qt;
            } catch (Exception e) {
                String id = dataSnapshot.getKey();
                Log.d("xxx", "doc err: " + id);
            }
        }
        return null;
    }

    // GET DOC FOR SAVING
    public HashMap<String, Object> getDocData() {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("Question", getQuestion());
        docData.put("A", getAnswerA());
        docData.put("B", getAnswerB());
        docData.put("C", getAnswerC());
        docData.put("D", getAnswerD());
        docData.put("CorrectAnswer", getCorrectAnswer());
        docData.put("CategoryId", Question.getCategoryIdByLevel(getLevel()));
        docData.put("Created", getCreated() + "");
        docData.put("LastInteracted", getLastInteracted() + "");
        docData.put("IsImageQuestion", getIsImageQuestion() + "");
        docData.put("IsAudioQuestion", getIsAudioQuestion() + "");
        docData.put("IsImageAnswer", getIsImageAnswer() + "");
        docData.put("IsVoiceAnswer", getIsVoiceAnswer() + "");
        showInfo();
        return docData;
    }

    public String getCorrectAnswerInLetter() {
        String letter = null;
        if (getAnswerA().equals(getCorrectAnswer())) letter = "A";
        else if (getAnswerB().equals(getCorrectAnswer())) letter = "B";
        else if (getAnswerC().equals(getCorrectAnswer())) letter = "C";
        else if (getAnswerD().equals(getCorrectAnswer())) letter = "D";
        return letter;
    }

    public boolean getIsImageQuestion() {
        return isImageQuestion;
    }

    public long getLastInteracted() {
        return lastInteracted;
    }

    public void setIsImageQuestion(boolean value) {
        isImageQuestion = value;
    }


    // LEVEL & CATEGORY
    public static String[] getLevelArr() {
        return levelArr;
    }

    public static String[] getSpinnerLevelArr() {
        String levelArr[] = getLevelArr();
        String spinnerLevelArr[] = new String[levelArr.length];
        for (int i = 0; i < levelArr.length; i++) {
            spinnerLevelArr[i] = Helper.ucFirst(levelArr[i]);
        }
        return spinnerLevelArr;
    }

    public static String getLevelByCategoryId(String id) {
        int numberId = Integer.parseInt(id) - 1;
        String level = Question.getLevelByIndex(numberId);
        return level;
    }

    public static String getLevelByIndex(int i) {
        String levelArr[] = getLevelArr();
        return levelArr[i];
    }

    public static int getLevelIndexByLevel(String level) {
        String levelArr[] = getLevelArr();
        for (int i = 0; i < levelArr.length; i++) {
            String lv = levelArr[i].toLowerCase();
            String currentLevel = level.toLowerCase();
            if (currentLevel.equals(lv)) return i;
        }
        return 0;
    }

    public static String getCategoryIdByLevel(String level) {
        int levelIndex = getLevelIndexByLevel(level);
        return "0" + (levelIndex + 1);
    }

    public void setLastInteracted(long value) {
        lastInteracted = value;
    }

    // SUPPORTED METHODS
    public void generateCorrectAnswerByLetter(String correctAnswerInLetter) {
        if (correctAnswerInLetter.equals("A")) this.setCorrectAnswer(getAnswerA());
        if (correctAnswerInLetter.equals("B")) this.setCorrectAnswer(getAnswerB());
        if (correctAnswerInLetter.equals("C")) this.setCorrectAnswer(getAnswerC());
        if (correctAnswerInLetter.equals("D")) this.setCorrectAnswer(getAnswerD());

    }


    public void setImageAnswer(boolean imageAnswer) {
        isImageAnswer = imageAnswer;
    }

    public boolean getIsAudioQuestion() {
        return isAudioQuestion || false;
    }

    public void setIsAudioQuestion(boolean value) {
        isAudioQuestion = value;
    }

    public boolean getIsVoiceAnswer() {
        return isVoiceAnswer || false;
    }

    public boolean getIsImageAnswer() {
        return isImageAnswer || false;
    }

    public void setVoiceAnswer(boolean voiceAnswer) {
        isVoiceAnswer = voiceAnswer;
    }

    public String getAnswerType() {
        String result;
        if (getIsImageAnswer()) result = "picture";
        else if (getIsVoiceAnswer()) result = "voice";
        else result = "text";
        return result;
    }

    public String getQuestionType() {
        String result;
        if (getIsImageQuestion()) result = "picture";
        else if (getIsAudioQuestion()) result = "audio";
        else result = "text";
        return result;
    }

    public void setQuestionType(String value) {
        setIsAudioQuestion(value.equals("audio"));
        setIsImageQuestion(value.equals("picture"));
    }

    public void showInfo() {
        Log.d("ppp", "image answer:" + getIsImageAnswer() + ";voice answer:" + getIsVoiceAnswer() + ";answer type:" + getAnswerType());
    }

    public String getQuestionImageFilePath() {
        String result = null;
        if (getIsImageQuestion()) {
            if (getQuestion() != null) result = getQuestion();
        }
        return result;
    }


}
