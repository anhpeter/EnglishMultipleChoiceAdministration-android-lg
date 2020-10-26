package MainFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

public class QuestionFormTitleBarFragment extends Fragment implements ICallback {

    String fragmentName = "question-form-title-bar";
    Button btnBack, btnDelete;
    View v;
    String id;
    FragmentCommunicate fragmentCommunicate;
    QuestionModel questionModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_question_form_title_bar, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        return v;
    }

    private void onInit(){
        questionModel = new QuestionModel(getActivity(), this);
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        // set arguments
        setArguments();

        onBackClicked();
        onDeleteClicked();
    }

    private void onBackClicked(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Back clicked", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    private void onDeleteClicked(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Delete question?");

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDelete();
            }
        });
        alertDialog.show();
    }

    private void onDelete(){
        questionModel.deleteItemById(id, "delete-item");
    }

    private void setArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HashMap<String, String> params = (HashMap<String, String>) bundle.getSerializable("params");
            id = params.get("id");
        }
    }

    private void mapping() {
        btnBack = (Button) v.findViewById(R.id.btnBack);
        btnDelete = (Button) v.findViewById(R.id.btnDelete);
    }

    @Override
    public void itemCallBack(Object item, String tag) {
        if (tag.equals("delete-item")) onDeleteItemCallback();
    }

    private void onDeleteItemCallback(){
        Toast.makeText(getActivity(), "item "+id+" deleted", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }


    @Override
    public void listCallBack(ArrayList items, String tag) {

    }
}