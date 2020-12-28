package es.udc.ws.races.restservice.json;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.races.restservice.dto.RestInscriptionDto;

public class JsonToRestInsciptionDtoConversor {

        public final static String CONVERSION_PATTERN = "yyyy-MM-dd HH:mm:ss";
        public final static SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);

        public static JsonNode toJsonObject(RestInscriptionDto inscription) {

            ObjectNode inscriptionNode = JsonNodeFactory.instance.objectNode();


                inscriptionNode.put("inscriptionId", inscription.getInscriptionId());


            inscriptionNode.put("raceId", inscription.getRaceID()).
                    put("mail", inscription.getMail()).
                    put("creditCardNumber", inscription.getCredCardNumber()).
                    put("price", inscription.getPrice()).
                    put("dorsal", inscription.getDorsal()).
                    put("dosalCollected", inscription.isDorsalCollected()).
                    put("reservationDate", inscription.getReservationDate());

            return inscriptionNode;
        }

        public static ArrayNode toArrayJsonObject(List<RestInscriptionDto> inscriptions) {
            ArrayNode inscriptionsObject = JsonNodeFactory.instance.arrayNode();
            for (int i = 0; i < inscriptions.size(); i++) {
                RestInscriptionDto inscriptionDto = inscriptions.get(i);
                JsonNode inscriptionObject = toJsonObject(inscriptionDto);
                inscriptionsObject.add(inscriptionObject);
            }

            return inscriptionsObject;
        }
}




