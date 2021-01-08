package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.multiple_choice.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Defines.Question;
import Helpers.Helper;

public class QuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Question> questionArrayList;

    public QuestionAdapter(Context context, int layout, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionArrayList = questionArrayList;
    }


    @Override
    public int getCount() {
        return questionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView txtQuestion, txtAnswerType, txtThumb, txtTimeLimit;
        ImageView imgThumb, imgQuestionType, imgAnswerType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new QuestionAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            mapping(holder, convertView);
            convertView.setTag(holder);
        } else holder = (QuestionAdapter.ViewHolder) convertView.getTag();

        Question question = questionArrayList.get(position);
        holder.txtQuestion.setText(question.getQuestion());
        holder.txtAnswerType.setText("Type: " + question.getAnswerType());
        solveThumb(holder, question);
        showTypeIcon(holder.imgQuestionType, question.getQuestionType());
        showTypeIcon(holder.imgAnswerType, question.getAnswerType());
        //holder.txtTimeLimit.setText(question.getTimeLimit()+" sec");
        animate(convertView);
        return convertView;
    }

    private void showTypeIcon(ImageView img, String type) {
        switch (type) {
            case "text":
                img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pencil_icon));
                break;
            case "picture":
                img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.picture_icon));
                break;
            case "audio":
                img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.audio_icon));
                break;
            case "voice":
                img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mic));
                break;
        }
    }

    private void animate(View v) {
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        v.startAnimation(animScale);
    }

    private void solveThumb(ViewHolder holder, Question question) {
        solveThumbVisibility(holder, question);
        if (question.getIsImageQuestion()) {
            Picasso.get().load(question.getQuestion()).into(holder.imgThumb);
        } else {
            String thumbText = (question.getIsImageQuestion()) ? "P" : Helper.getUppercaseFirstCharacter(question.getQuestion().trim());
            holder.txtThumb.setText(thumbText);
        }
    }

    private void mapping(ViewHolder holder, View v) {
        holder.txtQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        holder.txtAnswerType = (TextView) v.findViewById(R.id.txtAnswerType);
        holder.txtThumb = (TextView) v.findViewById(R.id.txtThumb);
        holder.imgThumb = (ImageView) v.findViewById(R.id.imgThumb);
        holder.imgQuestionType = (ImageView) v.findViewById(R.id.imgQuestionType);
        holder.imgAnswerType = (ImageView) v.findViewById(R.id.imgAnswerType);
        //holder.txtTimeLimit = (TextView) v.findViewById(R.id.txtTimeLimit);
    }

    private void solveThumbVisibility(ViewHolder holder, Question question) {
        if (question.getIsImageQuestion()) {
            holder.imgThumb.setVisibility(View.VISIBLE);
            holder.txtThumb.setVisibility(View.GONE);
        } else {
            holder.txtThumb.setVisibility(View.VISIBLE);
            holder.imgThumb.setVisibility(View.GONE);
        }
    }
}
