package com.mainsys.fhome.gui.connector;

import org.springframework.web.client.RestTemplate;

public interface RestConnector
{
	RestTemplate getRestTemplate();

	RestConnectorConfig getConnectorConfig();

}
