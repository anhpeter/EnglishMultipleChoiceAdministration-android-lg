package MainFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    EditText edtQuestion, edtAnswerA, edtAnswerB, edtAnswerC, edtAnswerD;
    Button btnSubmit;

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
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        questionModel = new QuestionModel(getActivity(), this);
        setArguments();
    }

    private void setArguments() {
        Bundle b = getArguments();
        // form params
        HashMap<String, String> params = (HashMap<String, String>) b.getSerializable("params");
        String id = params.get("id");
        formType = params.get("formType");
        Log.d("xxx", "formType: "+formType+", id: "+id);
        if (formType.equals("edit") && id != null) getItemById(id);
    }

    private void getItemById(String id) {
        Log.d("xxx", "get item by id: "+id);
        questionModel.getItemById(id, "edit-item");
    }

    @Override
    public void itemCallBack(Question item, String tag) {
        Log.d("xxx", "callback run");
        if (tag.equals("edit-item")) onGetEditItemCallBack(item);
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
    }

    private void mapping() {
        edtQuestion = (EditText) v.findViewById(R.id.edtQuestion);
        edtAnswerA = (EditText) v.findViewById(R.id.edtAnswerA);
        edtAnswerB = (EditText) v.findViewById(R.id.edtAnswerB);
        edtAnswerC = (EditText) v.findViewById(R.id.edtAnswerC);
        edtAnswerD = (EditText) v.findViewById(R.id.edtAnswerD);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
    }
}