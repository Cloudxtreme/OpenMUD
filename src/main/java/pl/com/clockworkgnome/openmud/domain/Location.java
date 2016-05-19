package pl.com.clockworkgnome.openmud.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Location {
    private int id;
    private String shortDescription;
    private String longDescirption;
    private Map<Exit, Location> exits = new EnumMap<>(Exit.class);
    private List<Player> players = new ArrayList<>();

    public Location(int id, String shortDescription, String longDescription) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescirption = longDescription;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescirption() {
        return longDescirption;
    }

    public void addExit(Exit exit, Location location) {
        this.exits.put(exit, location);
    }

    public Map<Exit, Location> getExits() {
        return exits;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
