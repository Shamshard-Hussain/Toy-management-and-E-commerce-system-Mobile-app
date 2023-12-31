package toy;

import java.io.Serializable;

public class Account implements Serializable {
    private String Id;
    private String Username;
    private String Email;
    private String TpNumber;
    private String Password;

    public Account() {
    }

    public Account(String id, String username, String email, String tpNumber, String password) {
        Id = id;
        Username = username;
        Email = email;
        TpNumber = tpNumber;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTpNumber() {
        return TpNumber;
    }

    public void setTpNumber(String tpNumber) {
        TpNumber = tpNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
