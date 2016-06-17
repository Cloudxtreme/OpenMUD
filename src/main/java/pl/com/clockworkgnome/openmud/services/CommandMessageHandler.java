package pl.com.clockworkgnome.openmud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.clockworkgnome.openmud.domain.*;
import pl.com.clockworkgnome.openmud.messages.CommandMessage;
import pl.com.clockworkgnome.openmud.util.CommandHandlerResponse;

@Service
public class CommandMessageHandler {

    @Autowired
    MessageJsonTransformer transformer;


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
                response = unknownCommand(command, player);
        }
        return response;
    }

    private CommandHandlerResponse unknownCommand(String command, Player player) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        final Location currentLocation = player.getCurrentLocation();

        if(currentLocation.doesExitExists(command)) {
            player.move(command);
            response.callerResponse = transformer.locationLook(player);
            response.playersOnLocationResponse = transformer.playerLeaves(player.getName(),command.toLowerCase());
        }
        return response;
    }

    private CommandHandlerResponse sayCommand(String text, String playerName) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        response.callerResponse = transformer.getSayResponse(text);
        response.playersOnLocationResponse = transformer.getSayOtherResponse(text, playerName);
        return response;
    }

    private CommandHandlerResponse lookCommand(Player player) {
        CommandHandlerResponse response = new CommandHandlerResponse();
        response.callerResponse = transformer.locationLook(player);
        return response;
    }

}
