package es.udc.ws.races.model.raceservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class RaceServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "RaceServiceFactory.className";
    private static RaceService raceService = null;

    private RaceServiceFactory(){}

    private static RaceService getInstance(){

        try{
            String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            System.out.println(serviceClassName);
            Class serviceClass = Class.forName(serviceClassName);
            return (RaceService) serviceClass.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static RaceService getService(){

        if (raceService == null)
            raceService = getInstance();
        return raceService;
    }

}
