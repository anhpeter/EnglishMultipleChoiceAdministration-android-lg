package Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.multiple_choice.R;
import com.google.firebase.firestore.DocumentSnapshot;

import Defines.FontManager;

public class Helper {

    // FONT AWESOME
    public static void initFontAwesome(Context context, View v){
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(v.findViewById(R.id.containerLayout), iconFont);
    }

    // TEMPLATE
    public static void solveListMessage(boolean isListEmpty, ListView lv, TextView txtMessage, String message){
        if (!isListEmpty) {
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
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void clearEditTextFocus(EditText edt){
        edt.setFocusableInTouchMode(false);
        edt.setFocusable(false);
        edt.setFocusableInTouchMode(true);
        edt.setFocusable(true);
    }

    //
    public static String getStringBySnapshot(DocumentSnapshot snapshot, String key, String defaultValue){
        try{
            return snapshot.getString(key);
        }catch(Exception e){
            return defaultValue;
        }
    }

    public static boolean getBooleanBySnapshot(DocumentSnapshot snapshot, String key, boolean defaultValue){
        try{
            return snapshot.getBoolean(key);
        }catch(Exception e){
            return defaultValue;
        }
    }

    public static String getUppercaseFirstCharacter(String string){
        string = (string!=null) ? string : "";
        if (string.trim()!="") return (string.charAt(0)+"").toUpperCase();
        return string;
    }

}
