package pl.com.clockworkgnome.openmud.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {


    @Test
    public void shouldHaveName() {
        Player player = new Player("Ulik", "sess");
        assertEquals(player.getName(), "Ulik");
    }
}
