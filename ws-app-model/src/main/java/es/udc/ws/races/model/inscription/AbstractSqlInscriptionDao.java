package es.udc.ws.races.model.inscription;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlInscriptionDao implements SqlInscriptionDao{

    protected AbstractSqlInscriptionDao(){

    }

    @java.lang.Override
    public void update(Connection connection, Inscription inscription) throws InstanceNotFoundException {

    }

    @java.lang.Override
    public Inscription find(Connection connection, Long inscriptionId) throws InstanceNotFoundException {
        return null;
    }

    @java.lang.Override
    public boolean isInscripted(Connection connection, Long raceId, String mail) {
        return false;
    }

    @java.lang.Override
    public List<Inscription> findByMail(Connection connection, String mail) {
        return null;
    }

    @java.lang.Override
    public void remove(Connection connection, Inscription inscription) throws InstanceNotFoundException {

    }
}
