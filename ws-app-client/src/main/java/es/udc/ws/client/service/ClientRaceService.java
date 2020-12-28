package es.udc.ws.client.service;

import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientRaceService {

    public Long addRace(ClientRaceDto race)
            throws InputValidationException;

    public Race findRace(Long raceID)
            throws InstanceNotFoundException;

    public List<ClientRaceDto> findRaces(LocalDate date, String city)
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


