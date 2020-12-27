package es.udc.ws.races.restservice.servlets;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.model.raceservice.exceptions.AlreadyCollectedException;
import es.udc.ws.races.model.raceservice.exceptions.IncorrectCreditCardException;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;

public class InscriptionsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: invalid race id")),
                    null);
            return;
        }

        String[] pathList = path.split("[/]");

        if(pathList.length != 3 || !pathList[2].equals("collect")){
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String inscriptionIdString = pathList[1];
        Long inscriptionId;

        try {
            inscriptionId = Long.valueOf(inscriptionIdString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: invalid race id '" + inscriptionIdString + "'")),
                    null);

            return;
        }

        String creditCardNumber = req.getParameter("creditCardNumber");

        if(creditCardNumber == null){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: parameter 'creditCardNumber' is required")),
                    null);

            return;
        }

        int dorsal;

        try {
            dorsal = RaceServiceFactory.getService().collectDorsal(creditCardNumber, inscriptionId);
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                JsonToExceptionConversor.toInstanceNotFoundException(e),
                null);
            return;
        } catch (AlreadyCollectedException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                JsonToExceptionConversor.toAlreadyCollectedException(e),
                null);
            return;
        } catch (IncorrectCreditCardException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                JsonToExceptionConversor.toIncorrectCreditCardException(e),
                null);
            return;
        } catch (InputValidationException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                JsonToExceptionConversor.toInputValidationException(e),
                null);
            return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
            JsonNodeFactory.instance.objectNode().put("dorsal", dorsal),
            null);
    }
}
