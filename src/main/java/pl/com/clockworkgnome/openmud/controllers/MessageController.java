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
import pl.com.clockworkgnome.openmud.util.CommandHandlerResponse;

import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private CommandMessageHandler commandMessageHandler;


    @MessageMapping("/player.login")
    @SendToUser("/queue/private")
    public LoginMessage handleLogin(LoginMessage message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Login message from: " + message.getPlayerName());
        String sessionId = headerAccessor.getSessionId();
        initNewPlayer(message.getPlayerName(), sessionId);
        message.setResponse("You're logged in, type 'look' to look around.");
        return message;
    }

    @MessageMapping("/playerInput")
    @SendToUser("/queue/private")
    public CommandMessage handleCommand(CommandMessage message) {
        String command = message.getCommand();
        Player player = playersRepository.get(message.getPlayerName());
        System.out.println("Command message from: " + message.getPlayerName() + " command: " + command);
        CommandHandlerResponse rsp = commandMessageHandler.handleCommandMessage(message, command, player);
        if(rsp.callerResponse!=null  && !rsp.callerResponse.isEmpty()) {
            message.setResponse(rsp.callerResponse);
        }
        if(rsp.playersOnLocationResponse!=null && !rsp.playersOnLocationResponse.isEmpty()) {
            sendToOtherPlayersOnLocation(player, rsp);
        }
        return message;
    }

    private void sendToOtherPlayersOnLocation(Player player, CommandHandlerResponse rsp) {
        List<Player> playersOnLocation = player.getCurrentLocation().getPlayers();
        CommandMessage responseForPlayers = new CommandMessage(player.getName(),"RESPONSE");
        responseForPlayers.setResponse(rsp.playersOnLocationResponse);
        for(Player p : playersOnLocation) {
            if (!p.equals(player)) {
                template.convertAndSendToUser(p.getSessionId(), "/queue/private", responseForPlayers, createHeaders(p.getSessionId()));
            }
        }
    }

    @SendTo("/topic/global")
    public GlobalMessage handleGlobal(GlobalMessage message) {
        System.out.println("Global message " + message.getMessage());
        return message;
    }

    private void sendGlobalMessage(String message) {
        template.convertAndSend("/topic/global", new GlobalMessage(message));
    }

    private void initNewPlayer(String playerName, String sessionId) {
        playersRepository.initPlayer(playerName, sessionId);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
