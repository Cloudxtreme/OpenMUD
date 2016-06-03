package pl.com.clockworkgnome.openmud.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.com.clockworkgnome.openmud.communication.messages.CommandMessage;
import pl.com.clockworkgnome.openmud.communication.messages.GlobalMessage;
import pl.com.clockworkgnome.openmud.communication.messages.LoginMessage;
import pl.com.clockworkgnome.openmud.domain.Location;
import pl.com.clockworkgnome.openmud.domain.LocationRepository;
import pl.com.clockworkgnome.openmud.domain.Player;
import pl.com.clockworkgnome.openmud.domain.PlayersRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PlayersRepository playersRepository;


    @MessageMapping("/player.login")
    @SendToUser("/queue/private")
    public LoginMessage handleLogin(LoginMessage message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Login message from: " + message.getPlayerName());
        String sessionId = headerAccessor.getSessionId();
        Player player = new Player(message.getPlayerName(),sessionId);
        // /queue/private{sessionId}
        playersRepository.add(player);
        Location starting = locationRepository.getStartingLocation();
        starting.addPlayer(player);
        message.setResponse(starting.getResponse(player));
        template.convertAndSend("/topic/global", new GlobalMessage("New player connected: " + message.getPlayerName()));
        return message;
    }

    @MessageMapping("/playerInput")
    @SendToUser("/queue/private")
    public CommandMessage handleCommand(CommandMessage message) {
        System.out.println("Command message from: " + message.getPlayerName() + " command: " + message.getCommand());
        Player player = playersRepository.get(message.getPlayerName());
        switch(message.getCommand()) {
            case "LOOK":
                message.setResponse(player.getCurrentLocation().getResponse(player));
                break;
            case "SAY":
                message.setResponse(getSayResponse(message));
                List<Player> playersOnLocation = player.getCurrentLocation().getPlayers();
                CommandMessage responseForPlayers = new CommandMessage(player.getName(),"SAY");
                responseForPlayers.setResponse(getSayOtherResponse(message));
                for(Player p : playersOnLocation) {
                    if(!p.equals(player)) {
                        template.convertAndSendToUser(p.getSessionId(),"/queue/private", responseForPlayers, createHeaders(p.getSessionId()));
                    }
                }
                break;
            default:
                message.setResponse("\"message\":\"Unknown command\"");
        }
        System.out.println("Command response: " + message.getResponse());
        return message;
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

    @SendTo("/topic/global")
    public GlobalMessage handleGlobal(GlobalMessage message) {
        System.out.println("Global message " + message.getMessage());
        return message;
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
