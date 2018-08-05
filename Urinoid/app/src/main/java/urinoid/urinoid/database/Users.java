package urinoid.urinoid.database;

/**
 * Created by Saepul Uyun on 6/1/2018.
 */

public class Users {

    private String Username;
    private String Password;
    private String ConfPassword;
    private String Nama;

    public Users() {
    }

    public Users(String username, String password, String confPassword, String nama) {
        Username = username;
        Password = password;
        ConfPassword = confPassword;
        Nama = nama;
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

    public String getConfPassword() {
        return ConfPassword;
    }

    public void setConfPassword(String confPassword) {
        ConfPassword = confPassword;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
}
