package es.udc.ws.client.service.exceptions;

public class ClientAlreadyInscriptedException extends Exception {

    Long raceId;
    String mail;

    public ClientAlreadyInscriptedException(Long raceId, String mail){
        super("User with mail " + mail + " already inscripted in race " + raceId);
        this.raceId = raceId;
        this.mail = mail;
    }

    public String getMail() {return mail;}

    public void setMail(String mail) {this.mail = mail;}

    public Long getRaceId() {return raceId;}

    public void setRaceId(Long raceId) {this.raceId = raceId;}
}
