package es.udc.ws.races.model.inscription;

import java.time.LocalDateTime;
import java.util.Objects;

public class Inscription {

    private long inscriptionId;
    private long raceID;
    private String mail;
    private String credCardNumber;
    private LocalDateTime reservationDate;
    private int dorsal;
    private boolean dorsalCollected;
    private float price;

    public Inscription(long raceID, String mail, String credCardNumber, LocalDateTime reservationDate ,int dorsal, boolean dorsalCollected, float price) {
        this.raceID = raceID;
        this.mail = mail;
        this.credCardNumber = credCardNumber;
        this.reservationDate = reservationDate;
        this.dorsal = dorsal;
        this.dorsalCollected = dorsalCollected;
        this.price = price;
    }

    public Inscription(long inscriptionId, long raceID, String mail, String credCardNumber, LocalDateTime reservationDate ,int dorsal, boolean dorsalCollected, float price) {
        this(raceID, mail, credCardNumber, reservationDate ,dorsal, dorsalCollected, price);
        this.inscriptionId = inscriptionId;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscription that = (Inscription) o;
        return inscriptionId == that.inscriptionId &&
                raceID == that.raceID &&
                dorsal == that.dorsal &&
                dorsalCollected == that.dorsalCollected &&
                Float.compare(that.price, price) == 0 &&
                mail.equals(that.mail) &&
                credCardNumber.equals(that.credCardNumber) &&
                Objects.equals(reservationDate, that.reservationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscriptionId, raceID, mail, credCardNumber, reservationDate, dorsal, dorsalCollected, price);
    }
}
