package pl.com.clockworkgnome.openmud.communication.messages;

public class LoginMessage {

    private String playerName;

    private String response;

    public LoginMessage() {}

    public LoginMessage(String playerName) {
        this.playerName = playerName;
    }

    public LoginMessage(String playerName, String response) {
        this.response = response;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

