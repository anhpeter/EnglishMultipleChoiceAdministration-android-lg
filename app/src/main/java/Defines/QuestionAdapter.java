package Defines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.multiple_choice.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;

    public QuestionAdapter(Context context, int layout, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionArrayList = questionArrayList;
    }

    private ArrayList<Question> questionArrayList;

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

    class ViewHolder{
        TextView txtQuestion, txtQuestionType, txtThumb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtQuestion  = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.txtQuestionType  = (TextView) convertView.findViewById(R.id.txtQuestionType);
            holder.txtThumb  = (TextView) convertView.findViewById(R.id.txtThumb);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Question question = questionArrayList.get(position);
        holder.txtQuestion.setText(question.getQuestion());
        holder.txtQuestionType.setText("Type: "+question.getQuestionType());
        String thumbText = (question.getIsImageQuestion()) ? "P" : getUppercaseFirstCharacter(question.getQuestion());
        holder.txtThumb.setText(thumbText);
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        convertView.startAnimation(animScale);
        return convertView;
    }

    private String getUppercaseFirstCharacter(String string){
        string = (string!=null) ? string : "";
        if (string.trim()!="") return (string.charAt(0)+"").toUpperCase();
        return string;
    }
}
