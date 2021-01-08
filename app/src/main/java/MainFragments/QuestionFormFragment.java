package MainFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.multiple_choice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import Interfaces.FragmentCommunicate;
import Interfaces.ICallback;
import Interfaces.IMyStorage;
import Defines.IntentCode;
import Defines.Question;
import Defines.QuestionFormData;
import Helpers.Helper;
import Helpers.QuestionPictureManager;
import Models.QuestionModel;

public class QuestionFormFragment extends MyFragment implements ICallback<Question>, IMyStorage {

    String fragmentName = "question-form";
    FragmentCommunicate fragmentCommunicate;
    QuestionModel questionModel;
    TextToSpeech tts;
    View v;

    // PARAMS
    String formType;
    Question item;
    HashMap<String, String> params;

    // WIDGET
    RadioGroup radioGroupAnswerType, radioGroupCorrectAnswer;
    EditText edtAnswerA, edtAnswerB, edtAnswerC, edtAnswerD;
    TextView txtUploadProgress, txtQuestion, txtSec;
    RadioButton radioPictureA, radioPictureB, radioPictureC, radioPictureD;
    ImageView pictureA, pictureB, pictureC, pictureD, pictureQuestion, imgSpeaker;
    RelativeLayout rltSec, rltTextAnswer, rltPictureAnswer, rltUploadProgress, rltTxtQuestion, rltAnswer;
    Button btnSubmit;
    ProgressBar progressUpload;
    private boolean isPickingQuestionPicture = false;
    private boolean isSaving = false;

    // VALUE ARRAY
    String levelValues[] = {"hard", "medium", "easy"};
    String typeValues[] = {"text", "picture", "voice"};
    String correctAnswerValues[] = {"A", "B", "C", "D"};
    String imagePaths[] = {"", "", "", ""};

    // WIDGET ARRAYS
    RadioButton radioPictures[];
    ImageView pictures[];

    // REQUEST CODE

    // OTHER
    int totalUpload;
    double progressArr[];
    int progress;
    ImageView interactingPicture;
    QuestionPictureManager questionPictureManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_question_form, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        initTextToSpeech();
        return v;
    }

    private void onInit() {
        setDefaultPictureAnswerParams();
        questionModel = new QuestionModel(getActivity(), this);
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        radioPictures = new RadioButton[]{radioPictureA, radioPictureB, radioPictureC, radioPictureD};
        pictures = new ImageView[]{pictureA, pictureB, pictureC, pictureD};
        radioPictureA.setChecked(true);
        updateSecText(Question.defaultTimeLimit);
        setArguments();
        initEvents();
        Helper.hideKeyboard(getCalledActivity());
    }

    private void initEvents() {
        initTxtQuestionClick();
        initSecClick();
        initPictureQuestionClick();
        initPictureRadiosChecked();
        initPictureClicked();
        initAnswerTypeRadiosChecked();
        initSpeakerClick();
        initSubmitClick();
    }

    private void initSecClick() {
        rltSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSecDialog();
            }
        });
    }

    private void initSpeakerClick() {
        imgSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = txtQuestion.getText().toString();
                if (speech.trim() != "") {
                    int speechStatus = tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("xxx", "Error in converting Text to Speech!");
                    }
                }
            }
        });
    }

    private void initTxtQuestionClick() {
        txtQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddQuestionDialog();
            }
        });
    }

    private void initPictureQuestionClick() {
        pictureQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddQuestionDialog();
            }
        });
    }

    private void initPictureClicked() {
        for (int i = 0; i < pictures.length; i++) {
            pictures[i].setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void onClick(View v) {
                    isPickingQuestionPicture = false;
                    interactingPicture = ((ImageView) v);
                    requestPermissionForReadExternalStorage(IntentCode.PICK_ANSWER_PICTURE_CODE);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissionForReadExternalStorage(int code) {
        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                code);
    }

    private int getAnswerPosByImageViewId(int id) {
        int no = 0;
        for (int i = 0; i < pictures.length; i++) {
            if (pictures[i].getId() == id) {
                no = i;
                break;
            }
        }
        return no;
    }

    private void initPictureRadiosChecked() {
        for (int i = 0; i < radioPictures.length; i++) {
            radioPictures[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int i = 0; i < radioPictures.length; i++) {
                            RadioButton radioButton = radioPictures[i];
                            if (buttonView.getId() != radioButton.getId()) {
                                radioButton.setChecked(false);
                            }
                        }
                    }
                }
            });
        }
    }

    private void initAnswerTypeRadiosChecked() {
        radioGroupAnswerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String answerType = null;
                switch (checkedId) {
                    case R.id.radioButtonText:
                        answerType = "text";
                        break;
                    case R.id.radioButtonPicture:
                        answerType = "picture";
                        break;
                    case R.id.radioButtonVoice:
                        answerType = "voice";
                        break;
                }
                solveAnswerVisibility(answerType);
            }
        });
    }

    private void solveAnswerVisibility(String answerType) {
        if (answerType.equals("voice")) {
            rltTextAnswer.setVisibility(View.GONE);
            rltPictureAnswer.setVisibility(View.GONE);
        } else if (answerType.equals("picture")) {
            rltTextAnswer.setVisibility(View.GONE);
            rltPictureAnswer.setVisibility(View.VISIBLE);
        } else if (answerType.equals("text")) {
            rltPictureAnswer.setVisibility(View.GONE);
            rltTextAnswer.setVisibility(View.VISIBLE);
        }

    }

    // ON SUBMIT
    private void initSubmitClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSaving) {
                    isSaving = true;
                    String answerTypeSelected = getAnswerTypeSelected();
                    if (answerTypeSelected == "text") {
                        onSaveTextAnswer();
                    } else if (answerTypeSelected == "voice") {
                        onSaveTextAnswer();
                    } else if (answerTypeSelected == "picture") {
                        onSavePictureAnswer();
                    }
                }
            }
        });
    }

    private String getAnswerTypeSelected() {
        String result = "";
        int typeRadioButtonId = radioGroupAnswerType.getCheckedRadioButtonId();
        switch (typeRadioButtonId) {
            case R.id.radioButtonText:
                result = "text";
                break;
            case R.id.radioButtonPicture:
                result = "picture";
                break;
            case R.id.radioButtonVoice:
                result = "voice";
                break;
        }
        return result;
    }

    // ON SAVE
    private void onSaveTextAnswer() {
        HashMap<String, String> saveParams = getSaveParams();
        if (isVoiceAnswerSelected(saveParams)) {
            onReallySaveTextAnswer(saveParams);
        } else {
            if (isTextQuestionOptionValid()) {
                onReallySaveTextAnswer(saveParams);
            } else {
                showInValidMessage();
            }

        }
    }

    private void onReallySaveTextAnswer(HashMap<String, String> saveParams) {
        switch (QuestionFormData.getQuestionType()) {
            case "audio":
            case "text":
                questionPictureManager.deleteQuestionIfExist();
                saveTextAnswer(saveParams);
                break;
            case "picture":
                if (questionPictureManager.getQuestionUri() != null) {
                    totalUpload = 1;
                    rltUploadProgress.setVisibility(View.VISIBLE);
                    questionPictureManager.uploadQuestionImage(this);
                } else {
                    saveTextAnswer(saveParams);
                }
                break;
        }
    }

    private void saveTextAnswer(HashMap<String, String> saveParams) {
        questionPictureManager.deleteAllAnswerIfExist();
        String answerA, answerB, answerC, answerD;
        if (isVoiceAnswerSelected(saveParams)) {
            answerA = answerB = answerC = answerD = "";
        } else {
            answerA = edtAnswerA.getText().toString().trim();
            answerB = edtAnswerB.getText().toString().trim();
            answerC = edtAnswerC.getText().toString().trim();
            answerD = edtAnswerD.getText().toString().trim();
        }
        String questionContent = getQuestionContent();
        if (formType.equals("edit")) {
            item.setQuestion(questionContent);
            item.setAnswerA(answerA);
            item.setAnswerB(answerB);
            item.setAnswerC(answerC);
            item.setAnswerD(answerD);
            item.setLevel(saveParams.get("level"));
            item.setLastInteracted(Helper.getTime());
            item.generateCorrectAnswerByLetter(saveParams.get("correctAnswerInLetter"));
            item.setVoiceAnswer(isVoiceAnswerSelected(saveParams));
            item.setImageAnswer(false);
            item.setQuestionType(QuestionFormData.getQuestionType());
            item.setTimeLimit(QuestionFormData.getTimeLimit());
            solveEdit(item);
        } else if (formType.equals("add")) {
            Question questionObj = new Question(null, questionContent, null,
                    answerA, answerB, answerC, answerD, saveParams.get("level"),
                    Helper.getTime(), Helper.getTime(), QuestionFormData.isImageQuestion(), QuestionFormData.isAudioQuestion(), isVoiceAnswerSelected(saveParams), false, QuestionFormData.getTimeLimit());
            questionObj.generateCorrectAnswerByLetter(saveParams.get("correctAnswerInLetter"));
            solveAdd(questionObj);
        }
    }

    private boolean isVoiceAnswerSelected(HashMap<String, String> saveParams) {
        return saveParams.get("answerType").equals("voice");
    }

    private void onSavePictureAnswer() {
        if (isOtherOptionValid() && questionPictureManager.isAnswerUriValid()) {
            totalUpload = questionPictureManager.getTotalUpload();
            if (totalUpload > 0) {
                rltUploadProgress.setVisibility(View.VISIBLE);
                questionPictureManager.uploadAll(QuestionFormFragment.this);
            } else {
                savePictureAnswer();
            }
        } else {
            showInValidMessage();
        }
    }

    // ON DELETE
    public void onDelete() {
        questionPictureManager.deleteAllIfExist();
        questionModel.deleteItemById(item.getId(), "delete-item");
    }

    //
    private void solveEdit(Question question) {
        questionModel.updateItem(question, "update-item");
    }

    private void solveAdd(Question question) {
        questionModel.addItem(question, "add-item");
    }

    private boolean isTextQuestionOptionValid() {
        String answerA = edtAnswerA.getText().toString().trim();
        String answerB = edtAnswerB.getText().toString().trim();
        String answerC = edtAnswerC.getText().toString().trim();
        String answerD = edtAnswerD.getText().toString().trim();
        if (
                isOtherOptionValid() &&
                        !answerA.trim().isEmpty() &&
                        !answerB.trim().isEmpty() &&
                        !answerC.trim().isEmpty() &&
                        !answerD.trim().isEmpty()
        ) return true;
        return false;
    }

    private boolean isOtherOptionValid() {
        HashMap<String, String> saveParams = getSaveParams();
        if (
                !saveParams.get("answerType").isEmpty() &&
                        !saveParams.get("level").isEmpty() &&
                        !saveParams.get("correctAnswerInLetter").trim().isEmpty()
        ) return true;
        return false;
    }

    private String getQuestionLevelValue() {
        String result = getCalledActivity().getQuestionLevel();
        return result;
    }

    private String getCorrectAnswerValue() {
        String result = "";
        if (getAnswerTypeSelected().equals("text")) {
            int typeRadioButtonId = radioGroupCorrectAnswer.getCheckedRadioButtonId();
            switch (typeRadioButtonId) {
                case R.id.radioA:
                    result = "A";
                    break;
                case R.id.radioB:
                    result = "B";
                    break;
                case R.id.radioC:
                    result = "C";
                    break;
                case R.id.radioD:
                    result = "D";
                    break;
            }
        } else {
            if (radioPictureA.isChecked()) result = "A";
            else if (radioPictureB.isChecked()) result = "B";
            else if (radioPictureC.isChecked()) result = "C";
            else if (radioPictureD.isChecked()) result = "D";
        }
        return result;
    }

    private void setArguments() {
        Bundle b = getArguments();
        // form params
        params = (HashMap<String, String>) b.getSerializable("params");
        String id = params.get("id");
        formType = params.get("formType");
        if (formType.equals("edit") && id != null) {
            rltAnswer.setVisibility(View.GONE);
            getItemById(id);
        } else initQuestionPictureManager();
    }

    private void getItemById(String id) {
        questionModel.getItemById(id, "get-edit-item");
    }

    private int getLevelPos(String level) {
        return java.util.Arrays.asList(levelValues).indexOf(level);
    }

    private int getTypePos(String type) {
        return java.util.Arrays.asList(typeValues).indexOf(type);
    }

    private int getCorrectAnswerPos(String answer) {
        int pos = 0;
        String answerArr[] = {item.getAnswerA(), item.getAnswerB(), item.getAnswerC(), item.getAnswerD()};
        pos = java.util.Arrays.asList(answerArr).indexOf(answer);
        return pos;
    }

    // MAPPING
    private void mapping() {
        radioGroupAnswerType = (RadioGroup) v.findViewById(R.id.radioGroupType);
        radioGroupCorrectAnswer = (RadioGroup) v.findViewById(R.id.radioGroupCorrectAnswer);
        txtSec = (TextView) v.findViewById(R.id.txtSec);
        txtQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        edtAnswerA = (EditText) v.findViewById(R.id.edtAnswerA);
        edtAnswerB = (EditText) v.findViewById(R.id.edtAnswerB);
        edtAnswerC = (EditText) v.findViewById(R.id.edtAnswerC);
        edtAnswerD = (EditText) v.findViewById(R.id.edtAnswerD);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        txtUploadProgress = (TextView) v.findViewById(R.id.txtUploadProgress);


        radioPictureA = (RadioButton) v.findViewById(R.id.radioPictureA);
        radioPictureB = (RadioButton) v.findViewById(R.id.radioPictureB);
        radioPictureC = (RadioButton) v.findViewById(R.id.radioPictureC);
        radioPictureD = (RadioButton) v.findViewById(R.id.radioPictureD);

        imgSpeaker = (ImageView) v.findViewById(R.id.imgSpeaker);
        pictureA = (ImageView) v.findViewById(R.id.pictureA);
        pictureB = (ImageView) v.findViewById(R.id.pictureB);
        pictureC = (ImageView) v.findViewById(R.id.pictureC);
        pictureD = (ImageView) v.findViewById(R.id.pictureD);
        pictureQuestion = (ImageView) v.findViewById(R.id.imgQuestion);

        rltAnswer = (RelativeLayout) v.findViewById(R.id.rltAnswer);
        rltTextAnswer = (RelativeLayout) v.findViewById(R.id.rltTextAnswer);
        rltTxtQuestion = (RelativeLayout) v.findViewById(R.id.rltTxtQuestion);
        rltSec = (RelativeLayout) v.findViewById(R.id.rltSec);
        rltPictureAnswer = (RelativeLayout) v.findViewById(R.id.rltPictureAnswer);
        rltUploadProgress = (RelativeLayout) v.findViewById(R.id.rltProgressUpload);
        progressUpload = (ProgressBar) v.findViewById(R.id.progressUpload);
    }

    // PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (isPickPictureCode(
                requestCode) &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, IntentCode.PICK_ANSWER_PICTURE_CODE);
            Log.d("xxx", "start activity:" + requestCode);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPickPictureCode(int code) {
        return (code == IntentCode.PICK_ANSWER_PICTURE_CODE || code == IntentCode.PICTURE_QUESTION_PICTURE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("xxx", "request code: " + requestCode);
        if (
                isPickPictureCode(requestCode) &&
                        resultCode == getActivity().RESULT_OK &&
                        data != null) {
            Uri filePath = data.getData();
            QuestionFormData.setQuestionImageUri(filePath);

            if (!isPickingQuestionPicture) {
                questionPictureManager.setUriByIndex(getAnswerPosByImageViewId(interactingPicture.getId()), filePath);
            } else {
                questionPictureManager.setQuestionUri(filePath);
            }
            Helper.setImageViewImageByUri(getActivity(), interactingPicture, filePath);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // STORAGE CALLBACK
    @Override
    public void uploadedCallback(String url, int code) {
        Log.d("xxx", "uploaded: " + url);
        questionPictureManager.setPathByIndex(code, url);
    }

    @Override
    public void progressCallback(double value, int code) {
        progressArr[code] = value;
        double totalProgress = 0;
        for (int i = 0; i < 5; i++) totalProgress += progressArr[i];
        progress = (int) (((totalProgress / 100) / totalUpload) * 100);
        progressUpload.setProgress(progress);
        txtUploadProgress.setText(progress + "%");
        if (progress == 100) {

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            switch (getAnswerTypeSelected()) {
                                case "text":
                                    saveTextAnswer(getSaveParams());
                                    break;
                                case "picture":
                                    savePictureAnswer();
                                    break;
                            }
                        }
                    }, 500);

        }
    }

    public void savePictureAnswer() {
        HashMap<String, String> saveParams = getSaveParams();
        String questionContent = getQuestionContent();
        if (formType.equals("edit")) {
            item.setQuestion(questionContent);
            item.setAnswerA(questionPictureManager.getAnswerAPath());
            item.setAnswerB(questionPictureManager.getAnswerBPath());
            item.setAnswerC(questionPictureManager.getAnswerCPath());
            item.setAnswerD(questionPictureManager.getAnswerDPath());
            item.setLevel(saveParams.get("level"));
            item.setLastInteracted(Helper.getTime());
            item.generateCorrectAnswerByLetter(saveParams.get("correctAnswerInLetter"));
            item.setVoiceAnswer(false);
            item.setImageAnswer(true);
            item.setQuestionType(QuestionFormData.getQuestionType());
            item.setTimeLimit(QuestionFormData.getTimeLimit());
            solveEdit(item);
        } else if (formType.equals("add")) {
            Question questionObj = new Question(saveParams.get("id"), questionContent, saveParams.get("correctAnswerInLetter"),
                    questionPictureManager.getAnswerAPath(),
                    questionPictureManager.getAnswerBPath(), questionPictureManager.getAnswerCPath(), questionPictureManager.getAnswerDPath(),
                    saveParams.get("level"), Helper.getTime(), Helper.getTime(), QuestionFormData.isImageQuestion(), QuestionFormData.isAudioQuestion(), false, true, QuestionFormData.getTimeLimit());
            questionObj.generateCorrectAnswerByLetter(saveParams.get("correctAnswerInLetter"));
            solveAdd(questionObj);
        }
    }

    private String getQuestionContent() {
        String result = null;
        switch (QuestionFormData.getQuestionType()) {
            case "audio":
            case "text":
                result = txtQuestion.getText().toString();
                break;
            case "picture":
                result = questionPictureManager.getQuestionPath();
                break;
        }
        return result;
    }

    @Override
    public void deletedCallback(int code) {

    }

    // DATABASE CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {
        if (tag.equals("get-edit-item")) onGetEditItemCallBack(item);
        if (tag.equals("update-item")) onUpdateItemCallBack(item);
        if (tag.equals("add-item")) onAddItemCallBack(item);
        if (tag.equals("delete-item")) onDeleteItemCallback();
    }

    private void onGetEditItemCallBack(Question item) {
        this.item = item;
        QuestionFormData.setQuestion(item);
        ((RadioButton) radioGroupAnswerType.getChildAt(getTypePos(item.getAnswerType()))).setChecked(true);
        Helper.hideKeyboard(getCalledActivity());
        solveQuestionVisibility(item.getQuestionType());
        solveAnswerVisibility(item.getAnswerType());

        // SET TIME LIMIT
        updateSecText(QuestionFormData.getTimeLimit());
        Log.d("xxx", "sec: " + item.getTimeLimit());

        // QUESTION VIEW
        switch (item.getQuestionType()) {
            case "text":
                setTextQuestionView();
                break;
            case "picture":
                setPictureQuestionView();
                break;
            case "audio":
                setAudioQuestionView();
                break;
        }

        // ANSWER VIEW
        switch (item.getAnswerType()) {
            case "text":
                setTextAnswerView();
                break;
            case "picture":
                setPictureAnswerView();
                break;
            case "voice":
                setVoiceAnswerView();
                break;
        }
        rltAnswer.setVisibility(View.VISIBLE);
        initQuestionPictureManager();
    }

    // SET ANSWER VIEW
    private void setAudioQuestionView() {
        txtQuestion.setText(item.getQuestion());
    }

    private void setPictureQuestionView() {
        Picasso.get().load(item.getQuestion()).into(pictureQuestion);
    }

    private void setTextQuestionView() {
        txtQuestion.setText(item.getQuestion());
    }


    // SET ANSWER VIEW
    private void setVoiceAnswerView() {
    }

    private void setTextAnswerView() {
        edtAnswerA.setText(item.getAnswerA());
        edtAnswerB.setText(item.getAnswerB());
        edtAnswerC.setText(item.getAnswerC());
        edtAnswerD.setText(item.getAnswerD());
        ((RadioButton) radioGroupCorrectAnswer.getChildAt(getCorrectAnswerPos(item.getCorrectAnswer()))).setChecked(true);
    }

    private void setPictureAnswerView() {
        Picasso.get().load(item.getAnswerA()).into(pictureA);
        Picasso.get().load(item.getAnswerB()).into(pictureB);
        Picasso.get().load(item.getAnswerC()).into(pictureC);
        Picasso.get().load(item.getAnswerD()).into(pictureD);
        try {
            ((RadioButton) radioPictures[getCorrectAnswerPos(item.getCorrectAnswer())]).setChecked(true);
        } catch (Exception e) {
            Log.d("xxx", "err: " + e.getMessage());
            ((RadioButton) radioPictures[0]).setChecked(true);
        }
    }

    private void onUpdateItemCallBack(Question item) {
        Helper.hideKeyboard(getActivity());
        this.item = item;
        initQuestionPictureManager();
        resetLayoutAndParams(item.getAnswerType());
        Toast.makeText(getActivity(), "Item  updated", Toast.LENGTH_SHORT).show();
        isSaving = false;
    }

    private void onAddItemCallBack(Question item) {
        Toast.makeText(getActivity(), "Item  added", Toast.LENGTH_SHORT).show();
        resetAll();
    }

    private void onDeleteItemCallback() {
        Toast.makeText(getActivity(), "Item  deleted", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
    }

    // SUPPORT
    private void initQuestionPictureManager() {
        if (formType.equals("edit")) {
            String questionPath = (item.getIsImageQuestion()) ? item.getQuestion() : null;
            if (item.getIsImageAnswer()) {
                questionPictureManager = new QuestionPictureManager(questionPath, item.getAnswerA(), item.getAnswerB(), item.getAnswerC(), item.getAnswerD());
            } else {
                questionPictureManager = new QuestionPictureManager(questionPath, null, null, null, null);
            }
        } else {
            questionPictureManager = new QuestionPictureManager(null, null, null, null, null);
        }
    }

    private HashMap<String, String> getSaveParams() {
        HashMap<String, String> saveParams = new HashMap<>();
        String id = (formType.equals("edit")) ? item.getId() : null;
        String level = getQuestionLevelValue();
        String answerType = getAnswerTypeSelected();
        String correctAnswer = getCorrectAnswerValue();
        saveParams.put("id", id);
        saveParams.put("level", level);
        saveParams.put("answerType", answerType);
        saveParams.put("correctAnswerInLetter", correctAnswer);
        return saveParams;

    }

    public void resetLayoutAndParams(String type) {
        // if current type is text => clear picture answer, if current type is picture => clear text answer
        if (type.equals("picture")) {
            edtAnswerA.setText("");
            edtAnswerB.setText("");
            edtAnswerC.setText("");
            edtAnswerD.setText("");
        } else {
            pictureA.setImageResource(R.drawable.add_picture);
            pictureB.setImageResource(R.drawable.add_picture);
            pictureC.setImageResource(R.drawable.add_picture);
            pictureD.setImageResource(R.drawable.add_picture);
            rltUploadProgress.setVisibility(View.GONE);
        }
        setDefaultPictureAnswerParams();
    }

    public void resetAll() {
        Helper.clearImageView(pictureQuestion);
        pictureA.setImageResource(R.drawable.add_picture);
        pictureB.setImageResource(R.drawable.add_picture);
        pictureC.setImageResource(R.drawable.add_picture);
        pictureD.setImageResource(R.drawable.add_picture);
        rltUploadProgress.setVisibility(View.GONE);
        txtQuestion.setText("");
        edtAnswerA.setText("");
        edtAnswerB.setText("");
        edtAnswerC.setText("");
        edtAnswerD.setText("");
    }

    public void showInValidMessage() {
        isSaving = false;
        Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
    }

    public void setDefaultPictureAnswerParams() {
        totalUpload = 0;
        progressArr = new double[]{0, 0, 0, 0, 0};
        progress = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuestionFormData.reset();
    }

    private void solveQuestionVisibility(String type) {
        switch (type) {
            case "audio":
                rltTxtQuestion.setVisibility(View.VISIBLE);
                imgSpeaker.setVisibility(View.VISIBLE);
                pictureQuestion.setVisibility(View.GONE);
                break;
            case "text":
                rltTxtQuestion.setVisibility(View.VISIBLE);
                imgSpeaker.setVisibility(View.GONE);
                pictureQuestion.setVisibility(View.GONE);
                break;
            case "picture":
                rltTxtQuestion.setVisibility(View.GONE);
                pictureQuestion.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initTextToSpeech() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPickQuestionPicture() {
        isPickingQuestionPicture = true;
        interactingPicture = pictureQuestion;
        requestPermissionForReadExternalStorage(IntentCode.PICTURE_QUESTION_PICTURE_CODE);
    }

    // ADD QUESTION DIALOG
    private void showAddQuestionDialog() {
        AddQuestionDialogFragment dialog = new AddQuestionDialogFragment();
        dialog.show(getFragmentManager(), "add-question-dialog");
    }

    public void onAddQuestionDialogDismiss() {
        solveQuestionVisibility(QuestionFormData.getQuestionType());
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                txtQuestion.setText(QuestionFormData.getQuestionText());
                break;
            case "picture":
                break;
            case "audio":
                txtQuestion.setText(QuestionFormData.getQuestionSpeech());
                break;
        }
    }


    // SEC DIALOG
    private void showSecDialog() {
        SecDialogFragment dialog = new SecDialogFragment();
        dialog.show(getFragmentManager(), "sec-dialog");
    }

    public void onSecDialogDismiss() {
        updateSecText(QuestionFormData.getTimeLimit());
    }

    public void updateSecText(int sec) {
        txtSec.setText(sec + " sec");
    }


}