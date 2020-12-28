package es.udc.ws.races.model.raceservice.exceptions;

import java.time.LocalDateTime;

public class InscriptionDateExpiredException extends Exception {

    Long raceId;
    LocalDateTime raceDate;

    public InscriptionDateExpiredException(Long raceId, LocalDateTime raceDate){
        super("No inscriptions allowed in race" + raceId + " 24h before " + raceDate.toString());
        this.raceId = raceId;
        this.raceDate = raceDate;
    }

    public LocalDateTime getRaceDate(){return raceDate;}

    public void setRaceDate(LocalDateTime raceDate){this.raceDate = raceDate;}


    public Long getRaceId() {return raceId;}

    public void setRaceId(Long raceId) {this.raceId = raceId;}

}
