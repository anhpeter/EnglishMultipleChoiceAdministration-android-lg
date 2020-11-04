package Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.multiple_choice.R;

import Defines.FontManager;

public class Helper {

    public static void initFontAwesome(Context context, View v){
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(v.findViewById(R.id.containerLayout), iconFont);
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

}
