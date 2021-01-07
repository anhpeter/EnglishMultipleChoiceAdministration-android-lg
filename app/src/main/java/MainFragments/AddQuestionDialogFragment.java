package MainFragments;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.multiple_choice.R;
import com.squareup.picasso.Picasso;

import Defines.FragmentCommunicate;
import Defines.IntentCode;
import Defines.Question;
import Defines.QuestionFormData;
import Helpers.Helper;

public class AddQuestionDialogFragment extends DialogFragment {

    public static String fragmentName = "add-question-dialog";
    EditText edtQuestion;
    ImageView pictureQuestion;
    RelativeLayout rltTextForm, rltPictureForm, textType, imageType, audioType;
    FrameLayout frameVoiceForm;
    Dialog dialog;
    FragmentCommunicate fragmentCommunicate;

    boolean isAudioTypeClicked = false;
    boolean isPictureTypeClicked = false;
    boolean isTextTypeClicked = false;

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
        initPictureQuestionClick();
    }

    private void initPictureQuestionClick() {
        pictureQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickQuestionPicture();
            }
        });
    }

    private void initQuestionTypeClick() {
        textType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuestionTypeClick("text");
                if (!isTextTypeClicked) edtQuestion.setText(QuestionFormData.getQuestionText());
            }
        });
        imageType.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onQuestionTypeClick("picture");
                if (!isPictureTypeClicked) setPictureQuestionIfExist();
                if (QuestionFormData.getQuestionImageUri() == null && QuestionFormData.getQuestionImageFilePath() == null) {
                    pickQuestionPicture();
                }
            }
        });
        audioType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuestionTypeClick("audio");
            }
        });
    }

    private void pickQuestionPicture() {
        dialog.dismiss();
        fragmentCommunicate.communicate("on-picture-type-click", AddQuestionDialogFragment.fragmentName);
    }

    private void onQuestionTypeClick(String type) {
        QuestionFormData.setQuestionType(type);
        solveFormVisibility();
    }

    private Question getQuestion() {
        return QuestionFormData.getQuestion();
    }

    private void initForm() {
        setIsTypeClicked(QuestionFormData.getQuestionType());
        solveFormVisibility();
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

    private void solveFormVisibility() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                rltTextForm.setVisibility(View.VISIBLE);
                rltPictureForm.setVisibility(View.GONE);
                frameVoiceForm.setVisibility(View.GONE);
                break;
            case "picture":
                rltPictureForm.setVisibility(View.VISIBLE);
                rltTextForm.setVisibility(View.GONE);
                frameVoiceForm.setVisibility(View.GONE);
                break;
            case "audio":
                frameVoiceForm.setVisibility(View.VISIBLE);
                rltTextForm.setVisibility(View.GONE);
                rltPictureForm.setVisibility(View.GONE);
                break;
        }
    }

    // INIT FORM
    private void initTextForm() {
        edtQuestion.setText(QuestionFormData.getQuestionText());
        edtQuestion.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void initImageForm() {
        setPictureQuestionIfExist();
    }

    private void setPictureQuestionIfExist() {
        if (QuestionFormData.getQuestionImageUri() != null) {
            Helper.setImageViewImageByUri(getActivity(), pictureQuestion, QuestionFormData.getQuestionImageUri());
        } else if (QuestionFormData.getQuestionImageFilePath() != null) {
            Picasso.get().load(QuestionFormData.getQuestionImageFilePath()).into(pictureQuestion);
        }
    }

    private void initAudioForm() {

    }

    private void initEditForm() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                Helper.showKeyboard(edtQuestion, getActivity());
                edtQuestion.setText(QuestionFormData.getQuestionText());
                break;
            case "picture":
                break;
            case "audio":
                break;
        }
    }
    //

    private void initDialog() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);

        // Setting dialog window
        Window window = dialog.getWindow();
        if (QuestionFormData.getQuestionType().equals("text")) window.setGravity(Gravity.BOTTOM);
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
        rltPictureForm = (RelativeLayout) dialog.findViewById(R.id.pictureForm);
        edtQuestion = (EditText) dialog.findViewById(R.id.edtQuestion);
        textType = (RelativeLayout) dialog.findViewById(R.id.textType);
        audioType = (RelativeLayout) dialog.findViewById(R.id.audioType);
        imageType = (RelativeLayout) dialog.findViewById(R.id.imageType);
        pictureQuestion = (ImageView) dialog.findViewById(R.id.pictureQuestion);
        frameVoiceForm = (FrameLayout) dialog.findViewById(R.id.frameVoiceForm);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        setDataWhenDismiss();
        fragmentCommunicate.communicate("dismiss", AddQuestionDialogFragment.fragmentName);
        Helper.hideKeyboard(getActivity());
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

    private void setIsTypeClicked(String type) {
        if (type.equals("text")) isTextTypeClicked = true;
        else if (type.equals("picture")) isPictureTypeClicked = true;
        else if (type.equals("audio")) isAudioTypeClicked = true;
    }
}