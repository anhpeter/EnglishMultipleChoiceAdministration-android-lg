package Helpers;

import android.app.Fragment;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Interfaces.IMyStorage;

public class MyStorage {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    IMyStorage iMyStorage;
    public static MyStorage instance;

    public static MyStorage getInstance() {
        if (instance == null) {
            instance = new MyStorage();
            return instance;
        }
        return instance;
    }

    private MyStorage() {
    }

    public void uploadImage(Uri uri, final int code, Fragment fragment) {
        iMyStorage = (IMyStorage) fragment;
        if (uri != null) {
            String fileName = System.currentTimeMillis() + "";
            StorageReference ref = storageReference.child("images/" + fileName);
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    iMyStorage.uploadedCallback(url,code);
                                    //String ref = yourStorageReference.getName();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    //displaying the upload progress
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    iMyStorage.progressCallback(progress, code );
                }
            });
        }
    }

    public void delete(final String downloadUrl){
        StorageReference reference = storage.getReferenceFromUrl(downloadUrl);
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("xxx", "deleted: "+downloadUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("xxx", "delete file err: "+exception.getMessage());
            }
        });

    }
}
