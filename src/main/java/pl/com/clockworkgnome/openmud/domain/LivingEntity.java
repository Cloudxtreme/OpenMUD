package pl.com.clockworkgnome.openmud.domain;

public abstract class LivingEntity {

    protected String name;
    protected Location currentLocation;

    public LivingEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation=currentLocation;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    abstract public void move(String direction);

}
