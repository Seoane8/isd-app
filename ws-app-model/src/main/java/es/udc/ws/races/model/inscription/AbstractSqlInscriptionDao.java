package es.udc.ws.races.model.inscription;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlInscriptionDao implements SqlInscriptionDao{

    protected AbstractSqlInscriptionDao(){

    }

    @Override
    public void update(Connection connection, Inscription inscription) throws InstanceNotFoundException {
        String queryString = "UPDATE Inscription "
                + "SET raceId = ?, mail = ?, creditCardNumber = ?, dorsal = ?, "
                + "dorsalCollected = ?, price = ? WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement =
             connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setLong(i++, inscription.getRaceID());
            preparedStatement.setString(i++, inscription.getMail());
            preparedStatement.setString(i++, inscription.getCredCardNumber());
            preparedStatement.setInt(i++, inscription.getDorsal());
            preparedStatement.setBoolean(i++, inscription.isDorsalCollected());
            preparedStatement.setFloat(i++, inscription.getPrice());
            preparedStatement.setLong(i++, inscription.getInscriptionId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0){
                throw new InstanceNotFoundException(inscription.getInscriptionId(),
                        Inscription.class.getName());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inscription find(Connection connection, Long inscriptionId) throws InstanceNotFoundException {
        String queryString = "SELECT raceId, mail, creditCardNumber, "
                + "reservationDate, dorsal, dorsalCollected, "
                + "price FROM Inscription WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement =
             connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setLong(i++, inscriptionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()){
                throw new InstanceNotFoundException(inscriptionId,
                        Inscription.class.getName());
            }

            i = 1;
            Long raceId = resultSet.getLong(i++);
            String mail = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime reservationDate = creationDateAsTimestamp.toLocalDateTime();
            int dorsal = resultSet.getInt(i++);
            boolean dorsalCollected = resultSet.getBoolean(i++);
            float price = resultSet.getFloat(i++);

            return new Inscription(inscriptionId, raceId, mail, creditCardNumber, reservationDate , dorsal, dorsalCollected, price);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isInscripted(Connection connection, Long raceId, String mail){

        /* Create "queryString". */
        String queryString = "SELECT * FROM Inscription WHERE raceID = ? AND mail = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){


            /* Fill "preparedStatement" */
            int i = 1;
            preparedStatement.setLong(i++,raceId);
            preparedStatement.setString(i++,mail);

            /* Execute query */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read result and return*/
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscription> findByMail(Connection connection, String mail){

        /* Create "queryString"*/

        String queryString = "SELECT * FROM Inscription WHERE mail = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            /* Fill "preparedStatement". */
            preparedStatement.setString(1,mail);

            /* Execute query */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read inscriptions */
            List<Inscription> inscriptions = new ArrayList<Inscription>();
            while(resultSet.next()){

                int i = 1;
                Long inscriptionId = resultSet.getLong(i++);
                Long raceId = resultSet.getLong(i++);
                String mailR = resultSet.getString(i++);
                String creditcard = resultSet.getString(i++);
                LocalDateTime reservationDate = Instant.ofEpochMilli(resultSet.getDate(i++).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                int dorsal = resultSet.getInt(i++);
                boolean dorsalCollected = resultSet.getBoolean(i++);
                float price = resultSet.getFloat(i++);

                inscriptions.add(new Inscription(inscriptionId,raceId, mailR,creditcard, reservationDate, dorsal, dorsalCollected, price));
            }

            /* Return inscriptions */
            return inscriptions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Inscription inscription) throws InstanceNotFoundException {

        String queryString = "DELETE FROM Inscription WHERE inscriptionId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, inscription.getInscriptionId());

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0){
                throw new InstanceNotFoundException(inscription.getInscriptionId(),
                        Inscription.class.getName());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
