package es.udc.ws.races.model.inscription;

import java.sql.*;

public class Jdbc3CcSqlInscriptionDao extends AbstractSqlInscriptionDao{

    @Override
    public Inscription create(Connection connection, Inscription inscription) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Inscription"
                + "(raceId, mail, creditCardNumber, reservationDate, dorsal ,dorsalCollected, price)"
                + "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement" */
            int i = 1;
            preparedStatement.setLong(i++, inscription.getRaceID());
            preparedStatement.setString(i++, inscription.getMail());
            preparedStatement.setString(i++, inscription.getCredCardNumber());
            Timestamp reservationDate = inscription.getReservationDate() != null ? new Timestamp(
                    Timestamp.valueOf(inscription.getReservationDate()).getTime()) : null;
            preparedStatement.setTimestamp(i++, reservationDate);
            preparedStatement.setInt(i++, inscription.getDorsal());
            preparedStatement.setBoolean(i++, inscription.isDorsalCollected());
            preparedStatement.setFloat(i++, inscription.getPrice());

            /* Execute query */
            preparedStatement.executeUpdate();

            /* Get generated identifier */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key");
            }
            Long inscriptionId = resultSet.getLong(1);

            /* Return inscription */

            return new Inscription(inscriptionId, inscription.getRaceID(), inscription.getMail(),
                    inscription.getCredCardNumber(), inscription.getReservationDate() ,inscription.getDorsal(),
                    inscription.isDorsalCollected(), inscription.getPrice());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
