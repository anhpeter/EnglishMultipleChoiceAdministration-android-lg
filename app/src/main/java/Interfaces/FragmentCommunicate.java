package Interfaces;

import java.util.HashMap;

public interface FragmentCommunicate {
    public void communicate(HashMap<String, String> data, String fromFragment);
    public void communicate(String event, String fromFragment);
}
