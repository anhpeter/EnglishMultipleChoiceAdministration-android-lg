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

import com.example.multiple_choice.R;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.ICallback;
import Defines.Question;
import Defines.QuestionAdapter;
import Helpers.Helper;
import Models.QuestionModel;

public class QuestionIndexFragment extends Fragment implements ICallback<Question> {

    FragmentCommunicate fragmentCommunicate;
    String fragmentName = "question-index";
    QuestionModel questionModel;
    View v;

    TextView txtMessage;
    String questionLevel;
    Button btnAdd;
    ListView lvMain;
    ArrayList<Question> questionArrayList;
    QuestionAdapter questionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_index, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        return v;
    }

    private void onInit(){
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        setArguments();
        questionModel = new QuestionModel(getActivity(), this);
        questionArrayList = new ArrayList<>();
        onListAll();
        initListView();
        onAddClicked();
    }

    private void onListAll(){
        HashMap<String, String> params = new HashMap<>();
        params.put("level", questionLevel);
        questionModel.listAll(params, "list-all");
    }

    private void initListView(){
        questionAdapter = new QuestionAdapter(getActivity(), R.layout.question_item_layout, questionArrayList);
        lvMain.setAdapter(questionAdapter);
        setListViewEvents();
    }

    private void setListViewEvents(){
        onListViewItemClick();
    }

    private void onListViewItemClick(){
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", questionArrayList.get(position).getId());
                params.put("formType", "edit");
                fragmentCommunicate.communicate(params, fragmentName);
            }
        });
    }

    private void onAddClicked(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getActivity(), "add clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapping(){
        btnAdd = (Button) v.findViewById(R.id.btnAdd);
        lvMain = (ListView) v.findViewById(R.id.lvMain);
        txtMessage = (TextView) v.findViewById(R.id.txtMessage);
    }

    public void onChangeData(HashMap<String, String> params){
        if (params!=null){
            questionLevel = params.get("questionLevel");
            onListAll();
        }
    }

    @Override
    public void itemCallBack(Question item, String tag) {
    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag == "list-all") onListAllCallback(items);
    }

    private void onListAllCallback(ArrayList<Question> items){
        Log.d("xxx", "list callback called, items size: "+items.size());
        questionArrayList.clear();
        if (!items.isEmpty()){
            txtMessage.setText("");
            txtMessage.setVisibility(View.GONE);
            lvMain.setVisibility(View.VISIBLE);
            questionArrayList.addAll(items);
        }else{
            lvMain.setVisibility(View.GONE);
            txtMessage.setText("There are no questions to show");
            txtMessage.setVisibility(View.VISIBLE);
        }
        questionAdapter.notifyDataSetChanged();
    }

    private void setArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HashMap<String, String> params = (HashMap<String, String>) bundle.getSerializable("params");
            questionLevel = params.get("questionLevel");
        }
    }

}