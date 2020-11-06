package es.udc.ws.races.model.race;

public class SqlRaceDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "Race.SqlRaceDaoFactory.className";
    private static SqlDaoRace dao = null;

    private SqlRaceDaoFactory(){}

    private static SqlDaoRace getInstance(){

        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            System.out.println(daoClassName);
            Class daoClass = Class.forName(daoClassName);
            return (SqlDaoRace) daoClass.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlDaoRace getDao(){

        if (dao == null)
            dao = getInstance();
        return dao;
    }

}

