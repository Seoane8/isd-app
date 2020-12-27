package es.udc.ws.races.model.raceservice.exceptions;

public class AlreadyCollectedException extends Exception {
    Long inscriptionId;
    String mail;

    public AlreadyCollectedException(Long inscriptionId, String mail){
        super("The dorsal corresponding to th "
                + "inscription with ID='" + inscriptionId
                + "' of the user with mail " + mail
                + " has already been collected previously");
        this.inscriptionId = inscriptionId;
        this.mail = mail;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
