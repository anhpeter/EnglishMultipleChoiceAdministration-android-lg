package Defines;

import com.google.firebase.firestore.DocumentSnapshot;

import Helpers.Helper;

public class MyUser {
    private String id;
    private String username;
    private String password;
    private boolean isAdmin;
    private String email;

    private String fullName;
    private long created;


    public MyUser(String id, String username, String password, boolean isAdmin, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    //
    /*
    private String id;
    private String username;
    private String password;
    private boolean isAdmin;
    private String email;
    private String fullName;
    private long created;
     */
    public static MyUser getUserBySnapshot(DocumentSnapshot queryDocumentSnapshot){
        if (queryDocumentSnapshot != null){
            String id = queryDocumentSnapshot.getId();
            String username = queryDocumentSnapshot.getString("username");
            String password = queryDocumentSnapshot.getString("password");
            boolean isAdmin = Helper.getBooleanBySnapshot(queryDocumentSnapshot, "isAdmin", false);
            String email = Helper.getStringBySnapshot(queryDocumentSnapshot, "email", null);
            String fullName = Helper.getStringBySnapshot(queryDocumentSnapshot, "fullName", null);
            //long created  = Long.parseLong(Helper.getStringBySnapshot(queryDocumentSnapshot, "created", "-1"));
            MyUser user  = new MyUser(id, username, password, isAdmin, email);
            return user;
        }else return null;
    }

    // GETTER AND SETTER
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

}
