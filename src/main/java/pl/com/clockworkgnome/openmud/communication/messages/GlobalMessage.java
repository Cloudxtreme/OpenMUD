package pl.com.clockworkgnome.openmud.communication.messages;


public class GlobalMessage {

    private String message;

    public GlobalMessage() {
    }

    public String getMessage() {
        return message;
    }

    public GlobalMessage(String message) {
        this.message = message;
    }
}
