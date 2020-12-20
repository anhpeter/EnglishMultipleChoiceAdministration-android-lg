package MainFragments;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiple_choice.Activity;
import com.example.multiple_choice.MainActivity;
import com.example.multiple_choice.R;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.ICallback;
import Defines.Question;
import Defines.QuestionAdapter;
import Helpers.Helper;
import Models.QuestionModel;

public class QuestionIndexFragment extends MyFragment implements ICallback<Question> {

    FragmentCommunicate fragmentCommunicate;
    String fragmentName = "question-index";
    QuestionModel questionModel;
    View v;

    TextView txtMessage;
    Button btnAdd;
    ListView lvMain;
    ArrayList<Question> questionArrayList;
    QuestionAdapter questionAdapter;
    ArrayList<Question> searchQuestionArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_index, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        return v;
    }

    private void onInit() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        questionModel = new QuestionModel(getActivity(), this);
        questionArrayList = new ArrayList<>();
        onListAll();
        initListView();
        onAddClicked();
    }

    public void onListAll() {
        if (getCalledActivity().internetConnectionAvailable()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("level", getCalledActivity().getQuestionLevel());
            questionModel.listAll(params, "list-all");
        } else {
            Helper.solveListMessage(true, lvMain, txtMessage, "No Internet connection");
        }
    }

    private void initListView() {
        questionAdapter = new QuestionAdapter(getActivity(), R.layout.question_item_layout, questionArrayList);
        lvMain.setAdapter(questionAdapter);
        setListViewEvents();
    }

    private void setListViewEvents() {
        onListViewItemClick();
    }

    private void onListViewItemClick() {
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestForm(questionArrayList.get(position).getId());
            }
        });
    }

    private void onAddClicked() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForm(null);
            }
        });
    }

    private void requestForm(String id) {
        String formType = (id == null) ? "add" : "edit";
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("formType", formType);
        fragmentCommunicate.communicate(params, fragmentName);
    }

    private void mapping() {
        btnAdd = (Button) v.findViewById(R.id.btnAdd);
        lvMain = (ListView) v.findViewById(R.id.lvMain);
        txtMessage = (TextView) v.findViewById(R.id.txtMessage);
    }

    public void onChangeData(HashMap<String, String> params) {
        onListAll();
    }

    // DATABASE CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {
    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag == "list-all") onListAllCallback(items);
    }

    private void onListAllCallback(ArrayList<Question> items) {
        questionArrayList.clear();

        // solve message
        Helper.solveListMessage(items.isEmpty(), lvMain, txtMessage, "No question, yet.");
        if (!items.isEmpty()) {
            questionArrayList.addAll(items);
        }
        questionAdapter.notifyDataSetChanged();
    }
}