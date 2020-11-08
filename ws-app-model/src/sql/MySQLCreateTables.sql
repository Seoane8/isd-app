-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Race;
DROP TABLE Inscriptions;

-- --------------------------------- Race -------------------------------------
CREATE TABLE Race(
    raceId BIGINT NOT NULL AUTO_INCREMENT,
    maxParticipants INT NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin,
    price FLOAT NOT NULL,
    raceDate DATETIME NOT NULL,
    city VARCHAR(64) NOT NULL,
    creationDate DATETIME NOT NULL,
    participants INT NOT NULL,
    CONSTRAINT RacePK PRIMARY KEY(raceId),
    CONSTRAINT validMaxParticipants CHECK (maxParticipants > 0),
    CONSTRAINT validParticipants CHECK (participants >= 0),
    CONSTRAINT validPrice CHECK (price >= 0)
) ENGINE = InnoDB;

-- ----------------------------- Inscriptions ---------------------------------
CREATE TABLE Inscription(
    inscriptionId BIGINT NOT NULL AUTO_INCREMENT,
    raceId BIGINT NOT NULL,
    mail VARCHAR(64) NOT NULL,
    creditCardNumber VARCHAR(32),
    reservationDate DATETIME NOT NULL,
    dorsal INT NOT NULL,
    dorsalCollected BOOLEAN NOT NULL,
    price FLOAT NOT NULL,
    CONSTRAINT InscriptionPK PRIMARY KEY(inscriptionId),
    CONSTRAINT InscriptionRaceFK FOREIGN KEY(raceId)
        REFERENCES Race(raceId) ON DELETE CASCADE,
    CONSTRAINT validPrice CHECK (price >= 0),
    CONSTRAINT validDorsal CHECK (dorsal >= 0)
) ENGINE = InnoDB;