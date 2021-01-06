package MainFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.multiple_choice.R;

import Defines.FragmentCommunicate;
import Defines.Question;
import Defines.QuestionFormData;

public class AddQuestionDialogFragment extends DialogFragment {

    public static String fragmentName = "add-question-dialog";
    EditText edtQuestion;
    RelativeLayout rltTextForm, textType, imageType, audioType;
    FrameLayout frameVoiceForm;
    Dialog dialog;
    FragmentCommunicate fragmentCommunicate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // init
        initDialog();

        // init form
        initForm();

        // init events
        initEvents();
        return dialog;
    }

    private void initEvents() {
        initQuestionTypeClick();
    }

    private void initQuestionTypeClick() {
        textType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuestionTypeClick("text");
            }
        });
        imageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuestionTypeClick("picture");
            }
        });
        audioType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuestionTypeClick("audio");
            }
        });
    }

    private void onQuestionTypeClick(String type) {
        QuestionFormData.setQuestionType(type);
    }

    private Question getQuestion() {
        return QuestionFormData.getQuestion();
    }

    private void initForm() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                Toast.makeText(getActivity(), "text question", Toast.LENGTH_SHORT).show();
                initTextForm();
                break;
            case "picture":
                Toast.makeText(getActivity(), "image question", Toast.LENGTH_SHORT).show();
                initImageForm();
                break;
            case "audio":
                Toast.makeText(getActivity(), "audio question", Toast.LENGTH_SHORT).show();
                initAudioForm();
                break;
        }
    }

    private void initTextForm() {
        edtQuestion.setText(QuestionFormData.getQuestionText());
        edtQuestion.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        if (getQuestion() != null) {
        } else {

        }
    }

    private void initImageForm() {

    }

    private void initAudioForm() {

    }

    private void initEditForm() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                showKeyboard();
                edtQuestion.setText(QuestionFormData.getQuestionText());
                break;
            case "picture":
                break;
            case "audio":
                break;
        }
    }

    private void initAddForm() {

    }

    private void showKeyboard() {
        edtQuestion.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void initDialog() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);

        // Setting dialog window
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // set width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        // mapping
        mapping();
    }

    private void mapping() {
        rltTextForm = (RelativeLayout) dialog.findViewById(R.id.textForm);
        edtQuestion = (EditText) dialog.findViewById(R.id.edtQuestion);
        textType = (RelativeLayout) dialog.findViewById(R.id.textType);
        audioType = (RelativeLayout) dialog.findViewById(R.id.audioType);
        imageType = (RelativeLayout) dialog.findViewById(R.id.imageType);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        setDataWhenDismiss();
        fragmentCommunicate.communicate("dismiss", AddQuestionDialogFragment.fragmentName);
    }

    private void setDataWhenDismiss() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                QuestionFormData.setQuestionText(edtQuestion.getText().toString().trim());
                break;
            case "picture":
            case "audio":
                break;
        }
    }
}