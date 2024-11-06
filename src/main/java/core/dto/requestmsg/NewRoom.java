package core.dto.requestmsg;

import java.io.Serializable;

public class NewRoom implements Serializable {
    private String creator;
    private String title;

    public NewRoom(String creator, String title) {
        this.creator = creator;
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }
}
