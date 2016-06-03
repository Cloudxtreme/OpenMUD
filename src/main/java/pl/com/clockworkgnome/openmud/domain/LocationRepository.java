package pl.com.clockworkgnome.openmud.domain;

import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {

    Location startingLocation;


    public Location getStartingLocation() {
        if(startingLocation==null) {
            startingLocation = new Location(1,"Short description","Long description of starting location");
        }
        return startingLocation;
    }
}
