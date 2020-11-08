package Defines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.multiple_choice.R;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import Helpers.Helper;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MyUser> userArrayList;

    public UserAdapter(Context context, int layout, ArrayList<MyUser> userArrayList) {
        this.context = context;
        this.layout = layout;
        this.userArrayList = userArrayList;
    }


    @Override
    public int getCount() {
        return userArrayList.size();
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
        TextView txtUsername, txtEmail, txtEmailLabel, txtThumb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new UserAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            Helper.initFontAwesome(context, convertView);

            holder.txtUsername  = (TextView) convertView.findViewById(R.id.txtUsername);
            holder.txtEmail  = (TextView) convertView.findViewById(R.id.txtEmail);
            holder.txtEmailLabel  = (TextView) convertView.findViewById(R.id.txtEmailLabel);
            holder.txtThumb  = (TextView) convertView.findViewById(R.id.txtThumb);
            convertView.setTag(holder);
        }else{
            holder = (UserAdapter.ViewHolder) convertView.getTag();
        }

        MyUser user = userArrayList.get(position);
        holder.txtUsername.setText(user.getUsername());

        // email
        if (user.getEmail() == null){
            holder.txtEmail.setVisibility(View.GONE);
        }else{
            holder.txtEmail.setVisibility(View.VISIBLE);
            holder.txtEmail.setText(user.getEmail());
        }
        //
        String thumbText = Helper.getUppercaseFirstCharacter(user.getUsername());
        holder.txtThumb.setText(thumbText);
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        convertView.startAnimation(animScale);
        return convertView;
    }
}
