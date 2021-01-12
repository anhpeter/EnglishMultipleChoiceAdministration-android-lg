package Defines;

import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;

public class QuestionFormData {
    private static Question question;
    private static String questionText;
    private static String questionSpeech;
    private static int timeLimit = Question.defaultTimeLimit;
    private static String questionImageFilePath;
    private static Uri questionImageUri;
    private static MediaRecorder questionRecorder;
    private static String questionType = "text";

    public static void reset() {
        question = null;
        questionText = null;
        questionSpeech = null;
        questionImageUri = null;
        questionRecorder = null;
        questionImageFilePath = null;
        questionType = "text";
        timeLimit = Question.defaultTimeLimit;
    }

    public static void prepare() {
        question = null;
        questionText = null;
        questionSpeech = null;
        questionImageUri = null;
        questionRecorder = null;
        questionImageFilePath = null;
        timeLimit = Question.defaultTimeLimit;

    }

    // GETTER AND SETTER
    public static String getQuestionText() {
        return questionText;
    }

    public static void setQuestionText(String questionText) {
        QuestionFormData.questionText = questionText;
    }

    public static Uri getQuestionImageUri() {
        return questionImageUri;
    }

    public static void setQuestionImageUri(Uri questionImageUri) {
        QuestionFormData.questionImageUri = questionImageUri;
    }

    public static MediaRecorder getQuestionRecorder() {
        return questionRecorder;
    }

    public static void setQuestionRecorder(MediaRecorder questionRecorder) {
        QuestionFormData.questionRecorder = questionRecorder;
    }

    public static String getQuestionType() {
        return questionType;
    }

    public static void setQuestionType(String questionType) {
        QuestionFormData.questionType = questionType;
    }

    public static Question getQuestion() {
        return question;
    }

    public static void setQuestion(Question question) {
        setQuestionType(question.getQuestionType());
        setTimeLimit(question.getTimeLimit());
        switch (question.getQuestionType()) {
            case "text":
                setQuestionText(question.getQuestion());
                break;
            case "picture":
                setQuestionImageFilePath(question.getQuestion());
                break;
            case "audio":
                setQuestionSpeech(question.getQuestion());
                break;
        }
    }

    public static boolean isImageQuestion() {
        boolean result = false;
        if (questionType != null) {
            result = questionType.equals("picture");
        }
        return result;
    }

    public static boolean isAudioQuestion() {
        boolean result = false;
        if (questionType != null) {
            result = questionType.equals("audio");
        }
        return result;
    }

    public static String getQuestionImageFilePath() {
        return questionImageFilePath;
    }

    public static void setQuestionImageFilePath(String questionImageFilePath) {
        QuestionFormData.questionImageFilePath = questionImageFilePath;
    }

    public static String getQuestionSpeech() {
        return questionSpeech;
    }

    public static void setQuestionSpeech(String questionSpeech) {
        QuestionFormData.questionSpeech = questionSpeech;
    }

    public static int getTimeLimit() {
        return timeLimit;
    }

    public static void setTimeLimit(int timeLimit) {
        QuestionFormData.timeLimit = timeLimit;
    }

    public static boolean isQuestionValid() {
        boolean result = false;
        switch (getQuestionType()) {
            case "text":
                if (getQuestionText() != null) {
                    if (!getQuestionText().trim().equals("")) result = true;
                }
                break;
            case "audio":
                if (getQuestionSpeech() != null) {
                    if (!getQuestionSpeech().trim().equals("")) result = true;
                }
                break;
            case "picture":
                if (getQuestionImageUri() != null) result = true;
                break;
        }
        return result;
    }
}
