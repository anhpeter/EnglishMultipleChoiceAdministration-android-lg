package Models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;

import Defines.User;

public abstract class Model {
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected String collection;
    protected Activity activity;

    public Model(String collection, Activity activity){
        this.collection = collection;
        this.activity = activity;
    }

    // MANIPULATE DATA
    public void deleteCollection(){
        db.collection(collection).addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                    DocumentReference laRef = db.collection(collection).document(queryDocumentSnapshot.getId());
                    batch.delete(laRef);
                }
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("xxx", "collection "+collection+" deleted");
                    }
                });
            }
        });
    }

    // ABSTRACT METHODS
    abstract public void listAll(final HashMap<String, String> params, final String tag);

    abstract public void getItemById(String id, final String tag);

    // GETTER & SETTER
    public FirebaseFirestore getDb(){
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
