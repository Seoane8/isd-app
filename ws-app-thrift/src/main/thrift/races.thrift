namespace java es.udc.ws.races.thrift

struct ThriftRaceDto {
    1: i64 raceId;
    2: i32 maxParticipants;
    3: string description;
    4: double inscriptionPrice;
    5: string raceDate;
    6: string raceLocation;
    7: i32 participants
}

struct ThriftInscriptionDto {
    1: i64 inscriptionId;
    2: i64 raceId;
    3: string mail;
    4: string creditCardNumber;
    5: i32 dorsal;
    6: bool dorsalCollected;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftAlreadyCollectedException {
    1: i64 inscriptionId
    2: string mail
}

exception ThriftIncorrectCreditCardException {
    1: i64 inscriptionId
    2: string mail
}

exception ThriftAlreadyInscriptedException {
    1: i64 raceId
    2: string mail
}

exception ThriftInscriptionDateExpiredException {
    1: i64 raceId
    2: string raceDate
}

exception ThriftNoMoreInscriptionsAllowedException {
    1: i64 raceId
}

service ThriftRaceService {

    i64 addRace(1: ThriftRaceDto raceDto)
        throws (1: ThriftInputValidationException e)

    ThriftRaceDto findRace (1: i64 raceId)
        throws (1: ThriftInstanceNotFoundException e)

    list<ThriftRaceDto> findRaces(1: string date, string city)
        throws (1: ThriftInputValidationException e)

    i64 addInscription(1: i64 raceId, 2: string mail, 3: string creditCard)
        throws (1: ThriftInputValidationException e1, 2: ThriftInstanceNotFoundException e2,
                3: ThriftAlreadyInscriptedException e3, 4: ThriftInscriptionDateExpiredException e4
                5: ThriftNoMoreInscriptionsAllowedException e5)

    list<ThriftInscriptionDto> findInscriptions(1: string mail) throws (1: ThriftInputValidationException e)

    i32 collectDorsal(1: string creditCard, 2: i64 inscriptionId)
        throws (1: ThriftInputValidationException e1, 2: ThriftInstanceNotFoundException e2,
                3: ThriftAlreadyCollectedException e3, 4: ThriftIncorrectCreditCardException e4)

}