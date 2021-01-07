package Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.multiple_choice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;

import Defines.FontManager;

public class Helper {

    // FONT AWESOME
    public static void initFontAwesome(Context context, View v) {
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(v.findViewById(R.id.containerLayout), iconFont);
    }

    // TEMPLATE
    public static void solveListMessage(boolean isShowMessage, ListView lv, TextView txtMessage, String message) {
        if (!isShowMessage) {
            txtMessage.setText("");
            txtMessage.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.GONE);
            txtMessage.setText(message);
            txtMessage.setVisibility(View.VISIBLE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("xxx", "Failed to hide keyboard:" + e.getMessage());
        }
    }

    public static void clearEditTextFocus(EditText edt) {
        edt.setFocusableInTouchMode(false);
        edt.setFocusable(false);
        edt.setFocusableInTouchMode(true);
        edt.setFocusable(true);
    }

    //
    public static String getStringBySnapshot(DocumentSnapshot snapshot, String key, String defaultValue) {
        try {
            return snapshot.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanBySnapshot(DocumentSnapshot snapshot, String key, boolean defaultValue) {
        try {
            return snapshot.getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanByDataSnapshot(DataSnapshot snapshot, String key, boolean defaultValue) {
        try {
            String booleanStr = snapshot.child(key).getValue().toString();
            if (booleanStr.equals("true")) return true;
            else return false;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStringByDataSnapshot(DataSnapshot snapshot, String key, String defaultValue) {
        try {
            return snapshot.child(key).getValue().toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getUppercaseFirstCharacter(String string) {
        string = (string != null) ? string : "";
        if (string.trim() != "") return (string.charAt(0) + "").toUpperCase();
        return string;
    }

    public static int getRandom(int min, int max) {
        return (int) (Math.floor(min + Math.random() * (max - min)));
    }

    public static String ucFirst(String value) {
        String result = null;
        if (value != null) {
            result = value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        return result;
    }

    public static long getTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static int arrayIndexOf(String[] array, String value) {
        return java.util.Arrays.asList(array).indexOf(value);
    }

    public static void showKeyboard(EditText edt, Activity activity) {
        try {
            edt.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            Log.d("xxx", "Failed to show keyboard:" + e.getMessage());
        }
    }

    public static void setImageViewImageByUri(Activity activity, ImageView img, Uri uri) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
