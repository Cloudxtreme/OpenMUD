package pl.com.clockworkgnome.openmud.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
            case "look":
                message.setResponse(player.getCurrentLocation().getResponse(player));
                break;
            default:
                message.setResponse("\"message\":\"Unknown command\"");
        }
        System.out.println("Command response: " + message.getResponse());
        return message;
    }

    @SendTo("/topic/global")
    public GlobalMessage handleGlobal(GlobalMessage message) {
        System.out.println("Global message " + message.getMessage());
        return message;
    }
}
