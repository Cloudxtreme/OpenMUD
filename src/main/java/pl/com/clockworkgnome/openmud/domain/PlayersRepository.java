package pl.com.clockworkgnome.openmud.domain;

import org.springframework.stereotype.Repository;
import pl.com.clockworkgnome.openmud.domain.Player;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PlayersRepository {

    private Map<Integer,Player> players = new HashMap<>();

    public void add(Player player) {
        this.players.put(player.getId(),player);
    }
}
