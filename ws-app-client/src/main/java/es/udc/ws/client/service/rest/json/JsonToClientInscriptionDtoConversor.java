package es.udc.ws.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientInscriptionDtoConversor {
    public static ClientInscriptionDto toClientInscriptionDto(InputStream json) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientInscriptionDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientInscriptionDto> toClientInscriptionDtos(InputStream json) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientInscriptionDto> racesDtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    racesDtos.add(toClientInscriptionDto(raceNode));
                }

                return racesDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientInscriptionDto toClientInscriptionDto(JsonNode node) throws ParsingException {

        if (node.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode object = (ObjectNode) node;


            Long inscriptionId = object.get("inscriptionId").longValue();
            Long raceId = object.get("raceId").longValue();
            String mail = object.get("mail").textValue().trim();
            String creditCardNumber = object.get("creditCardNumber").textValue().trim();
            int dorsal = object.get("dorsal").intValue();
            boolean dorsalCollected = object.get("dorsalCollected").booleanValue();
            return new ClientInscriptionDto(inscriptionId, raceId, mail, creditCardNumber, dorsal, dorsalCollected);
        }
    }
}
