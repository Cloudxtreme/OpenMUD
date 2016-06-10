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
}