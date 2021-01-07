package Defines;

import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;

public class QuestionFormData {
    private static Question question;
    private static String questionText;
    private static String questionSpeech;


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
        QuestionFormData.question = question;
        setQuestionType(question.getQuestionType());
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
}
