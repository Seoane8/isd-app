package es.udc.ws.race.restservice.dto;

import es.udc.ws.races.model.race.Race;

import java.util.ArrayList;
import java.util.List;


public class RacetoRestRaceDtoConversor {

    public static List<RestRaceDto> toRaceDtos(List<Race> races) {

        List<RestRaceDto> raceDtos = new ArrayList<>(races.size());

        for (int i = 0; i < races.size(); i++) {
            Race race = races.get(i);
            raceDtos.add(toRaceDto(race));
        }
        return raceDtos;
    }

    public static RestRaceDto toRaceDto(Race race) {
        return new RestRaceDto(race.getRaceId(), race.getMaxParticipants(), race.getDescription(), race.getInscriptionPrice(), race.getRaceDate(),race.getRaceLocation(),
        race.getCreationDate(),race.getParticipants());
    }

    public static Race toRace(RestRaceDto race) {
        return new  Race(race.getRaceId(), race.getMaxParticipants(), race.getDescription(), race.getInscriptionPrice(), race.getRaceDate(),race.getRaceLocation(),
                race.getCreationDate(),race.getParticipants());
    }







}
