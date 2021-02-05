package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.model.raceservice.exceptions.AlreadyCollectedException;
import es.udc.ws.races.model.raceservice.exceptions.IncorrectCreditCardException;
import es.udc.ws.races.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class ThriftRaceServiceImpl implements ThriftRaceService.Iface{

    @Override
    public long addRace(ThriftRaceDto raceDto)
            throws ThriftInputValidationException {
        return 0;
    }

    @Override
    public ThriftRaceDto findRace(long raceId)
            throws ThriftInstanceNotFoundException {

        try{
            Race race  = RaceServiceFactory.getService().findRace(raceId);
            return RaceToThriftRaceDtoConversor.toThriftRaceDto(race);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }

    @Override
    public List<ThriftRaceDto> findRaces(String date, String city)
            throws ThriftInputValidationException {
        return null;
    }

    @Override
    public long addInscription(long raceId, String mail, String creditCard)
            throws ThriftInputValidationException, ThriftInstanceNotFoundException,
            ThriftAlreadyInscriptedException, ThriftInscriptionDateExpiredException{
        return 0;
    }

    @Override
    public List<ThriftInscriptionDto> findInscriptions(String mail)
            throws ThriftInputValidationException {
        return null;
    }

    @Override
    public int collectDorsal(String creditCard, long inscriptionId)
            throws ThriftInputValidationException, ThriftInstanceNotFoundException,
            ThriftAlreadyCollectedException, ThriftIncorrectCreditCardException {

        try {
            return RaceServiceFactory.getService().collectDorsal(creditCard, inscriptionId);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (AlreadyCollectedException e) {
            throw new ThriftAlreadyCollectedException(e.getInscriptionId(), e.getMail());
        } catch (IncorrectCreditCardException e) {
            throw new ThriftIncorrectCreditCardException(e.getInscriptionId(), e.getMail());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }
}
