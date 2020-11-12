package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static es.udc.ws.races.model.util.Constants.DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class RaceServiceTest {
    private final String VALID_DESCRIPTION = "Description of a new race";
    private final float VALID_PRICE = 9.95f;
    private final LocalDateTime VALID_RACE_DATE = LocalDateTime.parse("2020-08-30T11:30:00");
    private final int VALID_PARTICIPANTS = 130;
    private final String VALID_CITY = "A Coruña";
    private final long INVALID_RACEID = -1;

    private final String VALID_CREDIT_CARD = "1234123412341234";
    private final String INVALID_CREDIT_CARD = "432";
    private final long INVALID_INSCRIPTION_ID = -1;

    private static RaceService raceService = null;
    private static SqlRaceDao raceDao = null;
    private static SqlInscriptionDao inscriptionDao = null;

    @BeforeAll
    public static void init(){
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(DATA_SOURCE, dataSource);

        raceService = RaceServiceFactory.getService();
        raceDao = SqlRaceDaoFactory.getDao();
        inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    private void removeRace(Race race) throws SQLException{
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                raceDao.remove(connection, race);

                connection.commit();
            }catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        }
    }

    @Test
    public void testAddRaceAndFindRace() throws InstanceNotFoundException, InputValidationException {
        Race race = null;

        try{
            LocalDateTime beforeAddDate = LocalDateTime.now().withNano(0);

            race = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);

            LocalDateTime afterAddDate = LocalDateTime.now().withNano(0);

            Race foundRace = raceService.findRace(race.getRaceId());

            assertEquals(race, foundRace);
            assertEquals(VALID_DESCRIPTION, foundRace.getDescription());
            assertEquals(VALID_PRICE, foundRace.getInscriptionPrice());
            assertEquals(VALID_RACE_DATE, foundRace.getRaceDate());
            assertEquals(VALID_PARTICIPANTS, foundRace.getMaxParticipants());
            assertEquals(VALID_CITY, foundRace.getRaceLocation());
            assertTrue((foundRace.getCreationDate().compareTo(beforeAddDate) >= 0)
                    && (foundRace.getCreationDate().compareTo(afterAddDate) <= 0));

        } finally {
            if (race != null){
                try{
                    removeRace(race);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void testFindNonExistentRace(){

        assertThrows(InstanceNotFoundException.class, () -> {
            Race race = raceService.findRace(INVALID_RACEID);
            removeRace(race);
        });

    }

    @Test
    public void testCollectDorsalWithInvalidCreditCard(){

        assertThrows(InputValidationException.class, () ->
                raceService.collectDorsal(INVALID_CREDIT_CARD, INVALID_INSCRIPTION_ID));
    }
}
