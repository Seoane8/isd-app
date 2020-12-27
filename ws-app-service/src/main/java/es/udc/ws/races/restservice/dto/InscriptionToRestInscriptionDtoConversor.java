package es.udc.ws.races.restservice.dto;

import es.udc.ws.races.model.inscription.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToRestInscriptionDtoConversor {

    public static RestInscriptionDto toInscriptionDto(Inscription inscription) {
        return new RestInscriptionDto(inscription.getInscriptionId(),inscription.getRaceID(),inscription.getMail(), inscription.getCredCardNumber(), inscription.getReservationDate(), inscription.getDorsal(),
                inscription.isDorsalCollected(), inscription.getPrice());
    }

    public static List<RestInscriptionDto> toInscriptionDtos(List<Inscription> inscriptions){
        List<RestInscriptionDto> reservationDtos = new ArrayList<RestInscriptionDto>();

        for(Inscription res: inscriptions){
            RestInscriptionDto reservationDto = InscriptionToRestInscriptionDtoConversor.toInscriptionDto(res);
            reservationDtos.add(reservationDto);
        }

        return reservationDtos;
    }


}
