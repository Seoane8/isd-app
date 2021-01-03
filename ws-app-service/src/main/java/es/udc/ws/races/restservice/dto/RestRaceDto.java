package es.udc.ws.races.restservice.dto;

public class RestRaceDto {

    private Long raceId;
    private int maxParticipants;
    private String description;
    private float inscriptionPrice;
    private String raceDate;
    private String raceLocation;
    private int participants;


    public RestRaceDto() {
    }

    public RestRaceDto(int maxParticipants, String description, float inscriptionPrice,
                       String raceDate, String raceLocation) {
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.inscriptionPrice = inscriptionPrice;
        this.raceDate = raceDate;
        this.raceLocation = raceLocation;
    }
    public RestRaceDto(Long raceId , int maxParticipants, String description, float inscriptionPrice,
                       String raceDate, String raceLocation, int participants){
        this(maxParticipants, description, inscriptionPrice, raceDate, raceLocation);
        this.raceId = raceId;
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

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
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

    @Override
    public String toString() {
        return "RaceDto [raceId=" + raceId + ", maxParticipants=" + maxParticipants
                + ", description=" + description
                + ", inscriptionPrice=" + inscriptionPrice + ", raceDate=" + raceDate
                + ", raceLocation=" + raceLocation + ", participants=" + participants
                + "]";
    }


}

