package es.udc.ws.client.service.thrift;

import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.races.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientRaceDtoToThriftRaceDtoConversor {

    public static ThriftRaceDto toThriftRaceDto(ClientRaceDto race){
        return new ThriftRaceDto(race.getRaceId(), race.getMaxParticipants(), race.getDescription(),
                race.getInscriptionPrice(), race.getRaceDate().toString(), race.getRaceLocation(),
                race.getParticipants());
    }

    public static List<ClientRaceDto> toClientRaceDto(List<ThriftRaceDto> races){
        List<ClientRaceDto> dtos = new ArrayList<>(races.size());

        for (ThriftRaceDto race : races){
            dtos.add(toClientRaceDto(race));
        }

        return dtos;
    }

    public static ClientRaceDto toClientRaceDto(ThriftRaceDto race){
        return new ClientRaceDto(race.getRaceId(), race.getMaxParticipants(), race.getDescription(),
                Double.valueOf(race.getInscriptionPrice()).floatValue(), LocalDateTime.parse(race.getRaceDate()),
                race.getRaceLocation(), race.getParticipants());
    }

}
