package es.udc.ws.raceService.serviceutil;

import es.udc.ws.raceService.dto.ServiceRaceDto;

import es.udc.ws.races.model.race.Race;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RacetoRaceDtoConversor {

    public static List<ServiceRaceDto> toRaceDto(List<Race> races) {
        List<ServiceRaceDto> raceDtos = new ArrayList<>(races.size());
        for (int i = 0; i < races.size(); i++) {
            Race race = races.get(i);
            raceDtos.add(toRaceDto(race));
        }
        return raceDtos;
    }
    public static ServiceRaceDto toRaceDto(Race race) {
        return new  ServiceRaceDto(race.getRaceId(), race.getMaxParticipants(), race.getDescription(), race.getInscriptionPrice(), race.getRaceDate(),race.getRaceLocation(),
        race.getCreationDate(),race.getParticipants());
    }

    public static Race toRace(ServiceRaceDto race) {
        return new  Race(race.getRaceId(), race.getMaxParticipants(), race.getDescription(), race.getInscriptionPrice(), race.getRaceDate(),race.getRaceLocation(),
                race.getCreationDate(),race.getParticipants());
    }







}
