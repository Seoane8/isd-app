package es.udc.ws.races.model.raceservice;

import Race.Race;
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
import es.udc.ws.util.validation.PropertyValidator;

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
        this.inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    @Override
    public Race addRace(int maxParticipants, String description, float inscriptionPrice, LocalDateTime raceDate, String raceLocation){

        //validateRace(maxParticipants,description,inscriptionPrice,raceDate,raceLocation);
        try(Connection connection = dataSource.getConnection()){


            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Race createdRace = raceDao.create(connection,new Race(maxParticipants,description,inscriptionPrice,raceDate,raceLocation,0));

                connection.commit();

                return createdRace;

            }catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException| Error e){
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

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
    public List<Race> findRaces(LocalDateTime date, String city ){
        try (Connection connection = dataSource.getConnection()){
            return raceDao.findRaces(connection,date,city);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
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
        PropertyValidator.validateCreditCard(creditCard);

        try (Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(
                        Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Inscription inscription = inscriptionDao.find(connection, inscriptionId);

                if (inscription.getCredCardNumber() != creditCard){
                    connection.commit();
                    throw new IncorrectCreditCardException();
                }
                if (inscription.isDorsalCollected()){
                    connection.commit();
                    throw new AlreadyCollectedException();
                }

                inscription.setDorsalCollected(true);
                inscriptionDao.update(connection, inscription);

                connection.commit();
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw e;
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
