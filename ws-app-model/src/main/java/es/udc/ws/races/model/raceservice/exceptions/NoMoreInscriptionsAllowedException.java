package es.udc.ws.races.model.raceservice.exceptions;

public class NoMoreInscriptionsAllowedException extends Exception {

    Long raceId;

    public NoMoreInscriptionsAllowedException(Long raceId){
        super("No more inscriptions allowed in race" + raceId);
        this.raceId = raceId;
    }

    public Long getRaceId() {return raceId;}

    public void setRaceId(Long raceId) {this.raceId = raceId;}
}
