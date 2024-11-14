package core.dto.requestmsg;

public class UserLogin {
    private String username;

    public UserLogin(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
