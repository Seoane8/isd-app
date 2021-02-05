package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RaceToThriftRaceDtoConversor {

    public static Race toRace(ThriftRaceDto race) {
        return new Race(race.getRaceId(), race.getMaxParticipants(), race.getDescription(),
                Double.valueOf(race.getInscriptionPrice()).floatValue(), LocalDateTime.parse(race.getRaceDate()),
                race.getRaceLocation(), LocalDateTime.now(), race.getParticipants());
    }

    public static List<ThriftRaceDto> toThriftRaceDto(List<Race> races){
        List<ThriftRaceDto> dtos = new ArrayList<>(races.size());

        for (Race race : races){
            dtos.add(toThriftRaceDto(race));
        }

        return dtos;
     }

     public static ThriftRaceDto toThriftRaceDto(Race race){
        return new ThriftRaceDto(race.getRaceId(), race.getMaxParticipants(), race.getDescription(),
                race.getInscriptionPrice(), race.getRaceDate().toString(), race.getRaceLocation(),
                race.getParticipants());
     }
}
