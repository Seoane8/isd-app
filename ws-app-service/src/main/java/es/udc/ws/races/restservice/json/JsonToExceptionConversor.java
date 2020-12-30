package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonToExceptionConversor {
    public static JsonNode toInputValidationException(InputValidationException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InputValidation");
        exceptionObject.put("message", e.getMessage());

        return exceptionObject;
    }

    public static JsonNode toInstanceNotFoundException(InstanceNotFoundException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InstanceNotFound");
        exceptionObject.put("instanceId", (e.getInstanceId() != null) ?
                e.getInstanceId().toString() : null);
        exceptionObject.put("instanceType",
                e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));

        return exceptionObject;
    }

    public static JsonNode toAlreadyCollectedException(AlreadyCollectedException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "DorsalAlreadyCollected");
        exceptionObject.put("inscriptionId", (e.getInscriptionId() != null) ?
                e.getInscriptionId().toString() : null);
        exceptionObject.put("mail", e.getMail());

        return exceptionObject;
    }

    public static JsonNode toIncorrectCreditCardException(IncorrectCreditCardException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "IncorrectCreditCard");
        exceptionObject.put("inscriptionId", (e.getInscriptionId() != null) ?
                e.getInscriptionId().toString() : null);
        exceptionObject.put("mail", e.getMail());

        return exceptionObject;
    }

    public static JsonNode toNoMoreInscriptionsAllowedException(NoMoreInscriptionsAllowedException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NoMoreInscriptionsAllowed");
        exceptionObject.put("raceId", (e.getRaceId() != null) ?
                e.getRaceId().toString() : null);
        return exceptionObject;
    }

    public static JsonNode toInscriptionDateExpiredException(InscriptionDateExpiredException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "InscriptionDateExpired");
        exceptionObject.put("raceId", (e.getRaceId() != null) ?
                e.getRaceId().toString() : null);
        exceptionObject.put("date", (e.getRaceDate() != null) ?
                e.getRaceDate().toString() : null);
        return exceptionObject;
    }

    public static JsonNode toAlreadyInscriptedException(AlreadyInscriptedException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "AlreadyInscripted");
        exceptionObject.put("raceId", (e.getRaceId() != null) ?
                e.getRaceId().toString() : null);
        exceptionObject.put("mail", e.getMail());
        return exceptionObject;
    }
}
