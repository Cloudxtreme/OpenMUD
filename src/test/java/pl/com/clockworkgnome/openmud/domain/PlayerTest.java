package pl.com.clockworkgnome.openmud.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {


    @Test
    public void shouldHaveName() {
        Player player = new Player(1, "Ulik");
        assertEquals(player.getId(), 1);
        assertEquals(player.getName(), "Ulik");
    }
}
