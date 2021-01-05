package MainFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.multiple_choice.R;

public class AddQuestionDialogFragment extends DialogFragment {

    EditText edtQuestion;
    RelativeLayout rltTextForm, textType, pictureType, audioType;
    FrameLayout frameVoiceForm;
    Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);
        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // set width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        // mapping...
        mapping();

        // events...
        //dialog.dismiss();
        return dialog;
    }

    private void mapping() {
        rltTextForm = (RelativeLayout) dialog.findViewById(R.id.textForm);
        edtQuestion = (EditText) dialog.findViewById(R.id.edtQuestion);
        textType = (RelativeLayout) dialog.findViewById(R.id.textType);
        audioType = (RelativeLayout) dialog.findViewById(R.id.audioType);
        pictureType = (RelativeLayout) dialog.findViewById(R.id.pictureType);
    }
}