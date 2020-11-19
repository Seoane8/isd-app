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
    public Race addRace(String description, float inscriptionPrice, LocalDateTime raceDate, int maxParticipants, String raceLocation) throws InputValidationException {

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
    public List<Race> findRaces(LocalDate date, String city)  {
        try (Connection connection = dataSource.getConnection()){
            return raceDao.findRaces(connection,date.atStartOfDay(),city);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long addInscription(Long raceId, String mail, String creditCard) throws InputValidationException, InstanceNotFoundException, NoMoreInscriptionsAllowedException, InscriptionDateExpiredException, AlreadyInscriptedException {

        PropertyValidator.validateCreditCard(creditCard);
        try(Connection connection = dataSource.getConnection()){
            try{
                /* Prepare connection */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work */

                Race race = findRace(raceId);
                if(race.getRaceDate().minusDays(1).isBefore(LocalDateTime.now())){
                    throw new InscriptionDateExpiredException();
                }
                if(race.getParticipants() >= race.getMaxParticipants()){
                    throw new NoMoreInscriptionsAllowedException();
                }

                if(inscriptionDao.isInscripted(connection,raceId,mail)){
                        throw new AlreadyInscriptedException();
                }

                Inscription inscription = new Inscription(raceId,mail,creditCard,LocalDateTime.now(),
                        race.getParticipants()+1,false,race.getInscriptionPrice());

                Inscription createdInscription = inscriptionDao.create(connection,inscription);
                race.setParticipants(race.getParticipants()+1);
                raceDao.update(connection,race);

                /* Commit */

                connection.commit();
                return createdInscription.getInscriptionId();

            }catch(SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch(RuntimeException|Error e){
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscription> findInscriptions(String mail) throws InputValidationException {
        try(Connection connection = dataSource.getConnection()){
            return inscriptionDao.findByMail(connection,mail);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
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

                if (!inscription.getCredCardNumber().equals(creditCard)){
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

    private void validateRace (int maxParticipants, String description, Float inscriptionPrice, LocalDate raceDate, String city) throws InputValidationException {
        PropertyValidator.validateDouble("maxParticipants",(double) maxParticipants,1,1000);
        PropertyValidator.validateMandatoryString("description", description);
        PropertyValidator.validateDouble("inscriptionPrice", inscriptionPrice, 1, 1000000);
        validateRaceDate(raceDate,city);
        PropertyValidator.validateMandatoryString("city", city);
    }

    public void validateRaceDate(LocalDate raceDate,String city){
        if(raceDate.isBefore(LocalDate.now())){
            throw new InputValidationException("Race with city=\"" + city +
                    "\" race date before current date (raceDate = \"" +
                    raceDate.toString() + "\") (currentDate = \"" +LocalDate.now().toString()+ "\")");
        }
    }

}
