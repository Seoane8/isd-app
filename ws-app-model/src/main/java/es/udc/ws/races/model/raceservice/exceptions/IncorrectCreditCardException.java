package es.udc.ws.races.model.raceservice.exceptions;

public class IncorrectCreditCardException extends Exception {
    Long inscriptionId;
    String mail;

    public IncorrectCreditCardException(Long inscriptionId, String mail){
        super("Inscription with ID='" + inscriptionId
                + "' of the user with mail " + mail
                + " was not done with given credit card");
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
