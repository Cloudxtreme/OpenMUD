package pl.com.clockworkgnome.openmud.domain;

import java.util.EnumMap;
import java.util.Map;

public class Location {
    private int id;
    private String shortDescription;
    private String longDescirption;
    private Map<Exit, Location> exits = new EnumMap<>(Exit.class);

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
}
