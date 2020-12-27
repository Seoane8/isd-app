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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class JsonToRestRaceDtoConversor {

    public final static String CONVERSION_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);

    public static ObjectNode toObjectNode(RestRaceDto race) {

        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (race.getRaceId() != null) {
            raceObject.put("raceId", race.getRaceId());
        }
        raceObject.put("maxParticipants", race.getMaxParticipants()).
                put("description", race.getDescription()).
                put("inscriptionPrice", race.getInscriptionPrice()).
                put("raceDate", getRaceDate(race.getRaceDate())).
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
                String raceDateString = raceObject.get("raceDate").textValue().trim();
                LocalDateTime raceDate = LocalDateTime.parse(raceDateString);
                String raceLocation = raceObject.asText().trim();
                int participants = raceObject.get("participants").asInt();
                return new RestRaceDto(raceId, maxParticipants, description, inscriptionPrice,
                        raceDate, raceLocation, participants);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static String getRaceDate(LocalDateTime raceDate) {

        return sdf.format(raceDate);

    }
}
