package pl.com.clockworkgnome.openmud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.com.clockworkgnome.openmud.messages.CommandMessage;
import pl.com.clockworkgnome.openmud.messages.GlobalMessage;
import pl.com.clockworkgnome.openmud.messages.LoginMessage;
import pl.com.clockworkgnome.openmud.domain.*;
import pl.com.clockworkgnome.openmud.services.CommandMessageHandler;

import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private CommandMessageHandler commandMessageHandler;


    @MessageMapping("/player.login")
    @SendToUser("/queue/private")
    public LoginMessage handleLogin(LoginMessage message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Login message from: " + message.getPlayerName());
        String sessionId = headerAccessor.getSessionId();
        initNewPlayer(message, sessionId);
        return message;
    }

    @MessageMapping("/playerInput")
    @SendToUser("/queue/private")
    public CommandMessage handleCommand(CommandMessage message) {
        String command = message.getCommand();
        Player player = playersRepository.get(message.getPlayerName());
        System.out.println("Command message from: " + message.getPlayerName() + " command: " + command);
        return commandMessageHandler.handleCommandMessage(message, command, player);
    }

    @SendTo("/topic/global")
    public GlobalMessage handleGlobal(GlobalMessage message) {
        System.out.println("Global message " + message.getMessage());
        return message;
    }

    private void sendGlobalMessage(String message) {
        template.convertAndSend("/topic/global", new GlobalMessage(message));
    }

    private void initNewPlayer(LoginMessage message, String sessionId) {
        String playerName = message.getPlayerName();
        Player newPlayer = playersRepository.initPlayer(playerName, sessionId);
        message.setResponse(newPlayer.getCurrentLocation().getResponse(newPlayer));
    }
}
