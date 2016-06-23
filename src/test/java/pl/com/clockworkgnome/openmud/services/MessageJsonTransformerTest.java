package pl.com.clockworkgnome.openmud.services;

import org.junit.Test;
import pl.com.clockworkgnome.openmud.domain.Exit;
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
        loc.addPlayer(player);

        final String json = transformer.locationLook(player);
        String expected = "{\"type\":\"Location\",\"short\":\"Short\",\"long\":\"Long\",\"players\":[],\"exits\":[]}";

        assertEquals(expected,json);
    }


    @Test
    public void testLocationTwoPlayersNoExits() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        Location loc = new Location(1,"Short","Long");
        Player firstPlayer = new Player("XXX","--");
        loc.addPlayer(firstPlayer);

        Player secondPlayer = new Player("YYY","--");
        loc.addPlayer(secondPlayer);

        final String json = transformer.locationLook(firstPlayer);
        String expected = "{\"type\":\"Location\",\"short\":\"Short\",\"long\":\"Long\",\"players\":[{\"name\":\"YYY\"}],\"exits\":[]}";

        assertEquals(expected,json);
    }

    @Test
    public void testLocationThreePlayersNoExits() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        Location loc = new Location(1,"Short","Long");
        Player firstPlayer = new Player("XXX","--");
        loc.addPlayer(firstPlayer);

        Player secondPlayer = new Player("YYY","--");
        loc.addPlayer(secondPlayer);

        Player thirdPlayer = new Player("ZZZ","--");
        loc.addPlayer(thirdPlayer);

        final String json = transformer.locationLook(firstPlayer);
        String expected = "{\"type\":\"Location\",\"short\":\"Short\",\"long\":\"Long\",\"players\":[{\"name\":\"YYY\"},{\"name\":\"ZZZ\"}],\"exits\":[]}";

        assertEquals(expected,json);
    }

    @Test
    public void testLocationTwoPlayersTwoExits() {
        MessageJsonTransformer transformer = new MessageJsonTransformer();

        Location loc = new Location(1,"Short","Long");
        Player player = new Player("XXX","--");
        loc.addPlayer(player);

        Location locTwo = new Location(2,"Another Short","Another Long");

        loc.addExit(Exit.DOOR,locTwo);
        loc.addExit(Exit.NORTH,locTwo);

        final String json = transformer.locationLook(player);
        String expected = "{\"type\":\"Location\",\"short\":\"Short\",\"long\":\"Long\",\"players\":[],\"exits\":[{\"exit\":\"door\"},{\"exit\":\"north\"}]}";

        assertEquals(expected,json);
    }

}