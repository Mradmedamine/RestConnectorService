package com.mainsys.fhome.gui.connector.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper<T>
{
	public String appName;
	public boolean success;
	public T result;
	public List<RestMessage> errors;
	public List<RestMessage> warnings;

	public String getAppName()
	{
		return this.appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public boolean isSuccess()
	{
		return this.success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public T getResult()
	{
		return result;
	}

	public void setResult(T result)
	{
		this.result = result;
	}

	public List<RestMessage> getErrors()
	{
		if (errors == null)
		{
			errors = new ArrayList<RestMessage>();
		}
		return errors;
	}

	public void setErrors(List<RestMessage> errors)
	{
		this.errors = errors;
	}

	public List<RestMessage> getWarnings()
	{
		if (warnings == null)
		{
			warnings = new ArrayList<RestMessage>();
		}
		return warnings;
	}

	public void setWarnings(List<RestMessage> warnings)
	{
		this.warnings = warnings;
	}

}
