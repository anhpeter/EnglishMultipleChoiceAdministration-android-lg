package MainFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
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

import com.example.multiple_choice.LoginActivity;
import com.example.multiple_choice.R;

import java.util.HashMap;

import Interfaces.FragmentCommunicate;
import Defines.Question;
import Helpers.Helper;

public class IndexTitleBarFragment extends MyFragment {

    String fragmentName = "index-title-bar";
    RelativeLayout rltDefault, rltSearchMode, rltSelectMode;
    Button btnRightMenu, btnSearch, btnRunSearch, btnBack, btnBackInSelectMode, btnDelete;
    EditText edtSearch;
    Spinner spinnerController;
    View v;

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

    private void onInit() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        initSpinner();
        initRightMenu();
        initSearchEvents();
        onBackClicked();
    }

    private void initSearchEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.requestFocus();
                rltSearchMode.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onBackClicked() {
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

    private void onBack() {
        Helper.hideKeyboard(getActivity());
        rltSearchMode.setVisibility(View.GONE);
        rltSelectMode.setVisibility(View.GONE);
    }

    private void initSpinner() {
        solveQuestionLevelSpinnerVisibility();
        String[] spinnerItems = Question.getSpinnerLevelArr();
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerController.setAdapter(adapter);
        spinnerController.setSelection(getSpinnerItemPosition());
        spinnerController.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setQuestionLevel(position);
                fragmentCommunicate.communicate((HashMap<String, String>) null, fragmentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRightMenu() {
        btnRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), btnRightMenu);
                popupMenu.getMenuInflater().inflate(R.menu.index_right_menu, popupMenu.getMenu());

                // solve menu item displayed
                //solveMenuItemDisplayed(popupMenu.getMenu());

                // solve menu action
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuLogout:
                                onLogoutClick();
                                break;

                            case R.id.menuQuestion:
                                onControllerClick("questions");
                                break;

                            case R.id.menuUser:
                                onControllerClick("users");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void onControllerClick(String value) {
        if (!getCalledActivity().getController().equals(value)) {
            getCalledActivity().setIsChangingController(true);
            getCalledActivity().setController(value);
            solveQuestionLevelSpinnerVisibility();
            fragmentCommunicate.communicate((HashMap<String, String>) null, fragmentName);
        }
    }

    private void solveQuestionLevelSpinnerVisibility(){
        if (getCalledActivity().getController().equals("questions")) spinnerController.setVisibility(View.VISIBLE);
        else spinnerController.setVisibility(View.GONE);
    }


    private void solveMenuItemDisplayed(Menu menu) {
        if (getCalledActivity().getController().equals("questions")) {
            menu.findItem(R.id.menuQuestion).setVisible(false);
        } else {
            menu.findItem(R.id.menuUser).setVisible(false);
        }
    }

    private void onLogoutClick() {
        showLogoutDialog();
    }

    private void onLogout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("global-package", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("loggedUsername");
        editor.apply();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    private void showLogoutDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Do you want to logout?");

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onLogout();
            }
        });
        alertDialog.show();
    }

    private int getSpinnerItemPosition() {
        int position = Question.getLevelIndexByLevel(getCalledActivity().getQuestionLevel());
        return position;
    }

    private void setQuestionLevel(int position) {
        String questionLevel = Question.getLevelByIndex(position);
        if (!getCalledActivity().getQuestionLevel().equals(questionLevel)){
            getCalledActivity().setIsChangingQuestionLevel(true);
            getCalledActivity().setQuestionLevel(questionLevel);
        }
    }

    private void mapping() {
        rltDefault = (RelativeLayout) v.findViewById(R.id.rltDefault);
        rltSearchMode = (RelativeLayout) v.findViewById(R.id.rltSearchMode);
        rltSelectMode = (RelativeLayout) v.findViewById(R.id.rltSelectMode);
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