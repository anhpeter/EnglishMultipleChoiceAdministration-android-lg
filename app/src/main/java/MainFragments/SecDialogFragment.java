package MainFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.multiple_choice.R;

import Interfaces.FragmentCommunicate;
import Defines.QuestionFormData;

public class SecDialogFragment extends DialogFragment {

    public static String fragmentName = "sec-dialog";
    FragmentCommunicate fragmentCommunicate;
    Dialog dialog;

    Button btn5, btn10, btn15, btn20, btn30, btn60, btn90, btn120;
    Button[] btnSecArray;

    // WIDGET
    Button btnDone;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // init
        initDialog();
        btnSecArray = new Button[]{btn5, btn10, btn15, btn20, btn30, btn60, btn90, btn120};
        activeSecBtnBySecValue(QuestionFormData.getTimeLimit());
        initEvents();
        return dialog;
    }

    private void initEvents() {
        initBtnDoneClick();
        initBtnSecsClick();
    }

    private void initBtnSecsClick() {
        for (int i = 0; i < btnSecArray.length; i++) {
            Button btn = btnSecArray[i];
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    int secValue = getSecValueBySecContent(btn.getText().toString());
                    QuestionFormData.setTimeLimit(secValue);
                    activeSecBtnBySecValue(secValue);
                }
            });
        }
    }

    private void initBtnDoneClick() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initDialog() {
        fragmentCommunicate = (FragmentCommunicate) getActivity();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_sec_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // Setting dialog window
        Window window = dialog.getWindow();
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
        btnDone = (Button) dialog.findViewById(R.id.btnDone);

        btn5 = (Button) dialog.findViewById(R.id.btn5Sec);
        btn10 = (Button) dialog.findViewById(R.id.btn10Sec);
        btn15 = (Button) dialog.findViewById(R.id.btn15Sec);
        btn20 = (Button) dialog.findViewById(R.id.btn20Sec);
        btn30 = (Button) dialog.findViewById(R.id.btn30Sec);
        btn60 = (Button) dialog.findViewById(R.id.btn60Sec);
        btn90 = (Button) dialog.findViewById(R.id.btn90Sec);
        btn120 = (Button) dialog.findViewById(R.id.btn120Sec);
    }

    private void activeSecBtnBySecValue(int sec) {
        for (int i = 0; i < btnSecArray.length; i++) {
            Button btn = btnSecArray[i];
            int secValue = getSecValueBySecContent(btn.getText().toString());
            if (secValue == sec) {
                btn.setBackgroundColor(getActivity().getResources().getColor(R.color.success));
                btn.setTextColor(getActivity().getResources().getColor(R.color.white));
            } else {
                btn.setBackgroundResource(android.R.drawable.btn_default);
                btn.setTextColor(getActivity().getResources().getColor(R.color.black));
            }
        }
    }

    private int getSecValueBySecContent(String secContent) {
        int result = Integer.parseInt(secContent.replace(" sec", ""));
        return result;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        fragmentCommunicate.communicate("dismiss", SecDialogFragment.fragmentName);
    }
}