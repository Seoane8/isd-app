package es.udc.ws.races.model.raceservice;

public interface RaceService {
    public Race addRace(String description, Float price, LocalDateTime raceDate, int maxParticipants, String city)
            throws InputValidationException;

    public Race findRace(Long raceID)
            throws InstanceNotFoundException;

    public List<Race> findRaces(LocalDate date, String city)
            throws InputValidationException;

    public String addInscription(Long raceId, String mail, String creditCard)
            throws InputValidationException, InstanceNotFoundException, NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException;

    public List<Inscriptions> findInscriptions(String mail)
            throws InputValidationException;

    public void collectDorsal(String creditCard, Long inscriptionId)
            throws InputValidationException, InstanceNotFoundException,
            AlreadyCollectedException, IncorrectCreditCardException;
}
