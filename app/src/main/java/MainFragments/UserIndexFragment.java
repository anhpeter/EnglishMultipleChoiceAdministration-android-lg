package MainFragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.multiple_choice.R;

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.FragmentCommunicate;
import Interfaces.ICallback;
import Defines.MyUser;
import Adapter.UserAdapter;
import Helpers.Helper;
import Models.UserModel;

public class UserIndexFragment extends MyFragment implements ICallback<MyUser> {

    FragmentCommunicate fragmentCommunicate;
    String fragmentName = "user-index";
    UserModel userModel;
    View v;

    TextView txtMessage;
    Button btnAdd;
    ListView lvMain;
    ArrayList<MyUser> userArrayList;
    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_index, container, false);
        Helper.initFontAwesome(getActivity(), v);

        mapping();
        onInit();
        return v;
    }

    private void onInit() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        userModel = new UserModel(getActivity(), this);
        userArrayList = new ArrayList<>();
        onListAll();
        initListView();
        onAddClicked();
    }

    private void initListView() {
        userAdapter = new UserAdapter(getActivity(), R.layout.user_item_layout, userArrayList);
        lvMain.setAdapter(userAdapter);
        //setListViewEvents();
    }

    public void onListAll() {
        if (getCalledActivity().internetConnectionAvailable()){
            HashMap<String, String> params = new HashMap<>();
            userModel.listAll(params, "list-all");
        }else{
            Helper.solveListMessage(true, lvMain, txtMessage, "No Internet connection");
        }
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

    private void mapping(){
        btnAdd = (Button) v.findViewById(R.id.btnAdd);
        lvMain = (ListView) v.findViewById(R.id.lvMain);
        txtMessage = (TextView) v.findViewById(R.id.txtMessage);
    }

    @Override
    public void itemCallBack(MyUser item, String tag) {
    }

    @Override
    public void listCallBack(ArrayList<MyUser> items, String tag) {
        if (tag.equals("list-all")) onListAllCallback(items);
    }


    private void onListAllCallback(ArrayList<MyUser> items) {
        userArrayList.clear();
        // solve message
        Helper.solveListMessage(items.isEmpty(), lvMain, txtMessage, "No user, yet.");
        if (!items.isEmpty()) {
            userArrayList.addAll(items);
        }
        userAdapter.notifyDataSetChanged();
    }
}