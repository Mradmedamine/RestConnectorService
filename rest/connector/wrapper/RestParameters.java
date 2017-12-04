package com.mainsys.fhome.gui.connector.wrapper;

import java.io.Serializable;
import java.util.Map;

public class RestParameters
	implements Serializable
{

	private static final long serialVersionUID = 8032371488129829027L;

	private Object body;
	private Map<String, ?> params;
	private long version;

	public RestParameters(Object body, Map<String, ?> params, long version)
	{
		super();
		this.body = body;
		this.params = params;
		this.version = version;
	}

	public RestParameters(Object body, long version)
	{
		super();
		this.body = body;
		this.version = version;
	}

	public RestParameters(Map<String, ?> params, long version)
	{
		super();
		this.params = params;
		this.version = version;
	}

	public RestParameters(long version)
	{
		super();
		this.version = version;
	}

	public RestParameters()
	{
		super();
	}

	public Object getBody()
	{
		return body;
	}

	public void setBody(Object body)
	{
		this.body = body;
	}

	public Map<String, ?> getParams()
	{
		return params;
	}

	public void setParams(Map<String, ?> params)
	{
		this.params = params;
	}

	public long getVersion()
	{
		return version;
	}

	public void setVersion(long version)
	{
		this.version = version;
	}

	public static class RestParametersBuilder
	{
		private Object body;
		private Map<String, ?> params;
		private long version;

		public RestParametersBuilder body(Object body)
		{
			this.body = body;
			return this;
		}

		public RestParametersBuilder params(Map<String, ?> params)
		{
			this.params = params;
			return this;
		}

		public RestParametersBuilder version(long version)
		{
			this.version = version;
			return this;
		}

		public RestParameters build()
		{
			return new RestParameters(body, params, version);
		}

	}
}
