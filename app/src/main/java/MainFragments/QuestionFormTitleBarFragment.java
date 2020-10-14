package MainFragments;

import android.app.Fragment;
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

import java.util.HashMap;

import Defines.FragmentCommunicate;
import Helpers.Helper;

public class QuestionFormTitleBarFragment extends Fragment {

    String fragmentName = "question-form-title-bar";
    Button btnBack, btnDelete;
    View v;
    String id;
    FragmentCommunicate fragmentCommunicate;

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
                Toast.makeText(getActivity(), "Delete clicked: "+id, Toast.LENGTH_SHORT).show();
            }
        });
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
}