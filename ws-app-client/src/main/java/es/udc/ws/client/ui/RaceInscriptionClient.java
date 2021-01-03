package es.udc.ws.client.ui;

import es.udc.ws.client.service.ClientRaceService;
import es.udc.ws.client.service.ClientRaceServiceFactory;
import es.udc.ws.client.service.dto.ClientInscriptionDto;
import es.udc.ws.client.service.dto.ClientRaceDto;
import es.udc.ws.client.service.exceptions.*;
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
            if("-addRace".equalsIgnoreCase(args[0])) {
              validateArgs(args, 6, new int[] { 1, 3}, new int[] {});

                // [add] RaceInscriptionClient -addRace <maxParticipants> <description> <inscriptionPrice> <raceDate> <raceLocation>

                try {
                    ClientRaceDto client = new ClientRaceDto(null,
                            Integer.parseInt(args[1]),args[2], Float.parseFloat(args[3]),LocalDateTime.parse(args[4])
                            , args[5], 0);

                    Long raceId = clientRaceService.addRace(client);


                    System.out.println("Race " + raceId + " created successfully");

                } catch (InputValidationException ex) {
                    System.err.println(ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-findRace".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {1}, new int[] {});

                // [findRace] RaceInscriptionClient -findRace <movieId>

                try {
                    ClientRaceDto race = clientRaceService.findRace(Long.parseLong(args[1]));

                    printRace(race);

                } catch (InstanceNotFoundException e) {
                    System.err.println(e.getMessage());
                }catch (Exception e) {
                    e.printStackTrace(System.err);
                }

            } else if("-findRaces".equalsIgnoreCase(args[0])) {
                validateArgs(args, 3, new int[] {}, new int[] {});

                // [findRaces] RaceInscriptionClient -findRaces <date> <city>

                try {
                    List<ClientRaceDto> races = clientRaceService.findRaces(LocalDate.parse(args[1]),args[2]);
                    System.out.println("Found " + races.size() +
                            " races(s) before " + args[1] + "and in " + args[2]);
                    for (ClientRaceDto raceDto : races) {
                        printRace(raceDto);
                    }
                } catch (InputValidationException ex) {
                    System.err.println(ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-addInscription".equalsIgnoreCase(args[0])) {
                validateArgs(args, 4, new int[] {1}, new int[] {});

                // [addInscription RaceServiceClient -addInscription <raceId> <mail> <creditCard>

                Long inscriptionId;
                try {
                    inscriptionId = clientRaceService.addInscription(Long.parseLong(args[1]),args[2],args[3]);

                    System.out.println("Successfully inscribed to race " + clientRaceService.findRace(Long.parseLong(args[1])).getDescription() +
                            " with inscription ID " +
                            inscriptionId + "\n");

                } catch (InputValidationException | InstanceNotFoundException | ClientNoMoreInscriptionsAllowedException |
                        ClientInscriptionDateExpiredException | ClientAlreadyInscriptedException ex) {
                    System.err.println(ex.getMessage());
                }
                catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            
	    } else if("-findInscriptions".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {}, new int[] {});

                // [findInscriptions] RaceInscriptionClient -findInscriptions <mail>
                
                try {
                    List<ClientInscriptionDto> inscriptions = clientRaceService.findInscriptions(args[1]);
                    System.out.println("Found " + inscriptions.size() + " inscriptions for mail " + args[1] + "\n");
                    for (ClientInscriptionDto inscriptionDto : inscriptions) {
                        ClientRaceDto race = clientRaceService.findRace(inscriptionDto.getRaceID());
                        System.out.println("InscriptionId: " + inscriptionDto.getInscriptionId() + "\n" +
                                "- RaceId: " + race.getRaceId() + "\n" +
                                "- Description: " + race.getDescription() + "\n" +
                                "- Dorsal: " + inscriptionDto.getDorsal() + "\n" +
                                "- participants: " + race.getParticipants() + "\n" +
                                "- Location: " + race.getRaceLocation() + "\n" +
                                "- Date: " + race.getRaceDate().toString() + "\n" +
                                "- Price: " + race.getInscriptionPrice() + "\n");
                    }
                }catch(InputValidationException ex){
                    System.err.println(ex.getMessage());
                }
                catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if("-collectDorsal".equalsIgnoreCase(args[0])) {
                validateArgs(args, 3, new int[] {2}, new int[] {});

                // [collectDorsal] RaceInscriptionClient -collectDorsal <creditCard> <inscriptionId>

                try {
                    int dorsal = clientRaceService.collectDorsal(args[1], Long.parseLong(args[2]));

                    System.out.println("The dorsal is: " + dorsal);

                } catch (InputValidationException | InstanceNotFoundException |
                        ClientAlreadyCollectedException | ClientIncorrectCreditCardException e) {
                    System.err.println(e.getMessage());
                }catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            } else printUsageAndExit();

        }

        public static void validateArgs(String[] args, int expectedArgs,
                                        int[] numericArguments,int[] datesArguments) {
            if(expectedArgs != args.length) {
                printUsageAndExit();
            }
            for (int position : numericArguments) {
                try {
                    Double.parseDouble(args[position]);
                } catch (NumberFormatException n) {
                    printUsageAndExit();
                }
            }
            for (int position : datesArguments) {
                try {
                    LocalDateTime.parse(args[position]);
                } catch (DateTimeException d) {
                    printUsageAndExit();
                }
            }
        }

        public static void printRace(ClientRaceDto race){
            System.out.println("Race with ID: " + race.getRaceId() + "\n" +
                    "- Location: " + race.getRaceLocation() + "\n" +
                    "- Description: " + race.getDescription() + "\n" +
                    "- Places: " + race.getAvailablePlaces() + "\n" +
                    "- Price: " + race.getInscriptionPrice() + "\n");
        }



        public static void printUsageAndExit() {
            printUsage();
            System.exit(-1);
        }

        public static void printUsage() {
            System.err.println("Usage:\n" +
                    "    [add]              -addRace <maxParticipants> <description> <price> <date> <city> \n" +
                    "    [findRace]         -findRace <raceId>\n" +
                    "    [findRaces]        -findRaces <date> <city>\n" +
                    "    [addInscription]   -addInscription <raceId> <mail> <creditCard>\n" +
                    "    [findInscriptions] -findInscriptions <mail>\n" +
                    "    [collectDorsal]    -collectDorsal <creditCard> <inscriptionId>\n");
        }
}
