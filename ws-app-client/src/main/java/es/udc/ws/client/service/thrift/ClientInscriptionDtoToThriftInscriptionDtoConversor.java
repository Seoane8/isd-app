package es.udc.ws.client.service.thrift;

import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class ClientInscriptionDtoToThriftInscriptionDtoConversor {

    public static List<ClientInscriptionDto> toClientInscriptionDto(List<ThriftInscriptionDto> inscriptions){
        List<ClientInscriptionDto> dtos = new ArrayList<>(inscriptions.size());

        for(ThriftInscriptionDto inscription : inscriptions){
            dtos.add(toClientInscriptionDto(inscription));
        }

        return dtos;
    }

    public static ClientInscriptionDto toClientInscriptionDto(ThriftInscriptionDto inscription){
        return new ClientInscriptionDto(inscription.getInscriptionId(), inscription.getRaceId(), inscription.getMail(),
                inscription.getCreditCardNumber(), inscription.getDorsal(), inscription.isDorsalCollected());
    }
}
