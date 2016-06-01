package pl.com.clockworkgnome.openmud.domain;

import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {


    public Location getStartingLocation() {
        Location starting = new Location(1,"Short description","Long description of starting location");
        return starting;
    }
}
