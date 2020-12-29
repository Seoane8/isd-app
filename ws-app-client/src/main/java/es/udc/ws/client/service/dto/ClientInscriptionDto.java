package es.udc.ws.client.service.dto;

import java.time.LocalDateTime;

public class ClientInscriptionDto {

    private long inscriptionId;
    private long raceID;
    private String mail;
    private String credCardNumber;
    private int dorsal;
    private boolean dorsalCollected;

    public ClientInscriptionDto(){}

    public ClientInscriptionDto(long inscriptionId,long raceID, String mail, String credCardNumber,
                                int dorsal, boolean dorsalCollected) {
        this.inscriptionId = inscriptionId;
        this.raceID = raceID;
        this.mail = mail;
        this.credCardNumber = credCardNumber;
        this.dorsal = dorsal;
        this.dorsalCollected = dorsalCollected;
    }



    public long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public long getRaceID() {
        return raceID;
    }

    public void setRaceID(long raceID) {
        this.raceID = raceID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCredCardNumber() {
        return credCardNumber;
    }

    public void setCredCardNumber(String credCardNumber) {
        this.credCardNumber = credCardNumber;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public boolean isDorsalCollected() {
        return dorsalCollected;
    }

    public void setDorsalCollected(boolean dorsalCollected) {
        this.dorsalCollected = dorsalCollected;
    }

    @Override
    public String toString() {
        return "InscriptionDto [inscriptionId=" + inscriptionId + ", raceId=" + raceID
                + ", mail=" + mail + ",creditCardNumber=" + credCardNumber
                + ", dorsal=" + dorsal
                + "]";

    }
}
