package es.udc.ws.client.service.thrift;

import es.udc.ws.client.service.ClientRaceService;
import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.client.service.exceptions.*;
import es.udc.ws.races.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientRaceService implements ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientRaceService.endpointAddress";
    private String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    private ThriftRaceService.Client getClient() {
        try{
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftRaceService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Long addRace(ClientRaceDto race)
            throws InputValidationException {
        return null;
    }

    @Override
    public ClientRaceDto findRace(Long raceID)
            throws InstanceNotFoundException {

        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            return ClientRaceDtoToThriftRaceDtoConversor.toClientRaceDto(client.findRace(raceID));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public List<ClientRaceDto> findRaces(LocalDate date, String city)
            throws InputValidationException {
        return null;
    }

    @Override
    public Long addInscription(Long raceId, String mail, String creditCard)
            throws InputValidationException, InstanceNotFoundException, ClientNoMoreInscriptionsAllowedException,
            ClientInscriptionDateExpiredException, ClientAlreadyInscriptedException {
        return null;
    }

    @Override
    public List<ClientInscriptionDto> findInscriptions(String mail)
            throws InputValidationException {
        return null;
    }

    @Override
    public int collectDorsal(String creditCard, Long inscriptionId) throws
            InputValidationException, InstanceNotFoundException,
            ClientAlreadyCollectedException, ClientIncorrectCreditCardException {

        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            return client.collectDorsal(creditCard, inscriptionId);
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftIncorrectCreditCardException e) {
            throw new ClientIncorrectCreditCardException(e.getInscriptionId(), e.getMail());
        } catch (ThriftAlreadyCollectedException e) {
            throw new ClientAlreadyCollectedException(e.getInscriptionId(), e.getMail());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }
}
