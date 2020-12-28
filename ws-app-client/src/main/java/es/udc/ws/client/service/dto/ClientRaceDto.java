package es.udc.ws.client.service.dto;

import java.time.LocalDateTime;

public class ClientRaceDto {

    private Long raceId;
    private int maxParticipants;
    private String description;
    private float inscriptionPrice;
    private LocalDateTime raceDate;
    private String raceLocation;
    private int participants;


    public ClientRaceDto() {
    }

    public ClientRaceDto(long raceId,int maxParticipants, String description, float inscriptionPrice,
                       LocalDateTime raceDate, String raceLocation, int participants) {
        this.raceId = raceId;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.inscriptionPrice = inscriptionPrice;
        this.raceDate = raceDate;
        this.raceLocation = raceLocation;
        this.participants = participants;
    }


    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long id) {
        this.raceId = id;
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

    public void setRentDate(LocalDateTime raceDate) {
        this.raceDate = raceDate;
    }

    public String getRaceLocation() {
        return raceLocation;
    }

    public void setRaceLocation(String raceLocation) {
        this.raceLocation = raceLocation;
    }

    public int getParticipants() { return participants; }

    public void setParticipants(int participants) { this.participants = participants; }

    public int getAvailablePlaces(){return this.maxParticipants - this.participants;}

    @Override
    public String toString() {
        return "RaceDto [raceId=" + raceId + ", maxParticipants=" + maxParticipants
                + ", description=" + description
                + ", inscriptionPrice=" + inscriptionPrice + ", raceDate=" + raceDate
                + ", raceLocation=" + raceLocation + ", participants=" + participants
                + "]";
    }


}