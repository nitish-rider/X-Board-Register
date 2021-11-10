package nks.register.lambda.model;

public class User {
    String Name;
    String email;
    String password;

    public User() {
    }

    public User(String name, String email, String password) {
        Name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
