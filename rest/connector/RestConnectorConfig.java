package com.mainsys.fhome.gui.connector;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

import com.mainsys.fhome.gui.connector.wrapper.ListWrapper;
import com.mainsys.fhome.gui.connector.wrapper.ResponseWrapper;

public interface RestConnectorConfig
{
	String getAbsoluteUrl(String path);

	HttpHeaders getHeaders(long version);

	<T> ParameterizedTypeReference<ResponseWrapper<T>> parameterizeWithResponseWrapper(Class<T> clazz);

	<T> ParameterizedTypeReference<ResponseWrapper<List<T>>> parameterizeWithResponseWrapperAndList(Class<T> clazz);

	<T> ParameterizedTypeReference<ResponseWrapper<ListWrapper<T>>> parameterizeWithResponseAndListWrappers(Class<T> clazz);

	<K, V> ParameterizedTypeReference<ResponseWrapper<Map<K, V>>> parameterizeWithResponseWrapperAndMap(Class<K> keyClass, Class<V> valueClass);
}
