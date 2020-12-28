package es.udc.ws.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.client.service.ClientRaceService;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.client.service.rest.json.JsonToClientRaceDtoConversor;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

public class RestClientRaceService implements ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientMovieService.endpointAddress";
    private String endpointAddress;


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientRaceDto race) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientRaceDtoConversor.toObjectNode(race));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long addRace(ClientRaceDto race) throws InputValidationException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "races").
                    bodyStream(toInputStream(race), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getRaceId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Race findRace(Long raceID) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public List<ClientRaceDto> findRaces(LocalDate date, String city) throws InputValidationException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "bikes/?keywords="
                    + URLEncoder.encode(city, "UTF-8") + "&date=" + URLEncoder.encode(date.toString(),"UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRaceDtoConversor.toClientRaceDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Long addInscription(Long raceId, String mail, String creditCard) throws InputValidationException, InstanceNotFoundException, NoMoreInscriptionsAllowedException, InscriptionDateExpiredException, AlreadyInscriptedException {
        return null;
    }

    @Override
    public List<Inscription> findInscriptions(String mail) throws InputValidationException {
        return null;
    }

    @Override
    public int collectDorsal(String creditCard, Long inscriptionId) throws InputValidationException, InstanceNotFoundException, AlreadyCollectedException, IncorrectCreditCardException {
        return 0;
    }
}
