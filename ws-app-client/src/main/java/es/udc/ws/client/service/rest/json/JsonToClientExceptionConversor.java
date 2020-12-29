package es.udc.ws.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.client.service.exceptions.ClientIncorrectCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {
    public static Exception fromNotFoundErrorCode(InputStream content) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(content);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromBadRequestErrorCode(InputStream content) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(content);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else if (errorType.equals("IncorrectCreditCard")) {
                    return toIncorrectCreditCardException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static Exception toIncorrectCreditCardException(JsonNode rootNode) {
        Long inscriptionId = rootNode.get("inscriptionId").longValue();
        String mail = rootNode.get("mail").textValue();
        return new ClientIncorrectCreditCardException(inscriptionId, mail);
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromGoneErrorCode(InputStream content) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(content);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("DorsalAlreadyCollected")) {
                    return toAlreadyCollectedException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientAlreadyCollectedException toAlreadyCollectedException(JsonNode rootNode) {
        Long inscriptionId = rootNode.get("inscriptionId").longValue();
        String mail = rootNode.get("mail").textValue();
        return new ClientAlreadyCollectedException(inscriptionId, mail);
    }
}