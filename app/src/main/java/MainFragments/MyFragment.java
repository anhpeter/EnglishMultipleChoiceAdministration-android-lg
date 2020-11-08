package MainFragments;

import com.example.multiple_choice.Activity;

public abstract class MyFragment extends android.app.Fragment {

    public Activity getCalledActivity(){
        return ((Activity)getActivity());
    }
}
