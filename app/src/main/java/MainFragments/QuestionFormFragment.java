package MainFragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.multiple_choice.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.ICallback;
import Defines.IMyStorage;
import Defines.Question;
import Helpers.Helper;
import Helpers.MyStorage;
import Helpers.QuestionPictureManager;
import Models.QuestionModel;

public class QuestionFormFragment extends Fragment implements ICallback<Question>, IMyStorage {

    String fragmentName = "question-form";
    FragmentCommunicate fragmentCommunicate;
    QuestionModel questionModel;
    View v;

    // PARAMS
    String formType;
    Question item;
    HashMap<String, String> params;

    // WIDGET
    Spinner spinnerLevel;
    RadioGroup radioGroupQuestionType, radioGroupCorrectAnswer;
    EditText edtQuestion, edtAnswerA, edtAnswerB, edtAnswerC, edtAnswerD;
    TextView txtUploadProgress;
    RadioButton radioPictureA, radioPictureB, radioPictureC, radioPictureD;
    ImageView pictureA, pictureB, pictureC, pictureD;
    RelativeLayout rltTextAnswer, rltPictureAnswer, rltUploadProgress;
    Button btnSubmit;
    ProgressBar progressUpload;

    // VALUE ARRAY
    String levelValues[] = {"hard", "medium", "easy"};
    String typeValues[] = {"text", "picture"};
    String correctAnswerValues[] = {"A", "B", "C", "D"};
    String picturePaths[] = {"", "", "", ""};

    // WIDGET ARRAYS
    RadioButton radioPictures[];
    ImageView pictures[];

    // REQUEST CODE
    int PICK_PICTURE_FROM_FILE_EXPLORE_REQUEST_CODE = 111;

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
        return v;
    }

    private void onInit() {
        setDefaultPictureQuestionParams();
        Helper.clearEditTextFocus(edtQuestion);
        questionModel = new QuestionModel(getActivity(), this);
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        radioPictures = new RadioButton[]{radioPictureA, radioPictureB, radioPictureC, radioPictureD};
        pictures = new ImageView[]{pictureA, pictureB, pictureC, pictureD};
        radioPictureA.setChecked(true);
        setArguments();
        //initLevelSpinner();
        onPictureRadiosChecked();
        onPictureClicked();
        onQuestionTypeRadiosChecked();
        onSubmitClick();
    }

    private void onPictureClicked() {
        for (int i = 0; i < pictures.length; i++) {
            pictures[i].setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    interactingPicture = ((ImageView) v);
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            PICK_PICTURE_FROM_FILE_EXPLORE_REQUEST_CODE);
                }
            });
        }
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

    private void onPictureRadiosChecked() {
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

    private void onQuestionTypeRadiosChecked() {
        radioGroupQuestionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonText:
                        rltPictureAnswer.setVisibility(View.GONE);
                        rltTextAnswer.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButtonPicture:
                        rltTextAnswer.setVisibility(View.GONE);
                        rltPictureAnswer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void initLevelSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, levelValues);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerLevel.setAdapter(adapter);

        // easy is default
        spinnerLevel.setSelection(getLevelPos(params.get("level")));
        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // ON SUBMIT
    private void onSubmitClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionType = getQuestionTypeValue();
                if (questionType == "text") {
                    onSaveTextQuestion();
                } else if (questionType == "picture") {
                    onSavePictureQuestion();
                }
            }
        });
    }

    // ON SAVE
    private void onSaveTextQuestion() {
        HashMap<String, String> saveParams = getSaveParams();
        String answerA = edtAnswerA.getText().toString().trim();
        String answerB = edtAnswerB.getText().toString().trim();
        String answerC = edtAnswerC.getText().toString().trim();
        String answerD = edtAnswerD.getText().toString().trim();
        if (isTextQuestionOptionValid(answerA, answerB, answerC, answerD)) {
            questionPictureManager.deleteAllIfExist();
            Question questionObj = new Question(saveParams.get("id"), saveParams.get("question"), saveParams.get("correctAnswer"),
                    answerA, answerB, answerC, answerD, saveParams.get("questionType"), saveParams.get("level"), -1);
            if (formType.equals("edit")) solveEdit(questionObj);
            else if (formType.equals("add")) solveAdd(questionObj);
        } else {
            showInValidMessage();
        }
    }

    private void onSavePictureQuestion() {
        if (isPictureQuestionOptionValid() && questionPictureManager.isAnswerUriValid()) {
            totalUpload = questionPictureManager.getTotalUpload();
            if (totalUpload > 0) {
                rltUploadProgress.setVisibility(View.VISIBLE);
                questionPictureManager.upload(QuestionFormFragment.this);
            } else {
                savePictureQuestion();
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

    private boolean isTextQuestionOptionValid(String answerA, String answerB, String answerC, String answerD) {
        if (
                isPictureQuestionOptionValid() &&
                        !answerA.trim().isEmpty() &&
                        !answerB.trim().isEmpty() &&
                        !answerC.trim().isEmpty() &&
                        !answerD.trim().isEmpty()
        ) return true;
        return false;
    }

    private boolean isPictureQuestionOptionValid() {
        HashMap<String, String> saveParams = getSaveParams();
        if (
                !saveParams.get("question").isEmpty() &&
                        !saveParams.get("questionType").isEmpty() &&
                        !saveParams.get("level").isEmpty() &&
                        !saveParams.get("correctAnswer").trim().isEmpty()
        ) return true;
        return false;
    }

    private String getQuestionLevelValue() {
        String result = params.get("level");
        /*
        int pos = spinnerLevel.getSelectedItemPosition();
        result = levelValues[pos];

         */
        return result;
    }

    private String getCorrectAnswerValue() {
        String result = "";
        if (getQuestionTypeValue().equals("text")) {
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

    private String getQuestionTypeValue() {
        String result = "";
        int typeRadioButtonId = radioGroupQuestionType.getCheckedRadioButtonId();
        switch (typeRadioButtonId) {
            case R.id.radioButtonText:
                result = "text";
                break;
            case R.id.radioButtonPicture:
                result = "picture";
                break;
        }
        return result;
    }

    private void setArguments() {
        Bundle b = getArguments();
        // form params
        params = (HashMap<String, String>) b.getSerializable("params");
        String id = params.get("id");
        String defaultLevel = params.get("level");
        formType = params.get("formType");
        if (formType.equals("edit") && id != null) {
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
        return java.util.Arrays.asList(correctAnswerValues).indexOf(answer);
    }

    // MAPPING
    private void mapping() {
        //spinnerLevel = (Spinner) v.findViewById(R.id.spinnerLevel);
        radioGroupQuestionType = (RadioGroup) v.findViewById(R.id.radioGroupQuestionType);
        radioGroupCorrectAnswer = (RadioGroup) v.findViewById(R.id.radioGroupCorrectAnswer);
        edtQuestion = (EditText) v.findViewById(R.id.edtQuestion);
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

        pictureA = (ImageView) v.findViewById(R.id.pictureA);
        pictureB = (ImageView) v.findViewById(R.id.pictureB);
        pictureC = (ImageView) v.findViewById(R.id.pictureC);
        pictureD = (ImageView) v.findViewById(R.id.pictureD);

        rltTextAnswer = (RelativeLayout) v.findViewById(R.id.rltTextAnswer);
        rltPictureAnswer = (RelativeLayout) v.findViewById(R.id.rltPictureAnswer);
        rltUploadProgress = (RelativeLayout) v.findViewById(R.id.rltProgressUpload);
        progressUpload = (ProgressBar) v.findViewById(R.id.progressUpload);
    }

    // PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_PICTURE_FROM_FILE_EXPLORE_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, PICK_PICTURE_FROM_FILE_EXPLORE_REQUEST_CODE);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PICTURE_FROM_FILE_EXPLORE_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri filePath = data.getData();
            questionPictureManager.setUriByIndex(getAnswerPosByImageViewId(interactingPicture.getId()), filePath);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                interactingPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // STORAGE CALLBACK
    @Override
    public void uploadedCallback(String url, int code) {
        Log.d("xxx", url);
        questionPictureManager.setPathByIndex(code, url);
    }

    @Override
    public void progressCallback(double value, int code) {
        progressArr[code] = value;
        double totalProgress = 0;
        for (int i = 0; i < 4; i++) totalProgress += progressArr[i];
        progress = (int) (((totalProgress / 100) / totalUpload) * 100);
        progressUpload.setProgress(progress);
        txtUploadProgress.setText(progress + "%");
        if (progress == 100) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            savePictureQuestion();
                        }
                    }, 500);
        }
    }

    public void savePictureQuestion() {
        Log.d("xxx", "on save");
        HashMap<String, String> saveParams = getSaveParams();
        Question questionObj = new Question(saveParams.get("id"), saveParams.get("question"), saveParams.get("correctAnswer"),
                questionPictureManager.getAnswerAPath(),
                questionPictureManager.getAnswerBPath(), questionPictureManager.getAnswerCPath(), questionPictureManager.getAnswerDPath(),
                saveParams.get("questionType"), saveParams.get("level"), -1);
        if (formType.equals("edit")) solveEdit(questionObj);
        else if (formType.equals("add")) solveAdd(questionObj);
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
        edtQuestion.setText(item.getQuestion());
        ((RadioButton) radioGroupQuestionType.getChildAt(getTypePos(item.getQuestionType()))).setChecked(true);
        if (item.getQuestionType().equals("text")) {
            setTextQuestionView();
        } else {
            setPictureQuestionView();
        }
        initQuestionPictureManager();
    }

    private void setTextQuestionView() {
        rltTextAnswer.setVisibility(View.VISIBLE);
        rltPictureAnswer.setVisibility(View.GONE);
        edtQuestion.setText(item.getQuestion());
        edtAnswerA.setText(item.getAnswerA());
        edtAnswerB.setText(item.getAnswerB());
        edtAnswerC.setText(item.getAnswerC());
        edtAnswerD.setText(item.getAnswerD());
        ((RadioButton) radioGroupCorrectAnswer.getChildAt(getCorrectAnswerPos(item.getCorrectAnswer()))).setChecked(true);
    }

    private void setPictureQuestionView() {
        rltPictureAnswer.setVisibility(View.VISIBLE);
        rltTextAnswer.setVisibility(View.GONE);
        Picasso.get().load(item.getAnswerA()).into(pictureA);
        Picasso.get().load(item.getAnswerB()).into(pictureB);
        Picasso.get().load(item.getAnswerC()).into(pictureC);
        Picasso.get().load(item.getAnswerD()).into(pictureD);
        ((RadioButton) radioPictures[getCorrectAnswerPos(item.getCorrectAnswer())]).setChecked(true);
    }

    private void onUpdateItemCallBack(Question item) {
        this.item = item;
        initQuestionPictureManager();
        resetLayoutAndParams(item.getQuestionType());
        Toast.makeText(getActivity(), "Item  updated", Toast.LENGTH_SHORT).show();
    }

    private void onAddItemCallBack(Question item) {
        Toast.makeText(getActivity(), "Item  added", Toast.LENGTH_SHORT).show();
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
            if (item.getQuestionType().equals("picture")) {
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
        String question = edtQuestion.getText().toString().trim();
        String level = getQuestionLevelValue();
        String questionType = getQuestionTypeValue();
        String correctAnswer = getCorrectAnswerValue();

        saveParams.put("id", id);
        saveParams.put("question", question);
        saveParams.put("level", level);
        saveParams.put("questionType", questionType);
        saveParams.put("correctAnswer", correctAnswer);
        return saveParams;
    }

    public void resetLayoutAndParams(String type) {
        // if current type is text => clear picture answer, if current type is picture => clear text answer
        if (type.equals("text")) {
            pictureA.setImageResource(R.drawable.add_picture);
            pictureB.setImageResource(R.drawable.add_picture);
            pictureC.setImageResource(R.drawable.add_picture);
            pictureD.setImageResource(R.drawable.add_picture);
            rltUploadProgress.setVisibility(View.GONE);
        } else {
            edtAnswerA.setText("");
            edtAnswerB.setText("");
            edtAnswerC.setText("");
            edtAnswerD.setText("");
        }
        setDefaultPictureQuestionParams();
    }

    public void showInValidMessage() {
        Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
    }

    public void setDefaultPictureQuestionParams() {
        totalUpload = 0;
        progressArr = new double[]{0, 0, 0, 0};
        progress = 0;
    }

}