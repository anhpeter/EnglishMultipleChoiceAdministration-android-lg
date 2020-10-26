package MainFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.multiple_choice.R;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.ICallback;
import Defines.Question;
import Helpers.Helper;
import Models.QuestionModel;

public class QuestionFormFragment extends Fragment implements ICallback<Question> {

    String fragmentName = "question-form";
    FragmentCommunicate fragmentCommunicate;
    QuestionModel questionModel;
    View v;

    String formType;
    Question item;
    HashMap<String, String> params;

    Spinner spinnerLevel;
    RadioGroup radioGroupQuestionType, radioGroupCorrectAnswer;
    EditText edtQuestion, edtAnswerA, edtAnswerB, edtAnswerC, edtAnswerD;
    Button btnSubmit;
    String levelValues[] = {"hard", "medium", "easy"};
    String typeValues[] = {"text", "picture"};
    String correctAnswerValues[] = {"A", "B", "C", "D"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_question_form, container, false);
        /*
        edtQuestion.clearFocus();
        Helper.hideKeyboard(getActivity());
        Helper.initFontAwesome(getActivity(), v);
        */

        mapping();
        onInit();
        return v;
    }

    private void onInit() {
        Helper.clearEditTextFocus(edtQuestion);
        questionModel = new QuestionModel(getActivity(), this);
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        setArguments();
        //initLevelSpinner();
        initRadioEvents();
        onSubmitClick();
    }

    private void initRadioEvents() {
        radioGroupQuestionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonText:
                        break;
                    case R.id.radioButtonPicture:
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
                //fragmentCommunicate.communicate(getHashMapDataBySpinnerItemPosition(position), fragmentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onSubmitClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                String id = (formType.equals("edit")) ? item.getId() : null;
                String question = edtQuestion.getText().toString().trim();
                String questionType = getQuestionTypeValue();
                String level = getQuestionLevelValue();
                String answerA = edtAnswerA.getText().toString().trim();
                String answerB = edtAnswerB.getText().toString().trim();
                String answerC = edtAnswerC.getText().toString().trim();
                String answerD = edtAnswerD.getText().toString().trim();
                String correctAnswer = getCorrectAnswerValue();
                if (isValid(question, questionType, level, correctAnswer, answerA, answerB, answerC, answerD)) {
                    Question questionObj = new Question(id, question, correctAnswer,
                            answerA, answerB, answerC, answerD, questionType, level, -1);
                    if (formType.equals("edit")) solveEdit(questionObj);
                    else if (formType.equals("add")) solveAdd(questionObj);
                } else {
                    Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void solveEdit(Question question) {
        questionModel.updateItem(question, "update-item");
    }

    private void solveAdd(Question question) {
        questionModel.addItem(question, "add-item");
    }

    private boolean isValid(String question, String questionType, String level, String correctAnswer, String answerA, String answerB, String answerC, String answerD) {
        if (
                !question.trim().isEmpty() &&
                        !questionType.trim().isEmpty() &&
                        !level.trim().isEmpty() &&
                        !correctAnswer.trim().isEmpty() &&
                        !answerA.trim().isEmpty() &&
                        !answerB.trim().isEmpty() &&
                        !answerC.trim().isEmpty() &&
                        !answerD.trim().isEmpty()
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
        if (formType.equals("edit") && id != null) getItemById(id);
    }

    private void getItemById(String id) {
        questionModel.getItemById(id, "get-edit-item");
    }

    @Override
    public void itemCallBack(Question item, String tag) {
        if (tag.equals("get-edit-item")) onGetEditItemCallBack(item);
        if (tag.equals("update-item")) onUpdateItemCallBack(item);
        if (tag.equals("add-item")) onAddItemCallBack(item);
    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
    }

    private void onGetEditItemCallBack(Question item) {
        this.item = item;
        edtQuestion.setText(item.getQuestion());
        edtAnswerA.setText(item.getAnswerA());
        edtAnswerB.setText(item.getAnswerB());
        edtAnswerC.setText(item.getAnswerC());
        edtAnswerD.setText(item.getAnswerD());
        setViewValueByItem();
    }

    private void setViewValueByItem(){
        //spinnerLevel.setSelection(getLevelPos(item.getLevel()));
        ((RadioButton)radioGroupQuestionType.getChildAt(getTypePos(item.getQuestionType()))).setChecked(true);
        ((RadioButton)radioGroupCorrectAnswer.getChildAt(getCorrectAnswerPos(item.getCorrectAnswer()))).setChecked(true);

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

    private void onUpdateItemCallBack(Question item) {
        Toast.makeText(getActivity(), "item "+item.getId()+" updated", Toast.LENGTH_SHORT).show();
    }

    private void onAddItemCallBack(Question item) {
        Toast.makeText(getActivity(), "item "+item.getId()+" added", Toast.LENGTH_SHORT).show();
    }

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
    }
}