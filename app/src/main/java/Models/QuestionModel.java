package Models;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Defines.ICallback;
import Defines.Question;
import Defines.User;
import MainFragments.QuestionIndexFragment;

public class QuestionModel extends Model {

    ICallback<Question> iCallback;

    public QuestionModel(Activity activity, Fragment fragment) {
        super("questions", activity);
        iCallback = (ICallback<Question>) fragment;
    }

    @Override
    public void listAll(final HashMap<String, String> params, final String tag) {
        Log.d("xxx", "list-all params level: "+params.get("level"));
        db.collection(collection)
                .whereIn("level", Arrays.asList(params.get("level")))
                .orderBy("created", Query.Direction.DESCENDING)
                .addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Question> questionArrayList = new ArrayList<>();
                        if (value != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                                Question qt = Question.getQuestionBySnapshot(queryDocumentSnapshot);
                                //for (int i = 0; i < 5; i++)
                                questionArrayList.add(qt);
                            }
                        }
                        iCallback.listCallBack(questionArrayList, tag);
                    }
                });
    }

    @Override
    public void getItemById(String id, final String tag) {
        db.collection(collection).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Question question = Question.getQuestionBySnapshot(documentSnapshot);
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void pushFakeData() {
        ArrayList<Question> questionArrayList = new ArrayList<>();
        questionArrayList.add(new Question(null,
                "hey didn't reach an agreement ______ their differences.", "B",
                "on account of", "due", "because", "owning", "text", "hard", -1));
        questionArrayList.add(new Question(null,
                "I'm very happy _____ in India. I really miss being there.", "A",
                "to live", "to have lived", "to be lived", "to be living", "text", "hard", -1));
        questionArrayList.add(new Question(null,
                "It is verry important for you... get well", "D",
                "for", "with", "in", "to", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "We have two ..... We see with them.", "A",
                "eyes", "legs", "hands", "ears", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "I am on my.... to the airport.", "B",
                "book", "street", "way", "tree", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "I am afraid ... studying English", "B",
                "to", "of", "for", "with", "text", "easy", -1));
        for (Question question : questionArrayList) {
            db.collection(collection).add(question.getDocData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("xxx", "doc " + documentReference.getId() + " added");
                }
            });
        }
    }
}
