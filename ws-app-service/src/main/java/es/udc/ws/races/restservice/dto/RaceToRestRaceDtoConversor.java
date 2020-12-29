package es.udc.ws.races.restservice.dto;

import es.udc.ws.races.model.race.Race;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RaceToRestRaceDtoConversor {

    public static List<RestRaceDto> toRaceDtos(List<Race> races) {

        List<RestRaceDto> raceDtos = new ArrayList<>(races.size());

        for (Race race : races) {
            raceDtos.add(toRaceDto(race));
        }
        return raceDtos;
    }

    public static RestRaceDto toRaceDto(Race race) {
        return new RestRaceDto(race.getRaceId(), race.getMaxParticipants(),
                race.getDescription(), race.getInscriptionPrice(), race.getRaceDate().toString(),
                race.getRaceLocation(),race.getParticipants());
    }

    public static Race toRace(RestRaceDto race) {
        return new  Race(race.getRaceId(), race.getMaxParticipants(), race.getDescription(),
                race.getInscriptionPrice(), LocalDateTime.parse(race.getRaceDate()), race.getRaceLocation(),
                LocalDateTime.now().withNano(0) ,race.getParticipants());
    }







}
