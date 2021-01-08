package Models;

import android.app.Activity;
import android.app.Fragment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Interfaces.ICallback;
import Defines.Question;
import Helpers.Helper;

public class QuestionModel extends Model {

    ICallback<Question> iCallback;

    public QuestionModel(Activity activity, Fragment fragment) {
        super("Questions", activity);
        iCallback = (ICallback<Question>) fragment;
    }

    @Override
    public void listAll(final HashMap<String, String> params, final String tag) {
        getCollectionRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Question> questionArrayList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Question qt = Question.getQuestionByDataSnapshot(childSnapshot);

                    // FILTER
                    if (qt.getLevel().equals(params.get("level"))) {
                        questionArrayList.add(qt);
                    }
                }

                // SORT
                Collections.sort(questionArrayList, new Comparator<Question>() {
                    @Override
                    public int compare(Question q1, Question q2) {
                        boolean isSwap = q2.getLastInteracted() > q1.getLastInteracted();
                        if (isSwap) return 1;
                        else return -1;
                    }
                });
                iCallback.listCallBack(questionArrayList, tag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateItem(final Question question, final String tag) {
        getCollectionRef().child(question.getId()).setValue(question.getDocData()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void addItem(final Question question, final String tag) {
        final String id = Helper.getRandom(100000, 999999) + "";
        getCollectionRef().child(id).setValue(question.getDocData()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                question.setId(id);
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void deleteItemById(final String id, final String tag) {
        getCollectionRef().child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iCallback.itemCallBack(null, tag);
            }
        });
    }


    @Override
    public void getItemById(String id, final String tag) {
        getCollectionRef().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Question question = Question.getQuestionByDataSnapshot(snapshot);
                iCallback.itemCallBack(question, tag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
