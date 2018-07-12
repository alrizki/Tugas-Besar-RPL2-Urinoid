package urinoid.urinoid.database;

/**
 * Created by Saepul Uyun on 6/1/2018.
 */

public class Users {

    private String Email;
    private String Username;
    private String Password;
    private String ConfPassword;
    private String Nama;

    public Users() {
    }

    public Users(String email, String username, String password, String nama, String confPassword) {
        Email = email;
        Username = username;
        Password = password;
        ConfPassword = confPassword;
        Nama = nama;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getconfPassword() {
        return ConfPassword;
    }

    public void setconfPassword(String confPassword) {
        ConfPassword = confPassword;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
}
