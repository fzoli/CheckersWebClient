package mill.database.entity;

public class UserPasswordData {
    
    private String password;
    private int passwordLength;

    public UserPasswordData(String password, int passwordLength) {
        this.password = password;
        this.passwordLength = passwordLength;
    }

    public String getPassword() {
        return password;
    }

    public int getPasswordLength() {
        return passwordLength;
    }
    
}