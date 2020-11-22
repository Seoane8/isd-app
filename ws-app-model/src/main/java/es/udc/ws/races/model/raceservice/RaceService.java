package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RaceService {
    public Race addRace(String description, float price, LocalDateTime raceDate, int maxParticipants, String city)
            throws InputValidationException;

    public Race findRace(Long raceID)
            throws InstanceNotFoundException;

    public List<Race> findRaces(LocalDate date, String city)
            throws InputValidationException;

    public Long addInscription(Long raceId, String mail, String creditCard)
            throws InputValidationException, InstanceNotFoundException, NoMoreInscriptionsAllowedException,
            InscriptionDateExpiredException, AlreadyInscriptedException;

    public List<Inscription> findInscriptions(String mail)
            throws InputValidationException;

    public int collectDorsal(String creditCard, Long inscriptionId)
            throws InputValidationException, InstanceNotFoundException,
            AlreadyCollectedException, IncorrectCreditCardException;
}
