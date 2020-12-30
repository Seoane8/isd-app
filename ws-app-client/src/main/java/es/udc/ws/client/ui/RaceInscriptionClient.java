package es.udc.ws.client.ui;

import es.udc.ws.client.service.ClientRaceService;
import es.udc.ws.client.service.ClientRaceServiceFactory;
import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.client.service.exceptions.ClientAlreadyInscriptedException;
import es.udc.ws.client.service.exceptions.ClientInscriptionDateExpiredException;
import es.udc.ws.client.service.exceptions.ClientNoMoreInscriptionsAllowedException;
import es.udc.ws.client.service.rest.RestClientRaceService;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RaceInscriptionClient {

        public static void main(String[] args) {

            if(args.length == 0) {
                printUsageAndExit();
            }
            ClientRaceService clientRaceService =
                    ClientRaceServiceFactory.getService();
            if("-a".equalsIgnoreCase(args[0])) {
              validateArgs(args, 7, new int[] { 1, 3, 6}, new int[] {});

                // [add] RaceInscriptionClient -a <maxParticipants> <description> <inscriptionPrice> <raceDate> <raceLocation> <participants>

                try {
                    ClientRaceDto client = new ClientRaceDto(null,
                            Integer.valueOf(args[1]),args[2], Float.valueOf(args[3]),LocalDateTime.parse(args[4])
                            , args[5], Integer.valueOf(args[6]));

                    Long raceId = clientRaceService.addRace(client);


                    System.out.println("Race " + raceId + " created sucessfully");

                } catch (NumberFormatException | InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-findRace".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {1}, new int[] {});

                // [findRace] RaceInscriptionClient -findRace <movieId>

                try {
                    ClientRaceDto race = clientRaceService.findRace(Long.parseLong(args[1]));

                    System.out.println("Race with ID: " + race.getRaceId() + "\n" +
                                        "Location: " + race.getRaceLocation() + "\n" +
                                        "Description: " + race.getDescription() + "\n" +
                                        "Places: " + race.getAvailablePlaces() + "\n" +
                                        "Price: " + race.getInscriptionPrice());

                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }

            } else if("-f".equalsIgnoreCase(args[0])) {
                validateArgs(args, 3, new int[] {}, new int[] {});

                // [findRaces] RaceInscriptionClient -f <date> <city>

                try {
                    List<ClientRaceDto> races = clientRaceService.findRaces(LocalDate.parse(args[1]),args[2]);
                    System.out.println("Found " + races.size() +
                            " races(s) before " + args[1] + "and in " + args[2]);
                    for (int i = 0; i < races.size(); i++) {
                        ClientRaceDto raceDto = races.get(i);
                        System.out.println("Id: " + raceDto.getRaceId() +
                                ", Description: " + raceDto.getDescription() +
                                ", participants: " + raceDto.getParticipants() +
                                ", Description: " + raceDto.getDescription() +
                                ", Location: " + raceDto.getRaceLocation() +
                                ", Price: " + raceDto.getInscriptionPrice());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-addInscription".equalsIgnoreCase(args[0])) {
                validateArgs(args, 4, new int[] {1}, new int[] {});

                // [addInscription RaceServiceClient -addInscription <raceId> <mail> <creditCard>

                Long inscriptionId;
                try {
                    inscriptionId = clientRaceService.addInscription(Long.parseLong(args[1]),args[2],args[3]);

                    System.out.println("Succesfully inscripted to race " + clientRaceService.findRace(Long.parseLong(args[1])).getDescription() +
                            " with inscription ID " +
                            inscriptionId);

                } catch (InputValidationException | InstanceNotFoundException | ClientNoMoreInscriptionsAllowedException |
                        ClientInscriptionDateExpiredException | ClientAlreadyInscriptedException ex) {
                    ex.printStackTrace(System.err);
                }

            /*} else if("-g".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {1});

                // [get] MovieServiceClient -g <saleId>

                try {
                    String movieURL =
                            clientMovieService.getMovieUrl(Long.parseLong(args[1]));

                    System.out.println("The URL for the sale " + args[1] +
                            " is " + movieURL);
                } catch (NumberFormatException | InstanceNotFoundException | ClientSaleExpirationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }*/

            } else if("-findInscriptions".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {}, new int[] {});

                // [findInscriptions] RaceInscriptionClient -findInscriptions <mail>
                
                try {
                    List<ClientInscriptionDto> inscriptions = clientRaceService.findInscriptions(args[1]);
                    System.out.println("Found " + inscriptions.size() + " inscriptions for mail " + args[1]);
                    for (int i = 0; i < inscriptions.size(); i++) {
                        ClientInscriptionDto inscriptionDto = inscriptions.get(i);
                        ClientRaceDto race = clientRaceService.findRace(inscriptionDto.getRaceID());
                        System.out.println("InscriptionId: " + inscriptionDto.getInscriptionId() +
                                ", RaceId: " + race.getRaceId() +
                                ", Description: " + race.getDescription() +
                                ", Dorsal: " + inscriptionDto.getDorsal() +
                                ", participants: " + race.getParticipants() +
                                ", Description: " + race.getDescription() +
                                ", Location: " + race.getRaceLocation() +
                                ", Date: " + race.getRaceDate().toString() +
                                ", Price: " + race.getInscriptionPrice());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-collectDorsal".equalsIgnoreCase(args[0])) {
                validateArgs(args, 3, new int[] {2}, new int[] {});

                // [collectDorsal] RaceInscriptionClient -collectDorsal <creditCard> <inscriptionId>

                try {
                    int dorsal = clientRaceService.collectDorsal(args[1], Long.parseLong(args[2]));

                    System.out.println("The dorsal is: " + dorsal);

                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            } else printUsageAndExit();

        }

        public static void validateArgs(String[] args, int expectedArgs,
                                        int[] numericArguments,int[] datesArguments) {
            if(expectedArgs != args.length) {
                printUsageAndExit();
            }
            for(int i = 0 ; i< numericArguments.length ; i++) {
                int position = numericArguments[i];
                try {
                    Double.parseDouble(args[position]);
                } catch(NumberFormatException n) {
                    printUsageAndExit();
                }
            }
            for (int j = 0; j < datesArguments.length; j++) {
               int position = datesArguments[j];
                try {
                    LocalDateTime.parse(args[position]);
                } catch (DateTimeException d) {
                    printUsageAndExit();
                }
            }
        }



        public static void printUsageAndExit() {
            printUsage();
            System.exit(-1);
        }

        public static void printUsage() {
            System.err.println("Usage:\n" +
                    "    [add]              -a <maxParticipants> <description> <price> <date> <city> <participants>\n" +
                    "    [findRace]         -findRace <raceId>\n" +
                    "    [findRaces]        -f <date> <city>\n" +
                    "    [addInscription]   -addInscription <raceId> <mail> <creditCard>\n" +
                    "    [findInscriptions] -findInscriptions <mail>\n" +
                    "    [collectDorsal]    -collectDorsal <creditCard> <inscriptionId>\n");
        }
}
