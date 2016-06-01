package pl.com.clockworkgnome.openmud.communication.messages;

public class CommandMessage {

    private String playerName;

    private String response;

    private String command;

    public CommandMessage() {}

    public CommandMessage(String playerName, String command) {
        this.command = command;
        this.playerName = playerName;
    }

    public CommandMessage(String playerName, String command, String response) {
        this.response = response;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getResponse() {
        return response;
    }

    public String getCommand() {
        return command;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
