package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.races.model.util.Constants.DATA_SOURCE;

public class RaceServiceImpl implements RaceService{

    private final DataSource dataSource;
    private SqlRaceDao raceDao = null;
    private SqlInscriptionDao inscriptionDao = null;

    public RaceServiceImpl() {
        this.dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);
        this.raceDao = SqlRaceDaoFactory.getDao();
    }

    @Override
    public Race addRace(String description, Float price, LocalDateTime raceDate, int maxParticipants, String city) throws InputValidationException {
        return null;
    }

    @Override
    public Race findRace(Long raceID) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()){
            return raceDao.findRace(connection, raceID);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Race> findRaces(LocalDate date, String city) throws InputValidationException {
        return null;
    }

    @Override
    public String addInscription(Long raceId, String mail, String creditCard) throws InputValidationException, InstanceNotFoundException, NoMoreInscriptionsAllowedException, InscriptionDateExpiredException, AlreadyInscriptedException {
        return null;
    }

    @Override
    public List<Inscription> findInscriptions(String mail) throws InputValidationException {
        return null;
    }

    @Override
    public void collectDorsal(String creditCard, Long inscriptionId) throws InputValidationException, InstanceNotFoundException, AlreadyCollectedException, IncorrectCreditCardException {

    }
}
