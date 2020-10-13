package MainFragments;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.multiple_choice.R;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.ICallback;
import Defines.Question;
import Defines.QuestionAdapter;
import Helpers.Helper;
import Models.QuestionModel;

public class QuestionIndexFragment extends Fragment implements ICallback<Question> {

    View v;
    Button btnAdd;
    ListView lvMain;
    QuestionModel questionModel;
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
        questionModel = new QuestionModel(getActivity(), this);
        questionModel.listAll( "list-all");
        questionArrayList = new ArrayList<>();
        initListView();
        onAddClicked();
    }

    private void initListView(){
        questionAdapter = new QuestionAdapter(getActivity(), R.layout.question_item_layout, questionArrayList);
        lvMain.setAdapter(questionAdapter);
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
    }

    public void onChangeData(HashMap<String, String> data){
        Toast.makeText(getActivity(), "received data from index", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemCallBack(Question item, String tag) {

    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag == "list-all") onListAll(items);
    }

    private void onListAll(ArrayList<Question> items){
        Log.d("xxx", "list callback called");
        questionArrayList.clear();
        questionArrayList.addAll(items);
        for (Question question: questionArrayList){
            Log.d("xxx", question.getQuestion());
        }
        questionAdapter.notifyDataSetChanged();
    }
}