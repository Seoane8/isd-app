package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.restservice.dto.RestRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestRaceDtoConversor {

    public static ObjectNode toObjectNode(RestRaceDto race) {

        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (race.getRaceId() != null) {
            raceObject.put("raceId", race.getRaceId());
        }
        raceObject.put("maxParticipants", race.getMaxParticipants()).
                put("description", race.getDescription()).
                put("inscriptionPrice", race.getInscriptionPrice()).
                put("raceDate", race.getRaceDate()).
                put("raceLocation", race.getRaceLocation()).
                put("participants", race.getParticipants());


        return raceObject;
    }

    public static ArrayNode toArrayNode(List<RestRaceDto> races) {

        ArrayNode racesNode = JsonNodeFactory.instance.arrayNode();
        for (RestRaceDto raceDto : races) {
            ObjectNode raceObject = toObjectNode(raceDto);
            racesNode.add(raceObject);
        }

        return racesNode;
    }

    public static RestRaceDto toServiceRaceDto(InputStream jsonRace) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) rootNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                int maxParticipants = raceObject.get("maxParticipants").asInt();
                String description = raceObject.get("description").textValue().trim();
                float inscriptionPrice = raceObject.get("price").floatValue();
                String raceDate = raceObject.get("raceDate").textValue().trim();
                String raceLocation = raceObject.get("city").textValue().trim();
                JsonNode participantsNode = raceObject.get("participants");
                int participants = (participantsNode != null) ? participantsNode.asInt() : 0;
                return new RestRaceDto(raceId, maxParticipants, description, inscriptionPrice,
                        raceDate, raceLocation, participants);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
