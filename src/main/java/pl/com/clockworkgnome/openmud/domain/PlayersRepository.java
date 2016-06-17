package pl.com.clockworkgnome.openmud.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PlayersRepository {

    LocationRepository locationRepository;

    private Map<String,Player> players = new HashMap<>();

    public void add(Player player) {
        this.players.put(player.getName(),player);
    }

    public Player get(String playerName) {
        return players.get(playerName);
    }

    public Player initPlayer(String playerName, String sessionId) {
        Player player = new Player(playerName,sessionId);
        add(player);
        Location starting = locationRepository.getStartingLocation();
        starting.addPlayer(player);
        return player;
    }

    @Autowired
    public void setLocationRepository(LocationRepository lr) {
        this.locationRepository = lr;
    }
}
