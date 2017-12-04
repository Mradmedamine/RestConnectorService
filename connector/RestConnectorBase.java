package com.mainsys.fhome.gui.connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.mainsys.fhome.gui.connector.wrapper.ListWrapper;
import com.mainsys.fhome.gui.connector.wrapper.ResponseWrapper;
import com.mainsys.fhome.gui.connector.wrapper.RestParameters;
import com.mainsys.fhome.gui.util.ListUtils;

public abstract class RestConnectorBase
	implements RestConnector
{
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	protected static final long DEFAULT_VERSION = 0;

	@Autowired
	protected RestTemplate restTemplate;

	@PostConstruct
	public void init()
	{
		restTemplate.setErrorHandler(new RestErrorHandler());
	}

	@Override
	public RestTemplate getRestTemplate()
	{
		return restTemplate;
	}

	protected HttpHeaders getHeaders()
	{
		return getConnectorConfig().getHeaders(DEFAULT_VERSION);
	}

	protected HttpHeaders getHeaders(long version)
	{
		return getConnectorConfig().getHeaders(version);
	}

	protected String getAbsoluteUrl(String path)
	{
		return getConnectorConfig().getAbsoluteUrl(path);
	}

	// Rest call using GET

	protected <B, R, T> R restGet(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result)
	{
		return restGet(url, responseType, result, null);
	}

	protected <B, R, T> R restGet(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, Map<String, ?> params)
	{
		return restCall(url, HttpMethod.GET, responseType, result, null, params, 0);
	}

	// Rest call using POST

	protected <B, R, T> R restPost(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, B body)
	{
		return restCall(url, HttpMethod.POST, responseType, result, body, null, 0);
	}

	protected <B, R, T> R restPost(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, RestParameters restParams)
	{
		return restCall(url, HttpMethod.POST, responseType, result, restParams.getBody(), restParams.getParams(), restParams.getVersion());
	}

	// Rest call using PUT

	protected <B, R, T> R restPut(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, B body)
	{
		return restCall(url, HttpMethod.PUT, responseType, result, body, null, 0);
	}

	protected <B, R, T> R restPut(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, RestParameters restParams)
	{
		return restCall(url, HttpMethod.PUT, responseType, result, restParams.getBody(), restParams.getParams(), restParams.getVersion());
	}

	// Rest call using DELETE

	protected <B, R, T> R restDelete(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, long version)
	{
		return restCall(url, HttpMethod.DELETE, responseType, result, null, null, version);
	}

	protected <B, R, T> R restDelete(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType, R result, RestParameters restParams)
	{
		return restCall(url, HttpMethod.DELETE, responseType, result, restParams.getBody(), restParams.getParams(), restParams.getVersion());
	}

	@SuppressWarnings("unchecked")
	private <B, R, T> R restCall(String url,
										  HttpMethod httpMethod,
										  ParameterizedTypeReference<ResponseWrapper<T>> responseType,
										  R result,
										  B body,
										  Map<String, ?> params,
										  long version)
	{
		HttpEntity<?> entity = body == null ? new HttpEntity<Object>(getHeaders(version)) : new HttpEntity<B>(body, getHeaders(version));
		ResponseWrapper<T> response = null;
		if (MapUtils.isEmpty(params))
		{
			response = restTemplate.exchange(url, httpMethod, entity, responseType).getBody();
		}
		else
		{
			response = restTemplate.exchange(url, httpMethod, entity, responseType, params).getBody();
		}
		result = (R) getPlainResponse(response, result.getClass());
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getPlainResponse(ResponseWrapper<?> responseBody, Class<T> resultClass)
	{
		if (responseBody != null)
		{
			Object result = responseBody.getResult();
			if (result instanceof ListWrapper)
			{
				return (T) ListUtils.emptyIfNull(((ListWrapper<T>) result).getList());
			}
			else if (result instanceof List)
			{
				return (T) ListUtils.emptyIfNull((List<T>) result);
			}
			else
			{
				return (T) result;
			}
		}
		else if (resultClass == List.class || resultClass == ArrayList.class)
		{
			return (T) Collections.emptyList();
		}
		return null;
	}

	// Parameterized Type Reference

	protected <T> ParameterizedTypeReference<ResponseWrapper<T>> parameterizeWithResponseWrapper(final Class<T> clazz)
	{
		return getConnectorConfig().parameterizeWithResponseWrapper(clazz);
	}

	protected <T> ParameterizedTypeReference<ResponseWrapper<ListWrapper<T>>> parameterizeWithResponseAndListWrappers(final Class<T> clazz)
	{
		return getConnectorConfig().parameterizeWithResponseAndListWrappers(clazz);
	}

	protected <T> ParameterizedTypeReference<ResponseWrapper<List<T>>> parameterizeWithResponseWrapperAndList(final Class<T> clazz)
	{
		return getConnectorConfig().parameterizeWithResponseWrapperAndList(clazz);
	}

}
