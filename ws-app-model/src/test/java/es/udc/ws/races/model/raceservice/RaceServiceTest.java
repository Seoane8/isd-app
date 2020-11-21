package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.races.model.util.Constants.DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class RaceServiceTest {
    private final String VALID_DESCRIPTION = "Description of a new race";
    private final float VALID_PRICE = 9.95f;
    private final LocalDateTime VALID_RACE_DATE = LocalDateTime.parse("2021-08-22T11:30:00");
    private final int VALID_PARTICIPANTS = 130;
    private final String VALID_CITY = "A Coruña";
    private final long INVALID_RACEID = -1;

    private final String VALID_MAIL = "example@udc.es";
    private final String VALID_CREDIT_CARD = "1234123412341234";
    private final String INVALID_CREDIT_CARD = "432";
    private final String INCORRECT_CREDIT_CARD = "1111222233334444";
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
    private Race createRace(Race race) throws InputValidationException {

        Race addedRace = null;

        addedRace = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);
        return addedRace;

    }


    private void removeRace(Race race){
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeInscription(Inscription inscription){
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                inscriptionDao.remove(connection, inscription);

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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Inscription findInscription(Long inscriptionId){
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            return inscriptionDao.find(connection, inscriptionId);
        }catch (InstanceNotFoundException | SQLException e){
            throw new RuntimeException(e);
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
                removeRace(race);
            }
        }
    }

    @Test
    public void testAddInvalidRace() throws InputValidationException, SQLException{

        // Check maxparticipants is >=1

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setMaxParticipants(0);
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

        // Check description isn't null

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setDescription(null);
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

        // Check description isn't empty

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setDescription("");
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

        // Check race price >= 0

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setInscriptionPrice(-5);
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

        //Check raceLocation isn't null

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setRaceLocation(null);
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

        //Check raceLocation isn't empty

        assertThrows(InputValidationException.class, () -> {
            Race race = new Race(10,VALID_DESCRIPTION,VALID_PRICE,VALID_RACE_DATE,VALID_CITY,VALID_PARTICIPANTS);
            race.setRaceLocation("");
            Race addedRace = createRace(race);
            removeRace(addedRace);
        });

    }


    @Test
    public void testFindNonExistentRace(){

        assertThrows(InstanceNotFoundException.class, () -> {
            Race race = raceService.findRace(INVALID_RACEID);
            removeRace(race);
        });

    }

    @Test
    public void testCollectDorsalAndAlreadyCollectedDorsal() throws InputValidationException,
            InstanceNotFoundException, AlreadyCollectedException,
            IncorrectCreditCardException, NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException {

        Race race = null;
        Inscription inscription = null;

        try{
            race = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);
            Long inscriptionId = raceService.addInscription(race.getRaceId(),
                    VALID_MAIL, VALID_CREDIT_CARD);

            raceService.collectDorsal(VALID_CREDIT_CARD, inscriptionId);

            inscription = findInscription(inscriptionId);

            assertTrue(inscription.isDorsalCollected());

            assertThrows(AlreadyCollectedException.class, () ->
                    raceService.collectDorsal(VALID_CREDIT_CARD, inscriptionId));

        } finally{
            if (inscription != null){
                removeInscription(inscription);
            }
            if (race != null){
                removeRace(race);
            }
        }
    }

    @Test
    public void testFindRaces() throws InputValidationException{
        Race race1 = null;
        Race race2 = null;
        Race race3 = null;
        Race raceDiferentCity = null;
        try {

            race1 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    LocalDateTime.parse("2021-12-01"), VALID_PARTICIPANTS, VALID_CITY);
            race2 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);
            race3 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    LocalDateTime.parse("2021-05-27"), VALID_PARTICIPANTS, VALID_CITY);
            raceDiferentCity = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    LocalDateTime.parse("2021-04-01"), VALID_PARTICIPANTS,"Ferrol");


            //Deberia añadir a la lista las carreras race2 y race 3
            List<Race> foundRaces = raceService.findRaces(LocalDate.parse("2021-02-01"), "A Coruña");
            //Deberia añadir a la lista la carrera que se celebra en Ferrol
            List<Race> foundDiferentRace = raceService.findRaces(LocalDate.parse("2021-01-01"), "Ferrol");
            // Probamos a hacer una busqueda sin resultado, deberia devolver una lista vacia
            List<Race> nofoundRace = raceService.findRaces(LocalDate.parse("2021-02-01"), "Coirós");

            assertEquals(2,foundRaces.size());
            assertEquals(race2, foundRaces.get(0));
            assertEquals(race3, foundRaces.get(1));
            //Busqueda en una ciudad distinta
            assertEquals(1,foundDiferentRace);
            assertEquals(raceDiferentCity,foundDiferentRace.get(0));
            //Búsqueda sin resultados
            assertTrue(nofoundRace.isEmpty());
            //Búsqueda incorrecta (ciudad=null)
            assertThrows(InputValidationException.class, () -> raceService.findRaces(LocalDate.parse("2021-09-01"),null));

        } finally {
            if (race1!=null)
                removeRace(race1);
            if (race2!=null)
                removeRace(race2);
            if (race3!=null)
                removeRace(race3);
        }
    }

    @Test
    public void testCollectDorsalWithInvalidCreditCard(){

        assertThrows(InputValidationException.class, () ->
                raceService.collectDorsal(INVALID_CREDIT_CARD, INVALID_INSCRIPTION_ID));
    }

    @Test
    public void testCollectDorsalOfNonExistentInscription(){

        assertThrows(InstanceNotFoundException.class, () ->
                raceService.collectDorsal(VALID_CREDIT_CARD, INVALID_INSCRIPTION_ID));
    }

    @Test
    public void testCollectDorsalWithIncorrectCreditCard() throws InputValidationException,
            InstanceNotFoundException, NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException {

        Race race = null;
        Inscription inscription = null;

        try{
            race = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE,
                    VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);
            Long inscriptionId = raceService.addInscription(race.getRaceId(),
                    VALID_MAIL, VALID_CREDIT_CARD);

            assertThrows(IncorrectCreditCardException.class, () ->
                    raceService.collectDorsal(INCORRECT_CREDIT_CARD, inscriptionId));

            //Necessary to remove inscription
            inscription = findInscription(inscriptionId);

        } finally{
            if (inscription != null){
                removeInscription(inscription);
            }
            if (race != null){
                removeRace(race);
            }
        }
    }

    @Test
    public void testAddInscription() throws InputValidationException,
            InstanceNotFoundException,
            NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException {
        Race race1 = null;
        Race race2 = null;

        Long inscriptionID = null;
        Long inscriptionID2 = null;

        try{
            race1 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE, VALID_RACE_DATE, 2, VALID_CITY);
            race2 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE, LocalDateTime.now(), VALID_PARTICIPANTS, VALID_CITY);
            Race finalRace1 = race1;
            Race finalRace2 = race2;
            assertThrows(InputValidationException.class, () -> raceService.addInscription(finalRace1.getRaceId(),VALID_MAIL,INVALID_CREDIT_CARD));
            assertThrows(InscriptionDateExpiredException.class, () -> raceService.addInscription(finalRace2.getRaceId(), VALID_MAIL, VALID_CREDIT_CARD));
            inscriptionID = raceService.addInscription(race1.getRaceId(),VALID_MAIL,VALID_CREDIT_CARD);
            assertThrows(AlreadyInscriptedException.class, () -> raceService.addInscription(finalRace1.getRaceId(), VALID_MAIL, VALID_CREDIT_CARD));
            inscriptionID2 = raceService.addInscription(race1.getRaceId(),"example2@udc.es","1234123412341235");
            assertEquals(race1.getRaceId(), findInscription(inscriptionID).getRaceID());
            assertEquals(race1.getRaceId(), findInscription(inscriptionID2).getRaceID());
            assertThrows(NoMoreInscriptionsAllowedException.class, () ->  raceService.addInscription(finalRace1.getRaceId(), "example3@udc.es", "1234123412341236"));
        }finally{
            if(inscriptionID != null){ removeInscription(findInscription(inscriptionID));}
            if(inscriptionID2 != null){ removeInscription(findInscription(inscriptionID2));}
            if(race1 != null){ removeRace(race1);}
            if(race2 != null){ removeRace(race2);}

        }
    }

    @Test
    void testFindInscriptions() throws InputValidationException,
            InstanceNotFoundException,
            NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException{

        Race race1 = null;
        Race race2 = null;

        Long inscriptionID = null;
        Long inscriptionID2 = null;

        List<Inscription> inscriptions = null;

        try {
            race1 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE, VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);
            race2 = raceService.addRace(VALID_DESCRIPTION, VALID_PRICE, VALID_RACE_DATE, VALID_PARTICIPANTS, VALID_CITY);

            inscriptionID = raceService.addInscription(race1.getRaceId(), VALID_MAIL, VALID_CREDIT_CARD);
            inscriptionID2 = raceService.addInscription(race2.getRaceId(), VALID_MAIL, VALID_CREDIT_CARD);

            inscriptions = raceService.findInscriptions(VALID_MAIL);

            assertEquals(inscriptions.get(0).getInscriptionId(), inscriptionID);
            assertEquals(inscriptions.get(1).getInscriptionId(), inscriptionID2);
        }finally{

            if(inscriptionID != null){ removeInscription(findInscription(inscriptionID));}
            if(inscriptionID2 != null){ removeInscription(findInscription(inscriptionID2));}
            if(race1 != null){ removeRace(race1);}
            if(race2 != null){ removeRace(race2);}
            if(inscriptions != null){inscriptions.clear();}
        }
    }
}


