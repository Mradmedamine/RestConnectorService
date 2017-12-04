package com.mainsys.fhome.gui.connector;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.core.ParameterizedTypeReference;

import com.mainsys.fhome.gui.connector.wrapper.ListWrapper;
import com.mainsys.fhome.gui.connector.wrapper.ResponseWrapper;

public abstract class RestConnectorConfigBase
	implements RestConnectorConfig
{
	protected abstract String getRootUrl();

	protected abstract String getBasePath();

	@Override
	public String getAbsoluteUrl(String path)
	{
		if (StringUtils.isBlank(getRootUrl()) || StringUtils.isBlank(getBasePath()))
		{
			throw new IllegalArgumentException("Root url and base path cannot be empty, " + getClass().getSimpleName());
		}
		return getRootUrl() + getBasePath() + path;
	}

	/**
	 * ResponseWrapper Parameterized Type Reference
	 *
	 * @param clazz
	 */
	@Override
	public <T> ParameterizedTypeReference<ResponseWrapper<T>> parameterizeWithResponseWrapper(final Class<T> clazz)
	{
		return new ParameterizedTypeReference<ResponseWrapper<T>>()
		{
			@Override
			public Type getType()
			{
				return TypeUtils.parameterize(ResponseWrapper.class, clazz);
			}
		};
	}

	/** ResponseWrapper and ListWrapper Parameterized Type Reference
	 *
	 * @param clazz
	 */
	@Override
	public <T> ParameterizedTypeReference<ResponseWrapper<ListWrapper<T>>> parameterizeWithResponseAndListWrappers(final Class<T> clazz)
	{
		return new ParameterizedTypeReference<ResponseWrapper<ListWrapper<T>>>()
		{
			@Override
			public Type getType()
			{
				return TypeUtils.parameterize(ResponseWrapper.class, TypeUtils.parameterize(ListWrapper.class, clazz));
			}
		};
	}

	/** ResponseWrapper and List Parameterized Type Reference
	 *
	 * @param clazz
	 */
	@Override
	public <T> ParameterizedTypeReference<ResponseWrapper<List<T>>> parameterizeWithResponseWrapperAndList(final Class<T> clazz)
	{
		return new ParameterizedTypeReference<ResponseWrapper<List<T>>>()
		{
			@Override
			public Type getType()
			{
				return TypeUtils.parameterize(ResponseWrapper.class, TypeUtils.parameterize(List.class, clazz));
			}
		};
	}

	/**
	 * ResponseWrapper and Map Parameterized Type Reference
	 *
	 * @param Map keyClass
	 * @param Map valueClass
	 */
	@Override
	public <K, V> ParameterizedTypeReference<ResponseWrapper<Map<K, V>>> parameterizeWithResponseWrapperAndMap(final Class<K> keyClass,
																																				  final Class<V> valueClass)
	{
		return new ParameterizedTypeReference<ResponseWrapper<Map<K, V>>>()
		{
			@Override
			public Type getType()
			{
				return TypeUtils.parameterize(ResponseWrapper.class, TypeUtils.parameterize(Map.class, keyClass, valueClass));
			}
		};
	}
}
