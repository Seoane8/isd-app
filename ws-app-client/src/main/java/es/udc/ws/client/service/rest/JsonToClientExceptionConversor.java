package es.udc.ws.client.service.rest;

import es.udc.ws.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {
    public static Exception fromNotFoundErrorCode(InputStream content) {
        return new InstanceNotFoundException(null, null);
    }

    public static Exception fromBadRequestErrorCode(InputStream content) {
        return new InputValidationException(null);
    }

    public static Exception fromGoneErrorCode(InputStream content) {
        return new ClientAlreadyCollectedException(null, null);
    }
}
