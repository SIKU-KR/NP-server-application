package core.dto.response;

import java.util.List;

public class ListUser {
    List<String> users;

    public ListUser(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}
