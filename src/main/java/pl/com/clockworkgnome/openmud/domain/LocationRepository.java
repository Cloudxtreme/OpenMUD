package pl.com.clockworkgnome.openmud.domain;

import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {

    Location startingLocation;


    public Location getStartingLocation() {
        if(startingLocation==null) {
            startingLocation = new Location(1,"Short description","Long description of starting location");
            Location nextOne = new Location(2,"Next short","Long of next location");
            startingLocation.addExit(Exit.EAST,nextOne);
            nextOne.addExit(Exit.WEST,startingLocation);
        }
        return startingLocation;
    }
}
