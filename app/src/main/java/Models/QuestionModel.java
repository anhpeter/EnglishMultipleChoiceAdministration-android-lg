package Models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.ICallback;
import Defines.Question;
import Defines.User;
import MainFragments.QuestionIndexFragment;

public class QuestionModel extends Model {

    ICallback<Question> iCallback;

    public QuestionModel(Activity activity, QuestionIndexFragment questionIndexFragment){
        super("questions", activity);
        iCallback = (ICallback<Question>) questionIndexFragment;
    }

    public void listAll(final String tag){
        db.collection(collection).addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Question> questionArrayList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                    Question qt = Question.getQuestionByQueryDoc(queryDocumentSnapshot);;
                    for (int i = 0; i<5; i++) questionArrayList.add(qt);
                }
                iCallback.listCallBack(questionArrayList, tag);
            }
        });
    }

    public void pushFakeData(){
        ArrayList<Question> questionArrayList = new ArrayList<>();
        questionArrayList.add(new Question("It is verry important for you... get well", "D", "for", "with", "in", "to", "text", "easy", -1));
        questionArrayList.add(new Question("We have two ..... We see with them.", "A", "eyes", "legs", "hands", "ears", "text", "easy", -1));
        questionArrayList.add(new Question("I am on my.... to the airport.", "B", "book", "street", "way", "tree", "text", "easy", -1));
        questionArrayList.add(new Question("I am afraid ... studying English", "B", "to", "of", "for", "with", "text", "easy", -1));
        for (Question question:questionArrayList){
            db.collection(collection).add(question.getDocData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("xxx", "doc "+documentReference.getId()+" added");
                }
            });
        }
    }
}
