package core.dto.response;

public class User {
    private String username;
    private Integer score;

    public User(String username, Integer score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public Integer getScore() {
        return score;
    }
}
