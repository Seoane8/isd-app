package es.udc.ws.races.model.inscription;

public interface SqlInscriptionDao {
    public Inscription create(Connecion connection, Inscription inscription);

    //Necesario para recoger dorsal
    public void update(Connection connection, Inscription inscription)
            throws InstanceNotFoundException;

    //Necesario para recoger dorsal
    public Inscription find(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException;

    public List<Inscription> findByMail(Connection connection, String mail);

    //Necesario para añadir una inscripción
    public boolean isInscripted(Connection connection, Long raceId, String mail);

    //Necesario para los tests
    public void remove(Connection connection, Inscription inscription)
            throws InstanceNotFoundException;
}
