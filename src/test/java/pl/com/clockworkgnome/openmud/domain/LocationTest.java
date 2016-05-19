package pl.com.clockworkgnome.openmud.domain;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    public void addPlayerToLocation() {
        Location location = new Location(1, "Test location", "This is test location.");
        Player player1 = new Player(1, "Ulik");
        Player player2 = new Player(2, "Felie");

        location.addPlayer(player1);
        location.addPlayer(player2);

        assertEquals(location.getPlayers().size(), 2);
        assertThat(location.getPlayers(), hasItems(player1));

        location.removePlayer(player1);

        assertEquals(location.getPlayers().size(), 1);

    }
}
