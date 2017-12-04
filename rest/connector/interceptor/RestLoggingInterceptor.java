package com.mainsys.fhome.gui.interceptor;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.mainsys.fhome.gui.util.HttpClientUtils;

public class RestLoggingInterceptor
	implements ClientHttpRequestInterceptor
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
		throws IOException
	{
		traceRequest(request, body);
		ClientHttpResponse clientHttpResponse = execution.execute(request, body);
		traceResponse(clientHttpResponse);
		return clientHttpResponse;
	}

	private void traceRequest(HttpRequest request, byte[] body)
		throws IOException
	{
		log.debug("request URI : " + request.getURI());
		log.debug("request method : " + request.getMethod());
		log.debug("request headers : " + request.getHeaders().toString());
		log.debug("request body : " + StringUtils.defaultIfEmpty(HttpClientUtils.getRequestBody(body), "empty") + "\n");
	}

	private void traceResponse(ClientHttpResponse response)
		throws IOException
	{
		log.debug("response status code: " + HttpClientUtils.getHttpStatusCode(response));
		log.debug("response status text: " + response.getStatusText());
		log.debug("response body : " + StringUtils.defaultIfEmpty(HttpClientUtils.getResponseBodyAsString(response), "empty") + "\n");
	}

}
