package es.udc.ws.races.restservice.servlets;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.races.restservice.dto.RestRaceDto;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;
import es.udc.ws.races.restservice.json.JsonToRestRaceDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RacesServlet extends HttpServlet {

    //public final static String CONVERSION_PATTERN = "yyyy-MM-dd HH:mm:ss";
   // public final static SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }
        RestRaceDto raceDto;
        try {
            raceDto = JsonToRestRaceDtoConversor.toServiceRaceDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor
                    .toInputValidationException(new InputValidationException(ex.getMessage())), null);
            return;
        }
        Race race = RaceToRestRaceDtoConversor.toRace(raceDto);
        try {
            race = RaceServiceFactory.getService().addRace(race.getDescription(), race.getInscriptionPrice(), race.getRaceDate(), race.getMaxParticipants(), race.getRaceLocation());
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex), null);
            return;
        }
        raceDto = RaceToRestRaceDtoConversor.toRaceDto(race);

        String raceURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + race.getRaceId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", raceURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestRaceDtoConversor.toObjectNode(raceDto), headers);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null || path.length() == 0) {
           if (req.getParameter("city")==null || req.getParameter("date")==null) {
               ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                       JsonToExceptionConversor.toInputValidationException(
                               new InputValidationException("Invalid Request : parameters city and date can`t be null")),
                       null);
           return;
           }
            String city = req.getParameter("city");
            String dateString;
            List<Race> races = new ArrayList<>();

            if (!((dateString = req.getParameter("date")) == (null))) {
                    LocalDate date = LocalDate.parse(dateString);
                try {
                    races = RaceServiceFactory.getService().findRaces(date, city);
                } catch (InputValidationException e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(e), null);
                    return;
                }

                List<RestRaceDto> raceDtos = RaceToRestRaceDtoConversor.toRaceDtos(races);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestRaceDtoConversor.toArrayNode(raceDtos), null);
                return;
            }
        }

        String raceIdString = path.substring(1);
        Long raceId;

        try {
            raceId = Long.valueOf(raceIdString);
        } catch (NumberFormatException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: invalid race id '" + raceIdString + "'")),
                    null);

            return;
        }

        Race race;

        try {
            race = RaceServiceFactory.getService().findRace(raceId);
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(e),
                    null);
            return;
        }

        RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRaceDto(race);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
    }
}

