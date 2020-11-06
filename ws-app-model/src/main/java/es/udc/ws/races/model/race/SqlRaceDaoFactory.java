package es.udc.ws.races.model.race;

public class SqlRaceDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "Race.SqlRaceDaoFactory.className";
    private static SqlRaceDao dao = null;

    private SqlRaceDaoFactory(){}

    private static SqlRaceDao getInstance(){

        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            System.out.println(daoClassName);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRaceDao) daoClass.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlRaceDao getDao(){

        if (dao == null)
            dao = getInstance();
        return dao;
    }

}

