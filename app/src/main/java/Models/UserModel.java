package Models;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Defines.ICallback;
import Defines.MyUser;
import Defines.Question;

public class UserModel extends Model{

    ICallback<MyUser> iCallback;

    public UserModel(Activity activity, Fragment fragment) {
        super("Users", activity);
        iCallback = (ICallback<MyUser>) fragment;
    }

    @Override
    public void listAll(HashMap<String, String> params, final String tag) {
        getCollectionRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("xxx", "list all called");
                ArrayList<MyUser> userArrayList = new ArrayList<>();
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    MyUser user = MyUser.getUserByDataSnapshot(childSnapshot);
                    userArrayList.add(user);
                }

                // sort
                Collections.sort(userArrayList, new Comparator<MyUser>() {
                    @Override
                    public int compare(MyUser u1, MyUser u2)
                    {
                        long result  =u2.getCreated() - u1.getCreated();

                        if (result > 0) return 1;
                        else if (result == 0) return 0;
                        else return -1;
                    }
                });
                iCallback.listCallBack(userArrayList, tag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
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

         */
    }

    @Override
    public void getItemById(String id, final String tag) {
        /*
        db.collection(collection).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MyUser user = MyUser.getUserBySnapshot(documentSnapshot);
                iCallback.itemCallBack(user, tag);
            }
        });

         */
    }
}
