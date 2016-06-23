package pl.com.clockworkgnome.openmud.domain;

public class NPC extends LivingEntity {

    private String id;

    public NPC(String id, String name) {
        super(name);
    }

    @Override
    public void move(String direction) {
        Location newLocation = currentLocation.getNextLocation(direction);
        currentLocation.removeNPC(this);
        newLocation.addNPC(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NPC npc = (NPC) o;

        return id != null ? id.equals(npc.id) : npc.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
