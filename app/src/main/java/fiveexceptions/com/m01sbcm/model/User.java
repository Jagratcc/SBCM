package fiveexceptions.com.m01sbcm.model;


/**
 * Created by amit on 9/4/16.
 */
public class User  {

    int userId = 0;
    String userName = "";
    String displayName = "";
    String mobiLeNumber ="";
    String password = "";
    String email = "";
    // 1 for gmail : 2 for facebook  : 0 for custom
    int userType = 0;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobiLeNumber() {
        return mobiLeNumber;
    }

    public void setMobiLeNumber(String mobiLeNumber) {
        this.mobiLeNumber = mobiLeNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getUserType() {
        return userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "displayName='" + displayName + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", mobiLeNumber='" + mobiLeNumber + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }
}
