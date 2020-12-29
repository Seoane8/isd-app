package es.udc.ws.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonToClientRaceDtoConversor {

    public static ObjectNode toObjectNode(ClientRaceDto race) throws IOException {

        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (race.getRaceId() != null) {
            raceObject.put("raceId", race.getRaceId());
        }
        raceObject.put("maxParticipants", race.getMaxParticipants()).
                put("description", race.getDescription()).
                put("inscriptionPrice", race.getInscriptionPrice()).
                put("raceDate", race.getRaceDate().toString()).
                put("raceLocation", race.getRaceLocation()).
                put("participants", race.getParticipants());


        return raceObject;
    }


    public static ClientRaceDto toClientRaceDto(InputStream jsonMovie) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMovie);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientRaceDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientRaceDto> toClientRaceDtos(InputStream jsonRaces) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRaces);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientRaceDto> racesDtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    racesDtos.add(toClientRaceDto(raceNode));
                }

                return racesDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientRaceDto toClientRaceDto(JsonNode raceNode) throws ParsingException {

            if (raceNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) raceNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                int maxParticipants = raceObject.get("maxParticipants").asInt();
                String description = raceObject.get("description").textValue().trim();
                float inscriptionPrice = raceObject.get("inscriptionPrice").floatValue();
                String raceDateString = raceObject.get("raceDate").textValue().trim();
                LocalDateTime raceDate = LocalDateTime.parse(raceDateString);
                String raceLocation = raceObject.get("raceLocation").textValue().trim();
                int participants = raceObject.get("participants").asInt();
                return new ClientRaceDto(raceId, maxParticipants, description, inscriptionPrice,
                        raceDate, raceLocation, participants);
            }


    }
}
