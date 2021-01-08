package MainFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.multiple_choice.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import Interfaces.FragmentCommunicate;
import Defines.Question;
import Defines.QuestionFormData;
import Helpers.Helper;

public class AddQuestionDialogFragment extends DialogFragment {

    public static String fragmentName = "add-question-dialog";
    EditText edtQuestion, edtSpeech;
    ImageView pictureQuestion, imgAudio, imgText, imgPicture, imgSpeaker;
    RelativeLayout rltTextForm, rltPictureForm, textType, imageType, audioType, rltAudioForm;
    Dialog dialog;
    FragmentCommunicate fragmentCommunicate;

    boolean isAudioTypeClicked = false;
    boolean isPictureTypeClicked = false;
    boolean isTextTypeClicked = false;
    TextToSpeech tts;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // init
        initDialog();
        initTextToSpeech();

        // init form
        initForm();

        // init events
        initEvents();
        return dialog;
    }

    private void initTextToSpeech() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                }
            }
        });
    }

    private void initEvents() {
        initQuestionTypeClick();
        initPictureQuestionClick();
        initSpeakerClick();
        initEdtSpeechTextChanged();
    }

    private void initEdtSpeechTextChanged() {
        edtSpeech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSpeakerStatus();
            }
        });
    }

    private void updateSpeakerStatus() {
        String speech = edtSpeech.getText().toString();
        if (speech.trim() == "") {
            imgSpeaker.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.audio_icon_disabled));
        } else {
            imgSpeaker.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.black_audio_icon));
        }
    }

    private void initSpeakerClick() {
        imgSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = edtSpeech.getText().toString();
                if (speech.trim() != "") {
                    int speechStatus = tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("xxx", "Error in converting Text to Speech!");
                    }
                }
            }
        });
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
                if (!isTextTypeClicked) edtQuestion.setText(QuestionFormData.getQuestionText());
                onQuestionTypeClick("text");
            }
        });
        imageType.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPictureTypeClicked) setPictureQuestionIfExist();
                if (QuestionFormData.getQuestionImageUri() == null && QuestionFormData.getQuestionImageFilePath() == null) {
                    pickQuestionPicture();
                }
                onQuestionTypeClick("picture");
            }
        });
        audioType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAudioTypeClicked) edtSpeech.setText(QuestionFormData.getQuestionSpeech());
                onQuestionTypeClick("audio");
            }
        });
    }

    private void pickQuestionPicture() {
        dialog.dismiss();
        fragmentCommunicate.communicate("on-picture-type-click", AddQuestionDialogFragment.fragmentName);
    }

    private void onQuestionTypeClick(String type) {
        setIsTypeClicked(type);
        QuestionFormData.setQuestionType(type);
        solveFormVisibility();
        activeCurrentQuestionType(type);
    }

    private Question getQuestion() {
        return QuestionFormData.getQuestion();
    }

    private void initForm() {
        setIsTypeClicked(QuestionFormData.getQuestionType());
        activeCurrentQuestionType(QuestionFormData.getQuestionType());
        solveFormVisibility();
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                initTextForm();
                break;
            case "picture":
                initImageForm();
                break;
            case "audio":
                initAudioForm();
                break;
        }
    }

    private void solveFormVisibility() {
        switch (QuestionFormData.getQuestionType()) {
            case "text":
                rltTextForm.setVisibility(View.VISIBLE);
                rltPictureForm.setVisibility(View.GONE);
                rltAudioForm.setVisibility(View.GONE);
                break;
            case "picture":
                rltPictureForm.setVisibility(View.VISIBLE);
                rltTextForm.setVisibility(View.GONE);
                rltAudioForm.setVisibility(View.GONE);
                break;
            case "audio":
                rltAudioForm.setVisibility(View.VISIBLE);
                rltTextForm.setVisibility(View.GONE);
                rltPictureForm.setVisibility(View.GONE);
                break;
        }
    }

    // INIT FORM
    private void initTextForm() {
        edtQuestion.setText(QuestionFormData.getQuestionText());
        edtQuestion.requestFocus();
        Helper.showKeyboard(edtQuestion, getActivity());
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
        edtSpeech.setText(QuestionFormData.getQuestionSpeech());
        updateSpeakerStatus();
        edtQuestion.requestFocus();
        Helper.showKeyboard(edtSpeech, getActivity());
    }

    private void initDialog() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);

        // Setting dialog window
        Window window = dialog.getWindow();
        if (!QuestionFormData.getQuestionType().equals("picture")) window.setGravity(Gravity.BOTTOM);
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
        edtQuestion = (EditText) dialog.findViewById(R.id.edtQuestion);
        edtSpeech = (EditText) dialog.findViewById(R.id.edtSpeech);

        rltTextForm = (RelativeLayout) dialog.findViewById(R.id.textForm);
        rltPictureForm = (RelativeLayout) dialog.findViewById(R.id.pictureForm);
        rltAudioForm = (RelativeLayout) dialog.findViewById(R.id.audioForm);
        textType = (RelativeLayout) dialog.findViewById(R.id.textType);
        audioType = (RelativeLayout) dialog.findViewById(R.id.audioType);
        imageType = (RelativeLayout) dialog.findViewById(R.id.imageType);

        pictureQuestion = (ImageView) dialog.findViewById(R.id.pictureQuestion);
        imgAudio = (ImageView) dialog.findViewById(R.id.imgAudio);
        imgText = (ImageView) dialog.findViewById(R.id.imgText);
        imgPicture = (ImageView) dialog.findViewById(R.id.imgPicture);
        imgSpeaker = (ImageView) dialog.findViewById(R.id.imgSpeaker);

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
            case "audio":
                QuestionFormData.setQuestionSpeech(edtSpeech.getText().toString().trim());
                break;
        }
    }

    private void setIsTypeClicked(String type) {
        //solveTypeVisible(type);
        if (type.equals("text")) isTextTypeClicked = true;
        else if (type.equals("picture")) isPictureTypeClicked = true;
        else if (type.equals("audio")) isAudioTypeClicked = true;

    }

    private void activeCurrentQuestionType(String type) {
        switch (type) {
            case "text":
                imgText.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.blue_pencil_icon));
                imgAudio.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.audio_icon));
                imgPicture.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.picture_icon));
                break;
            case "picture":
                imgPicture.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.blue_picture_icon));
                imgText.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pencil_icon));
                imgAudio.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.audio_icon));
                break;
            case "audio":
                imgAudio.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.blue_audio_icon));
                imgPicture.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.picture_icon));
                imgText.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pencil_icon));
                break;
        }
    }
}