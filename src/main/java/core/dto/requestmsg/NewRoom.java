package core.dto.requestmsg;

public class NewRoom {
    private String creator;
    private String title;

    public NewRoom() {
    }

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
