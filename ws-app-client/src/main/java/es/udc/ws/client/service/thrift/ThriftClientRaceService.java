package es.udc.ws.client.service.thrift;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ThriftClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientRaceService.endpointAddress";
    private String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
}
