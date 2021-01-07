package Helpers;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.util.Log;

public class QuestionPictureManager {
    private String questionPath;
    private String answerAPath;
    private String answerBPath;
    private String answerCPath;
    private String answerDPath;

    private Uri questionUri;
    private Uri answerAUri;
    private Uri answerBUri;
    private Uri answerCUri;
    private Uri answerDUri;

    // CONSTRUCTOR
    public QuestionPictureManager(String questionPath, String answerAPath, String answerBPath, String answerCPath, String answerDPath) {
        updateParams(questionPath, answerAPath, answerBPath, answerCPath, answerDPath);
    }

    public void updateParams(String questionPath, String answerAPath, String answerBPath, String answerCPath, String answerDPath) {
        reset();
        this.questionPath = questionPath;
        this.answerAPath = answerAPath;
        this.answerBPath = answerBPath;
        this.answerCPath = answerCPath;
        this.answerDPath = answerDPath;
    }

    public void reset() {
        setAnswerAPath(null);
        setAnswerBPath(null);
        setAnswerCPath(null);
        setAnswerDPath(null);
        setAnswerAUri(null);
        setAnswerBUri(null);
        setAnswerCUri(null);
        setAnswerDUri(null);
    }

    public void setUriByIndex(int index, Uri uri) {
        switch (index) {
            case 0:
                setAnswerAUri(uri);
                break;
            case 1:
                setAnswerBUri(uri);
                break;
            case 2:
                setAnswerCUri(uri);
                break;
            case 3:
                setAnswerDUri(uri);
                break;
            case 4:
                setQuestionUri(uri);
                break;
        }
    }

    public void setPathByIndex(int index, String path) {
        switch (index) {
            case 0:
                setAnswerAPath(path);
                break;
            case 1:
                setAnswerBPath(path);
                break;
            case 2:
                setAnswerCPath(path);
                break;
            case 3:
                setAnswerDPath(path);
                break;
            case 4:
                setQuestionPath(path);
                break;
        }
    }

    // GETTER AND SETTER
    public String getQuestionPath() {
        return questionPath;
    }

    public void setQuestionPath(String questionPath) {
        this.questionPath = questionPath;
    }

    public String getAnswerAPath() {
        return answerAPath;
    }

    public void setAnswerAPath(String answerAPath) {
        this.answerAPath = answerAPath;
    }

    public String getAnswerBPath() {
        return answerBPath;
    }

    public void setAnswerBPath(String answerBPath) {
        this.answerBPath = answerBPath;
    }

    public String getAnswerCPath() {
        return answerCPath;
    }

    public void setAnswerCPath(String answerCPath) {
        this.answerCPath = answerCPath;
    }

    public String getAnswerDPath() {
        return answerDPath;
    }

    public void setAnswerDPath(String answerDPath) {
        this.answerDPath = answerDPath;
    }

    public Uri getQuestionUri() {
        return questionUri;
    }

    public void setQuestionUri(Uri questionUri) {
        this.questionUri = questionUri;
    }

    public Uri getAnswerAUri() {
        return answerAUri;
    }

    public void setAnswerAUri(Uri answerAUri) {
        this.answerAUri = answerAUri;
    }

    public Uri getAnswerBUri() {
        return answerBUri;
    }

    public void setAnswerBUri(Uri answerBUri) {
        this.answerBUri = answerBUri;
    }

    public Uri getAnswerCUri() {
        return answerCUri;
    }

    public void setAnswerCUri(Uri answerCUri) {
        this.answerCUri = answerCUri;
    }

    public Uri getAnswerDUri() {
        return answerDUri;
    }

    public void setAnswerDUri(Uri answerDUri) {
        this.answerDUri = answerDUri;
    }

    public boolean isAnswerUriValid() {
        if (
                (getAnswerAUri() != null || getAnswerAPath() != null) &&
                        (getAnswerBUri() != null || getAnswerBPath() != null) &&
                        (getAnswerCUri() != null || getAnswerCPath() != null) &&
                        (getAnswerDUri() != null || getAnswerDPath() != null)
        ) return true;
        return false;
    }

    public int getTotalUpload() {
        int count = 0;
        if (getAnswerAUri() != null) count++;
        if (getAnswerBUri() != null) count++;
        if (getAnswerCUri() != null) count++;
        if (getAnswerDUri() != null) count++;
        if (getQuestionUri() != null) count++;
        return count;
    }

    // UPLOAD & DELETE
    public void uploadAll(Fragment fragment) {
        uploadAnswerImage(fragment);
        uploadQuestionImage(fragment);
    }

    public void uploadAnswerImage(Fragment fragment) {
        MyStorage myStorage = MyStorage.getInstance();
        deleteOldAnswerFile();
        myStorage.uploadImage(getAnswerAUri(), 0, fragment);
        myStorage.uploadImage(getAnswerBUri(), 1, fragment);
        myStorage.uploadImage(getAnswerCUri(), 2, fragment);
        myStorage.uploadImage(getAnswerDUri(), 3, fragment);
    }

    public void uploadQuestionImage(Fragment fragment) {
        MyStorage myStorage = MyStorage.getInstance();
        deleteOldQuestionFile();
        myStorage.uploadImage(getQuestionUri(), 4, fragment);
    }

    private void deleteOldAnswerFile() {
        MyStorage myStorage = MyStorage.getInstance();
        if (getAnswerAPath() != null && getAnswerAUri() != null) myStorage.delete(getAnswerAPath());
        if (getAnswerBPath() != null && getAnswerBUri() != null) myStorage.delete(getAnswerBPath());
        if (getAnswerCPath() != null && getAnswerCUri() != null) myStorage.delete(getAnswerCPath());
        if (getAnswerDPath() != null && getAnswerDUri() != null) myStorage.delete(getAnswerDPath());
    }

    private void deleteOldQuestionFile() {
        if (getQuestionUri() != null)
            deleteQuestionIfExist();
    }

    public void deleteQuestionIfExist() {
        MyStorage myStorage = MyStorage.getInstance();
        if (getQuestionPath() != null){
            myStorage.delete(getQuestionPath());
        }
    }

    public void deleteAllAnswerIfExist() {
        MyStorage myStorage = MyStorage.getInstance();
        if (getAnswerAPath() != null) myStorage.delete(getAnswerAPath());
        if (getAnswerBPath() != null) myStorage.delete(getAnswerBPath());
        if (getAnswerCPath() != null) myStorage.delete(getAnswerCPath());
        if (getAnswerDPath() != null) myStorage.delete(getAnswerDPath());
    }

    public void deleteAllIfExist() {
        deleteAllAnswerIfExist();
    }
}
