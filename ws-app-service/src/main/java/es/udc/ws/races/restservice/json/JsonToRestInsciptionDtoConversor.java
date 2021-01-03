package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.restservice.dto.RestInscriptionDto;

import java.util.List;

public class JsonToRestInsciptionDtoConversor {

        public static JsonNode toJsonObject(RestInscriptionDto inscription) {

            ObjectNode inscriptionNode = JsonNodeFactory.instance.objectNode();


                inscriptionNode.put("inscriptionId", inscription.getInscriptionId());


            inscriptionNode.put("raceId", inscription.getRaceID()).
                    put("mail", inscription.getMail()).
                    put("creditCardNumber", inscription.getCredCardNumber()).
                    put("dorsal", inscription.getDorsal()).
                    put("dorsalCollected", inscription.isDorsalCollected());

            return inscriptionNode;
        }

        public static ArrayNode toArrayJsonObject(List<RestInscriptionDto> inscriptions) {
            ArrayNode inscriptionsObject = JsonNodeFactory.instance.arrayNode();
            for (RestInscriptionDto inscriptionDto : inscriptions) {
                JsonNode inscriptionObject = toJsonObject(inscriptionDto);
                inscriptionsObject.add(inscriptionObject);
            }

            return inscriptionsObject;
        }
}




