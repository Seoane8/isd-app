package es.udc.ws.races.model.race;

import java.sql.*;
import java.time.LocalDateTime;


public class Jdbc3CcSqlRaceDao extends AbstractSqlRaceDao {

	 @Override
	    public Race create(Connection connection, Race race) {

	        /* Create "queryString". */
	        String queryString = "INSERT INTO Race"
	                + " (maxParticipants, description, price, raceDate, city,creationDate, participants)"
	                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                        queryString, Statement.RETURN_GENERATED_KEYS)) {

	            /* Fill "preparedStatement". */
	        	race.setCreationDate(LocalDateTime.now().withNano(0));
	            int i = 1;
	            preparedStatement.setInt(i++,race.getMaxParticipants());
	            preparedStatement.setString(i++, race.getDescription());
	            preparedStatement.setFloat(i++, race.getInscriptionPrice());
	            Timestamp raceDate = race.getRaceDate()!= null ? new Timestamp(
						Timestamp.valueOf(race.getRaceDate()).getTime()) : null;
	            preparedStatement.setTimestamp(i++, raceDate);
	            preparedStatement.setString(i++, race.getRaceLocation());
	            Timestamp creationDate = race.getCreationDate() != null ? new Timestamp(
						Timestamp.valueOf(race.getCreationDate()).getTime()) : null;
	            preparedStatement.setTimestamp(i++, creationDate);
	            preparedStatement.setInt(i++,race.getParticipants());
	            /* Execute query. */
	            preparedStatement.executeUpdate();
	            /* Get generated identifier. */
	            ResultSet resultSet = preparedStatement.getGeneratedKeys();

	            if (!resultSet.next()) {
	                throw new SQLException(
	                        "JDBC driver did not return generated key.");
	            }
	            Long raceId = resultSet.getLong(1);
	            

	            /* Return movie. */
	            return new Race(raceId, race.getMaxParticipants(), race.getDescription(),
	                    race.getInscriptionPrice(), race.getRaceDate(), race.getRaceLocation(),
						race.getCreationDate(),race.getParticipants());

	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }

	    }
}
