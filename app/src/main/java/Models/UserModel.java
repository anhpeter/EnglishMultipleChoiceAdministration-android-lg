package Models;

import android.app.Activity;
import android.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.ICallback;
import Defines.MyUser;

public class UserModel extends Model{

    ICallback<MyUser> iCallback;

    public UserModel(Activity activity, Fragment fragment) {
        super("users", activity);
        iCallback = (ICallback<MyUser>) fragment;
    }

    @Override
    public void listAll(HashMap<String, String> params, final String tag) {
        db.collection(collection)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<MyUser> userArrayList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    MyUser user = MyUser.getUserBySnapshot(queryDocumentSnapshot);
                    userArrayList.add(user);
                }
                iCallback.listCallBack(userArrayList, tag);
            }
        });
    }

    @Override
    public void getItemById(String id, final String tag) {
        db.collection(collection).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MyUser user = MyUser.getUserBySnapshot(documentSnapshot);
                iCallback.itemCallBack(user, tag);
            }
        });
    }
}
