package pl.com.clockworkgnome.openmud.domain;

import java.util.*;

public class Location {
    private int id;
    private String shortDescription;
    private String longDescription;
    private Map<Exit, Location> exits = new EnumMap<>(Exit.class);
    private List<Player> players = new ArrayList<>();
    private List<NPC> npcs = new ArrayList<>();

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

    public boolean doesExitExists(String exit) {
        for(Exit e : exits.keySet()) {
            if(exit.equalsIgnoreCase(e.exitString) || exit.equalsIgnoreCase(e.shortString)) {
               return true;
            }
        }
        return false;
    }

    public Location getNextLocation(String exit) {
        for(Exit e : exits.keySet()) {
            if(exit.equalsIgnoreCase(e.exitString) || exit.equalsIgnoreCase(e.shortString)) {
                return exits.get(e);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != location.id) return false;
        return shortDescription != null ? shortDescription.equals(location.shortDescription) : location.shortDescription == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        return result;
    }

    public void removeNPC(NPC npc) {
        this.npcs.remove(npc);
    }

    public void addNPC(NPC npc) {
        this.npcs.add(npc);
    }
}
