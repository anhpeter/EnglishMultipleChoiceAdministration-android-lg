package Defines;

import android.util.Log;

import Helpers.MyStorage;

public class Auth {
    private MyUser user;

    public static Auth instance;

    private Auth() {
    }

    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
            return instance;
        }
        return instance;
    }

    public boolean loginByUsernameAndPassword(String username, String password) {
        if (username.equals("admin") && password.equals("1234")) {
            MyUser user = new MyUser(null, "admin", "1234", true, "");
            setUser(user);
            return true;
        } else {
            return false;
        }
    }

    public void setUserByUsername(String username){
        setUser(getUserByUsername(username));
    }

    public MyUser getUserByUsername(String username) {
        MyUser user = null;
        if (username.equals("admin")) {
            user = new MyUser(null, "admin", "1234", true, "");
        }
        return user;
    }

    public boolean isLogged() {
        if (getUser() != null) return true;
        else {
            return false;
        }

    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

}
