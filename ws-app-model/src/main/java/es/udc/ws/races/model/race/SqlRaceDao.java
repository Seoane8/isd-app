package es.udc.ws.races.model.race;

public interface SqlRaceDao {
    public Race create(Connection connection, Race race);

    //Necesario para añadir una nueva inscripción
    public void update(Connection connection, Race race)
            throws InstanceNotFoundException;

    public Race findRace(Connection connection, Long raceId)
            throws InstanceNotFoundException;

    public List<Race> findRaces(Connection connection, LocalDate date, String city);

    //Necesario para los tests
    public void remove(Connection connection, Race race)
            throws InstanceNotFoundException;
}
