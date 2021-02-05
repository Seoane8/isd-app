package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToThriftInscriptionDtoConversor {

    public static List<ThriftInscriptionDto> toThriftInscriptionDto(List<Inscription> inscriptions){
        List<ThriftInscriptionDto> dtos = new ArrayList<>(inscriptions.size());

        for (Inscription inscription : inscriptions){
            dtos.add(toThriftInscriptionDto(inscription));
        }

        return dtos;
    }

    public static ThriftInscriptionDto toThriftInscriptionDto(Inscription inscription){
        return new ThriftInscriptionDto(inscription.getInscriptionId(), inscription.getRaceID(), inscription.getMail(),
                inscription.getCredCardNumber(), inscription.getDorsal(), inscription.isDorsalCollected());
    }
}

