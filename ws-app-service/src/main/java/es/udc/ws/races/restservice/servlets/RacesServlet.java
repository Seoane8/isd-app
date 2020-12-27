package es.udc.ws.races.restservice.servlets;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.races.restservice.dto.RestRaceDto;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;
import es.udc.ws.races.restservice.json.JsonToRestRaceDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RacesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: invalid race id")),
                    null);
            return;
        }

        String raceIdString = path.substring(1);
        Long raceId;

        try {
            raceId = Long.valueOf(raceIdString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: invalid race id '" + path + "'")),
                    null);

            return;
        }

        Race race;

        try {
            race = RaceServiceFactory.getService().findRace(raceId);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(ex),
                    null);
            return;
        }

        RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRaceDto(race);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
    }
}
