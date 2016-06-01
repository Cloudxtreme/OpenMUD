package pl.com.clockworkgnome.openmud.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Location {
    private int id;
    private String shortDescription;
    private String longDescription;
    private Map<Exit, Location> exits = new EnumMap<>(Exit.class);
    private List<Player> players = new ArrayList<>();

    public Location(int id, String shortDescription, String longDescription) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void addExit(Exit exit, Location location) {
        this.exits.put(exit, location);
    }

    public Map<Exit, Location> getExits() {
        return exits;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setCurrentLocation(this);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public String getResponse(Player player) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Location\"");
        sb.append(",");
        sb.append("\"short\": \""+this.shortDescription+"\"");
        sb.append(",");
        sb.append("\"long\": \""+this.longDescription+"\"");
        sb.append("}");
        return sb.toString();
    }
}
