package es.udc.ws.raceService.serviceutil;

import es.udc.ws.raceService.dto.ServiceInscriptionDto;
import es.udc.ws.races.model.inscription.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToInscriptionDtoConversor {

    public static ServiceInscriptionDto toInscriptionDto(Inscription inscription) {
        return new ServiceInscriptionDto(inscription.getInscriptionId(),inscription.getRaceID(),inscription.getMail(), inscription.getCredCardNumber(), inscription.getReservationDate(), inscription.getDorsal(),
                inscription.isDorsalCollected(), inscription.getPrice());
    }

    public static List<ServiceInscriptionDto> toInscriptionDtos(List<Inscription> inscriptions){
        List<ServiceInscriptionDto> reservationDtos = new ArrayList<ServiceInscriptionDto>();

        for(Inscription res: inscriptions){
            ServiceInscriptionDto reservationDto = InscriptionToInscriptionDtoConversor.toInscriptionDto(res);
            reservationDtos.add(reservationDto);
        }

        return reservationDtos;
    }


}
