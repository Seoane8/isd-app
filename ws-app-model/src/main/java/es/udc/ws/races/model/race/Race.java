package es.udc.ws.races.model.race;

import java.time.LocalDateTime;

public class Race {

 private Long raceId;
 private int maxParticipants;
 private String description;
 private float inscriptionPrice;
 private LocalDateTime raceDate;
 private String raceLocation;
 private LocalDateTime creationDate;
 private int participants;

    public Race(int maxParticipants, String description, float inscriptionPrice, LocalDateTime raceDate, String raceLocation,int participants) {
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.inscriptionPrice = inscriptionPrice;
        this.raceDate = raceDate;
        this.raceLocation = raceLocation;
        this.participants = participants;
    }
    public Race(Long raceId , int maxParticipants, String description, float inscriptionPrice, LocalDateTime raceDate, String raceLocation,LocalDateTime creationDate,int participants){
        this(maxParticipants, description, inscriptionPrice, raceDate, raceLocation,participants);
        this.raceId = raceId;
        this.creationDate = creationDate;
    }

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getInscriptionPrice() {
        return inscriptionPrice;
    }

    public void setInscriptionPrice(float inscriptionPrice) {
        this.inscriptionPrice = inscriptionPrice;
    }

    public LocalDateTime getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(LocalDateTime raceDate) {
        this.raceDate = raceDate;
    }

    public String getRaceLocation() {
        return raceLocation;
    }

    public void setRaceLocation(String raceLocation) {
        this.raceLocation = raceLocation;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race)) return false;

        Race race = (Race) o;

        if (getMaxParticipants() != race.getMaxParticipants()) return false;
        if (Float.compare(race.getInscriptionPrice(), getInscriptionPrice()) != 0) return false;
        if (getParticipants() != race.getParticipants()) return false;
        if (getRaceId() != null ? !getRaceId().equals(race.getRaceId()) : race.getRaceId() != null) return false;
        if (getDescription() != null ? !getDescription().equals(race.getDescription()) : race.getDescription() != null)
            return false;
        if (getRaceDate() != null ? !getRaceDate().equals(race.getRaceDate()) : race.getRaceDate() != null)
            return false;
        if (getRaceLocation() != null ? !getRaceLocation().equals(race.getRaceLocation()) : race.getRaceLocation() != null)
            return false;
        return creationDate != null ? creationDate.equals(race.creationDate) : race.creationDate == null;
    }

    @Override
    public int hashCode() {
        int result = getRaceId() != null ? getRaceId().hashCode() : 0;
        result = 31 * result + getMaxParticipants();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getInscriptionPrice() != +0.0f ? Float.floatToIntBits(getInscriptionPrice()) : 0);
        result = 31 * result + (getRaceDate() != null ? getRaceDate().hashCode() : 0);
        result = 31 * result + (getRaceLocation() != null ? getRaceLocation().hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + getParticipants();
        return result;
    }
}
