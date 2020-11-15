package Models;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




import java.util.HashMap;

public abstract class Model {
    protected FirebaseDatabase realtimeDb = FirebaseDatabase.getInstance();
    protected String collection;
    protected Activity activity;

    public Model(String collection, Activity activity){
        this.collection = collection;
        this.activity = activity;
    }

    // MANIPULATE DATA
    // ABSTRACT METHODS
    abstract public void listAll(final HashMap<String, String> params, final String tag);

    abstract public void getItemById(String id, final String tag);

    // GETTER & SETTER

    public FirebaseDatabase getRealtimeDb(){
        return realtimeDb;
    }

    public DatabaseReference getCollectionRef(){
        return getRealtimeDb().getReference().child(collection);
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
