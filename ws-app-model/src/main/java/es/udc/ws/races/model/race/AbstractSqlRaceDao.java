package es.udc.ws.races.model.race;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlRaceDao implements SqlRaceDao {

	
	protected AbstractSqlRaceDao() {
    }

    @Override
    public Race findRace(Connection connection, Long raceId)
    		throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT maxParticipants, description, "
                + "  price, raceDate, city, creationDate, participants FROM Race WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, raceId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(raceId,
                        Race.class.getName());
            }

            /* Get results. */
            i = 1;
            int maxParticipants = resultSet.getInt(i++);
            String description = resultSet.getString(i++);
            float inscriptionPrice = resultSet.getFloat(i++);
            LocalDateTime raceDate = resultSet.getTimestamp(i++).toLocalDateTime();
            String raceLocation = resultSet.getString(i++);
            LocalDateTime creationDate = resultSet.getTimestamp(i++).toLocalDateTime();
            int participants = resultSet.getInt(i++);


            /* Return movie. */
            return new Race(raceId,maxParticipants,description,inscriptionPrice,raceDate,raceLocation,creationDate,participants);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    
    @Override
    public List<Race> findRaces(Connection connection, LocalDateTime initDate, String city) {
	    initDate = initDate.withNano(0);

        /* Create "queryString". */
        String queryString = "SELECT raceId, maxParticipants, description, "
                + " price, raceDate, city, creationDate, participants FROM Race";

        queryString += " WHERE raceDate >= (?)";
        if (initDate != null) {
           queryString += " AND raceDate <= (?)";
        }
        if (city != null) {
            queryString += " AND city = (?)";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int j = 1;

            Timestamp nowDate = new Timestamp(
                    Timestamp.valueOf(LocalDateTime.now()).getTime());
            preparedStatement.setTimestamp(j++, nowDate);

            if (initDate != null) {
                Timestamp raceDate = initDate != null ? new Timestamp(
                        Timestamp.valueOf(initDate).getTime()) : null;
                preparedStatement.setTimestamp(j++, raceDate);
            }
            if (city != null) {
                /* Fill "preparedStatement". */
                preparedStatement.setString(j++,city);
            }

            /* Execute query. */

            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read bikes. */
            List<Race> races = new ArrayList<Race>();

            while (resultSet.next()) {

               int i = 1;
                long raceId = resultSet.getLong(i++);
                int maxParticipants = resultSet.getInt(i++);
                String description = resultSet.getString(i++);
                float inscriptionPrice = resultSet.getFloat(i++);
                LocalDateTime raceDate = resultSet.getTimestamp(i++).toLocalDateTime();
                String raceLocation = resultSet.getString(i++);
                LocalDateTime creationDate = resultSet.getTimestamp(i++).toLocalDateTime();
                int participants = resultSet.getInt(i++);

                races.add(new Race(raceId, maxParticipants, description, inscriptionPrice,
                        raceDate, raceLocation, creationDate, participants));

            }

            /* Return bikes. */
            return races;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    
    @Override
    public void update(Connection connection, Race race)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Race"
                + " SET maxParticipants = ?, description = ?, price = ?, "
                + "raceDate = ?, city = ?, participants = ? WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setInt(i++,race.getMaxParticipants());
            preparedStatement.setString(i++, race.getDescription());
            preparedStatement.setFloat(i++, race.getInscriptionPrice());
            Timestamp raceDate = race.getRaceDate()!= null ? new Timestamp(
                    Timestamp.valueOf(race.getRaceDate()).getTime()) : null;
            preparedStatement.setTimestamp(i++, raceDate);
            preparedStatement.setString(i++, race.getRaceLocation());
            preparedStatement.setInt(i++,race.getParticipants());
            preparedStatement.setLong(i++,race.getRaceId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(race.getRaceId(),
                        Race.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    
    @Override
    public void remove(Connection connection, Race race)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Race WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, race.getRaceId());

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(race.getRaceId(),
                        Race.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

