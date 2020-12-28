package es.udc.ws.races.model.raceservice.exceptions;

public class AlreadyInscriptedException extends Exception {

    Long raceId;
    String mail;

    public AlreadyInscriptedException(Long raceId, String mail){
        super("User with mail " + mail + " already inscripted in race " + raceId);
        this.raceId = raceId;
        this.mail = mail;
    }

    public String getMail() {return mail;}

    public void setMail(String mail) {this.mail = mail;}

    public Long getRaceId() {return raceId;}

    public void setRaceId(Long raceId) {this.raceId = raceId;}
}
