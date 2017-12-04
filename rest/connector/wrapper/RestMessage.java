package com.mainsys.fhome.gui.connector.wrapper;

public class RestMessage
{
	private String errorCode;
	private String errorLabel;
	private String errorText;
	private String field;

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorLabel()
	{
		return errorLabel;
	}

	public void setErrorLabel(String errorLabel)
	{
		this.errorLabel = errorLabel;
	}

	public String getErrorText()
	{
		return errorText;
	}

	public void setErrorText(String errorText)
	{
		this.errorText = errorText;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}
}
