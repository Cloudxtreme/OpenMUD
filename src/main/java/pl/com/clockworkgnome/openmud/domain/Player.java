package pl.com.clockworkgnome.openmud.domain;

public class Player extends LivingEntity {

    private String sessionId;

    public Player(String name, String sessionId) {
        super(name);
        this.sessionId = sessionId;
    }

    public String getSessionId() { return sessionId; }

    public void move(String direction) {
        Location newLocation = currentLocation.getNextLocation(direction);
        currentLocation.removePlayer(this);
        newLocation.addPlayer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return (sessionId.equals(player.sessionId))&&(name.equals(player.name));

    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
