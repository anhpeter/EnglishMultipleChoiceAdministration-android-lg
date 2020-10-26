package MainFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import Helpers.Helper;

public class IndexTitleBarFragment extends Fragment {

    String fragmentName = "question-index-title-bar";
    RelativeLayout rltDefault, rltSearchMode, rltSelectMode;
    Button btnRightMenu, btnSearch, btnRunSearch, btnBack, btnBackInSelectMode, btnDelete;
    EditText edtSearch;
    Spinner spinnerController;
    View v;

    String controller;
    String questionLevel;

    FragmentCommunicate fragmentCommunicate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_index_title_bar, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        return v;
    }

    private void onInit(){
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        // set arguments
        setArguments();

        initSpinner();
        initRightMenu();
        initSearchEvents();
        onBackClicked();
    }

    private void initSearchEvents(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.requestFocus();
                rltSearchMode.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onBackClicked(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        btnBackInSelectMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    private void onBack(){
        Helper.hideKeyboard(getActivity());
        rltSearchMode.setVisibility(View.GONE);
        rltSelectMode.setVisibility(View.GONE);
    }

    private void initSpinner() {
        String[] spinnerItems = {"Hard question", "Medium question", "Easy question"};
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerController.setAdapter(adapter);
        spinnerController.setSelection(getSpinnerItemPosition());
        spinnerController.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fragmentCommunicate.communicate(getHashMapDataBySpinnerItemPosition(position), fragmentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRightMenu(){
        btnRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), btnRightMenu);
                popupMenu.getMenuInflater().inflate(R.menu.index_right_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuLogout:
                                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private int getSpinnerItemPosition() {
        int position = 0;
        if (controller.equals("questions")) {
            if (questionLevel.equals("medium")) position =  1;
            else if (questionLevel.equals("easy")) position =  2;
        } else position = 3;
        return position;
    }

    private HashMap<String, String> getHashMapDataBySpinnerItemPosition(int position){
        HashMap<String, String> data =new HashMap<>();
        data.put("controller", "questions");
        if (position == 0){
            data.put("questionLevel", "hard");
        }else if (position == 1){
            data.put("questionLevel", "medium");
        }else if (position == 2){
            data.put("questionLevel", "easy");
        }
        return data;
    }

    private void setArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HashMap<String, String> params = (HashMap<String, String>) bundle.getSerializable("params");
            controller = params.get("controller");
            questionLevel = params.get("questionLevel");
        }
    }

    private void mapping() {
        rltDefault = (RelativeLayout)  v.findViewById(R.id.rltDefault);
        rltSearchMode = (RelativeLayout)  v.findViewById(R.id.rltSearchMode);
        rltSelectMode = (RelativeLayout)  v.findViewById(R.id.rltSelectMode);
        btnRightMenu = (Button) v.findViewById(R.id.btnRightMenu);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        btnRunSearch = (Button) v.findViewById(R.id.btnRunSearch);
        btnBack = (Button) v.findViewById(R.id.btnBack);
        btnDelete = (Button) v.findViewById(R.id.btnDelete);
        btnBackInSelectMode = (Button) v.findViewById(R.id.btnBackInSelectMode);
        spinnerController = (Spinner) v.findViewById(R.id.spinnerController);
        edtSearch = (EditText) v.findViewById(R.id.edtSearch);
    }

}