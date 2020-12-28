package es.udc.ws.client.service.exceptions;

public class ClientNoMoreInscriptionsAllowedException extends Exception {

    Long raceId;

    public ClientNoMoreInscriptionsAllowedException(Long raceId){
        super("No more inscriptions allowed in race" + raceId);
        this.raceId = raceId;
    }

    public Long getRaceId() {return raceId;}

    public void setRaceId(Long raceId) {this.raceId = raceId;}
}
