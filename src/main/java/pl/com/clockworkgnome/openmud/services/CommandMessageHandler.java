package pl.com.clockworkgnome.openmud.services;

import org.springframework.stereotype.Service;
import pl.com.clockworkgnome.openmud.domain.*;
import pl.com.clockworkgnome.openmud.messages.CommandMessage;
import pl.com.clockworkgnome.openmud.util.CommandHandlerResponse;

@Service
public class CommandMessageHandler {


    public CommandHandlerResponse handleCommandMessage(CommandMessage message, String command, Player player) {
        CommandHandlerResponse response;
        switch(command) {
            case "LOOK":
                response = lookCommand(player);
                break;
            case "SAY":
                response = sayCommand(message.getPayload(), player.getName());
                break;
            default:
                response = unknowCommand(command, player);
        }
        return response;
    }

    private CommandHandlerResponse unknowCommand(String command, Player player) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        final Location currentLocation = player.getCurrentLocation();

        if(currentLocation.doesExitExists(command)) {
            movePlayerToNewLocation(command, player, currentLocation);
            response.callerResponse = lookCommand(player).callerResponse;
            response.playersOnLocationResponse = getSayOtherResponseLeaves(player.getName(),command.toLowerCase());
        }
        return response;
    }

    private void movePlayerToNewLocation(String direction, Player player, Location currentLocation) {
        Location newLocation = currentLocation.getNextLocation(direction);
        currentLocation.removePlayer(player);
        player.setCurrentLocation(newLocation);
    }

    private CommandHandlerResponse sayCommand(String text, String playerName) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        response.callerResponse = getSayResponse(text);
        response.playersOnLocationResponse = getSayOtherResponse(text, playerName);
        return response;
    }

    private CommandHandlerResponse lookCommand(Player player) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        response.callerResponse = player.getCurrentLocation().getResponse(player);
        return response;
    }

    private String getSayResponse(String text) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\": \"You say: "+text+"\"");
        sb.append("}");
        return sb.toString();
    }

    private String getSayOtherResponse(String text, String playerName) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\":\""+ playerName + " says: "+ text +"\"");
        sb.append("}");
        return sb.toString();
    }

    private String getSayOtherResponseLeaves(String playerName, String where) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Leaves\"");
        sb.append(",");
        sb.append("\"message\":\""+ playerName + " goes " + where + "\"");
        sb.append("}");
        return sb.toString();
    }
}
