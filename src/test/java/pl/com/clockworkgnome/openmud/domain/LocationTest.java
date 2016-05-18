package pl.com.clockworkgnome.openmud.domain;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {

    @Test
    public void createLocation() {
        Location location = new Location(1, "Test location", "This is test location");
        assertEquals(location.getId(), 1);
        assertEquals(location.getShortDescription(), "Test location");
        assertEquals(location.getLongDescirption(), "This is test location");
    }

    @Test
    public void addExitsToLocation() {
        Location location = new Location(1, "Test location", "This is test location.");
        Location secondLocation = new Location(2, "Second", "Second location.");
        Location thirdLocation = new Location(3, "Third", "Third location.");

        location.addExit(Exit.NORTH, secondLocation);
        location.addExit(Exit.DOOR, thirdLocation);

        assertEquals(location.getExits().size(), 2);
        assertEquals(location.getExits().get(Exit.DOOR), thirdLocation);


    }
}
