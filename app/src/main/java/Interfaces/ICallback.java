package Interfaces;

import java.util.ArrayList;

public interface ICallback <T> {
    public void itemCallBack(T item, String tag);
    public void listCallBack(ArrayList<T> items, String tag);
}
