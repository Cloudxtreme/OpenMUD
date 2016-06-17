package pl.com.clockworkgnome.openmud.domain;

import java.util.Objects;

public class Player {

    private String name;
    private Location currentLocation;
    private String sessionId;

    public Player(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public String getSessionId() { return sessionId; }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation=currentLocation;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void move(String direction) {
        Location newLocation = currentLocation.getNextLocation(direction);
        currentLocation.removePlayer(this);
        newLocation.addPlayer(this);
    }
}
