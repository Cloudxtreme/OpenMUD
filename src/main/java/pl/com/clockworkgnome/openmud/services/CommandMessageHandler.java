package pl.com.clockworkgnome.openmud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.com.clockworkgnome.openmud.domain.*;
import pl.com.clockworkgnome.openmud.messages.CommandMessage;

import java.util.List;
import java.util.Map;

@Service
public class CommandMessageHandler {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PlayersRepository playersRepository;


    public CommandMessage handleCommandMessage(CommandMessage message, String command, Player player) {
        switch(command) {
            case "LOOK":
                lookCommand(message, player);
                break;
            case "SAY":
                sayCommand(message, player);
                break;
            default:
                unknowCommand(message, command, player);
        }
        System.out.println("Command response: " + message.getResponse());
        return message;
    }

    private void unknowCommand(CommandMessage message, String command, Player player) {
        Map<Exit, Location> exits = player.getCurrentLocation().getExits();
        for(Exit e : exits.keySet()) {
            if(command.equalsIgnoreCase(e.exitString) || command.equalsIgnoreCase(e.shortString)) {
                Location newLocation = exits.get(e);
                Location oldLocation = player.getCurrentLocation();
                oldLocation.removePlayer(player);
                player.setCurrentLocation(newLocation);
                lookCommand(message,player);
                CommandMessage responseForPlayersEx = new CommandMessage(player.getName(),"LEAVES");
                responseForPlayersEx.setResponse(getSayOtherResponseLeaves(message,e.exitString));
                for(Player p : oldLocation.getPlayers()) {
                    template.convertAndSendToUser(p.getSessionId(),"/queue/private", responseForPlayersEx, createHeaders(p.getSessionId()));
                }
                break;
            }
        }
    }

    private void sayCommand(CommandMessage message, Player player) {
        message.setResponse(getSayResponse(message));
        List<Player> playersOnLocation = player.getCurrentLocation().getPlayers();
        CommandMessage responseForPlayers = new CommandMessage(player.getName(),"SAY");
        responseForPlayers.setResponse(getSayOtherResponse(message));
        for(Player p : playersOnLocation) {
            if(!p.equals(player)) {
                template.convertAndSendToUser(p.getSessionId(),"/queue/private", responseForPlayers, createHeaders(p.getSessionId()));
            }
        }
    }

    private void lookCommand(CommandMessage message, Player player) {
        message.setResponse(player.getCurrentLocation().getResponse(player));
    }

    private String getSayResponse(CommandMessage message) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\": \"You say: "+message.getPayload()+"\"");
        sb.append("}");
        return sb.toString();
    }

    private String getSayOtherResponse(CommandMessage message) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\":\""+ message.getPlayerName()+ " says: "+message.getPayload()+"\"");
        sb.append("}");
        return sb.toString();
    }

    private String getSayOtherResponseLeaves(CommandMessage message, String where) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Leaves\"");
        sb.append(",");
        sb.append("\"message\":\""+ message.getPlayerName()+ " goes " + where + "\"");
        sb.append("}");
        return sb.toString();
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }


}