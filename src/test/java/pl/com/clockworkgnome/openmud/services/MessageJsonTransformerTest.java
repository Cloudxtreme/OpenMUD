package pl.com.clockworkgnome.openmud.services;

import org.junit.Test;
import pl.com.clockworkgnome.openmud.domain.Location;
import pl.com.clockworkgnome.openmud.domain.Player;

import static org.junit.Assert.*;

public class MessageJsonTransformerTest {

    @Test
    public void testPlayerLeaves() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        final String json = transformer.playerLeaves("xxx", "over there");
        String expected = "{\"type\":\"Leaves\",\"message\":\"xxx goes over there\"}";

        assertEquals(expected,json);
    }

    @Test
    public void testPlayerSays() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        final String json = transformer.getSayResponse("Yep");
        String expected = "{\"type\":\"Say\",\"message\":\"You say: Yep\"}";

        assertEquals(expected,json);
    }

    @Test
    public void testSayOther() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        final String json = transformer.getSayOtherResponse("Yep","PlayerName");
        String expected = "{\"type\":\"Say\",\"message\":\"PlayerName says: Yep\"}";

        assertEquals(expected,json);
    }

    @Test
    public void testLocationOnePlayerNoExits() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        Location loc = new Location(1,"Short","Long");
        Player player = new Player("XXX","--");
        player.setCurrentLocation(loc);

        final String json = transformer.locationLook(player);
        String expected = "{\"type\":\"Location\",\"short\":\"Short\",\"long\":\"Long\",\"players\":[],\"exits\":[]}";

        assertEquals(expected,json);
    }

}