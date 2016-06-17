package pl.com.clockworkgnome.openmud.domain;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;


public class PlayersRepositoryTest {


    PlayersRepository repository;

    @Before
    public void beforeTest() {
        repository = new PlayersRepository();
        LocationRepository lr = new LocationRepository();
        repository.setLocationRepository(lr);
    }

    @Test
    public void newPlayerInit() {
        Player xxx = repository.initPlayer("XXX", "123");
        assertEquals(xxx.getName(),"XXX");
        Player fromRepo = repository.get("XXX");
        assertEquals(xxx,fromRepo);
        assertEquals(fromRepo.getSessionId(),"123");
    }

    @Test
    public void initTwoPlayersSameLocation() {
        final Player first = repository.initPlayer("XXX", "--");
        final Player second = repository.initPlayer("YYY", "--");

        assertEquals(2,first.getCurrentLocation().getPlayers().size());
    }

    @Test
    public void initTwoPlayersSameLocationOneLeaves() {
        final Player first = repository.initPlayer("XXX", "--");
        final Player second = repository.initPlayer("YYY", "--");

        assertEquals(first.getCurrentLocation().getPlayers().size(),2);

        final Exit firstExit = first.getCurrentLocation().getExits().keySet().iterator().next();

        first.move(firstExit.exitString);

        assertEquals(1,first.getCurrentLocation().getPlayers().size());
        assertEquals(1,second.getCurrentLocation().getPlayers().size());
        assertNotEquals(first.getCurrentLocation(),second.getCurrentLocation());
    }
}