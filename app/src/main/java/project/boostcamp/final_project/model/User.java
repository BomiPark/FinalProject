package project.boostcamp.final_project.model;

/**
 * Created by qkrqh on 2017-08-17.
 */

public class User {

    private String email;
    private String pwd;

    public User(){}

    public User(String email, String pwd){
        this.email = email;
        this.pwd = pwd;
    }

    public User(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
