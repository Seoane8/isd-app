package es.udc.ws.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.client.service.ClientRaceService;
import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.client.service.exceptions.*;
import es.udc.ws.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.client.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.client.service.rest.json.JsonToClientRaceDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
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

    private void validateStatusCode(int successCode, HttpResponse response)
            throws Exception {

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
    public Long addRace(ClientRaceDto race)
            throws InputValidationException {
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
    public ClientRaceDto findRace(Long raceID)
            throws InstanceNotFoundException {

        try{

            HttpResponse response = Request.Get(getEndpointAddress() + "races/" + raceID).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent());

        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientRaceDto> findRaces(LocalDate date, String city)
            throws InputValidationException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "races/?city="
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
    public Long addInscription(Long raceId, String mail, String creditCard)
            throws InputValidationException, InstanceNotFoundException, ClientNoMoreInscriptionsAllowedException,
            ClientInscriptionDateExpiredException, ClientAlreadyInscriptedException {

            try{
                HttpResponse response =
                    Request.Post(getEndpointAddress() + "inscriptions").
                            bodyForm(Form.form().
                                    add("race", Long.toString(raceId)).
                                    add("mail", mail).
                                    add("creditCard", creditCard).
                                    build()).
                            execute().returnResponse();

                validateStatusCode(HttpStatus.SC_CREATED, response);

                ObjectMapper mapper = ObjectMapperFactory.instance();
                JsonNode jsonMap = mapper.readTree(response.getEntity().getContent());
                if (jsonMap.getNodeType() != JsonNodeType.OBJECT) {
                    throw new ParsingException("Unrecognized JSON (object expected)");
                }
                return jsonMap.get("inscriptionID").asLong();

            } catch (InputValidationException | InstanceNotFoundException | ClientNoMoreInscriptionsAllowedException |
        ClientInscriptionDateExpiredException | ClientAlreadyInscriptedException e){
                throw e;
            } catch (Exception e){
                throw new RuntimeException(e);
            }
    }

    @Override
    public List<ClientInscriptionDto> findInscriptions(String mail)
            throws InputValidationException {
        try{
            HttpResponse response =
                    Request.Get(getEndpointAddress() + "inscriptions?mail="
                            + URLEncoder.encode(mail, "UTF-8")).
                            execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDtos(
                    response.getEntity().getContent());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int collectDorsal(String creditCard, Long inscriptionId)
            throws InputValidationException, InstanceNotFoundException,
            ClientAlreadyCollectedException, ClientIncorrectCreditCardException {

        try{

            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions/" + inscriptionId + "/collect").
                    bodyForm(Form.form().add("creditCardNumber", creditCard).build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            ObjectMapper mapper = ObjectMapperFactory.instance();
            JsonNode jsonMap = mapper.readTree(response.getEntity().getContent());
            if (jsonMap.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            }
            return jsonMap.get("dorsal").asInt();

        } catch (InputValidationException | InstanceNotFoundException |
                ClientAlreadyCollectedException | ClientIncorrectCreditCardException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
