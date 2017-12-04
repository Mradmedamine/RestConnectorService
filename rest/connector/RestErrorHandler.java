package com.mainsys.fhome.gui.connector;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mainsys.fhome.gui.connector.wrapper.ResponseWrapper;
import com.mainsys.fhome.gui.connector.wrapper.RestMessage;
import com.mainsys.fhome.gui.module.base.model.BusinessException;
import com.mainsys.fhome.gui.util.HttpClientUtils;
import com.mainsys.fhome.module.base.model.validation.MessageType;
import com.mainsys.fhome.module.base.model.validation.MessageType.SeverityType;
import com.mainsys.fhome.module.base.model.validation.ValidationResult;

public class RestErrorHandler
	extends DefaultResponseErrorHandler
{
	private static HttpStatus[] httpStatusList;
	private static final String DROOLS_BUSINESS_ERROR_LABEL = "_DRL_";

	static
	{
		httpStatusList =
			new HttpStatus[] {
				HttpStatus.BAD_REQUEST,
				HttpStatus.UNAUTHORIZED,
				HttpStatus.NOT_FOUND,
				HttpStatus.CONFLICT,
				HttpStatus.PRECONDITION_FAILED,
				HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.NOT_IMPLEMENTED };
	}

	@Override
	public void handleError(ClientHttpResponse response)
	{
		try
		{
			if (ArrayUtils.contains(httpStatusList, HttpClientUtils.getHttpStatusCode(response)))
			{
				String responseBody = HttpClientUtils.getResponseBodyAsString(response);
				ResponseWrapper<?> responseWrapper = mapToResponseWrapper(responseBody);
				if (hasDroolsErrorsOrWarnings(responseWrapper))
				{
					throw new BusinessException(responseWrapperToValidationResult(responseWrapper));
				}
			}
			super.handleError(response);
		}
		catch (IOException ex)
		{
			// ignore
		}
	}

	private ResponseWrapper<?> mapToResponseWrapper(String json)
	{
		ResponseWrapper<?> responseWrapper = new ResponseWrapper<Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try
		{
			responseWrapper = mapper.readValue(json, ResponseWrapper.class);
		}
		catch (Exception e)
		{
			responseWrapper = null;
		}
		return responseWrapper;
	}

	public static ValidationResult responseWrapperToValidationResult(ResponseWrapper<?> responseWrapper)
	{
		ValidationResult validationResult = new ValidationResult();
		if (responseWrapper != null)
		{
			for (RestMessage message : responseWrapper.getErrors())
			{
				MessageType messageType = restMessageToMessageType(message, SeverityType.ERROR);
				if (messageType != null)
				{
					validationResult.add(messageType);
				}
			}

			for (RestMessage message : responseWrapper.getWarnings())
			{
				MessageType messageType = restMessageToMessageType(message, SeverityType.WARNING);
				if (messageType != null)
				{
					validationResult.add(messageType);
				}
			}
		}
		return validationResult;
	}

	private static MessageType restMessageToMessageType(RestMessage message, SeverityType severityType)
	{
		MessageType messageType = null;
		if (message != null && severityType != null)
		{
			messageType = new MessageType();
			messageType.setSeverityType(severityType);

			String description = StringUtils.EMPTY;
			if (StringUtils.isNotBlank(message.getErrorLabel()))
			{
				description += StringUtils.defaultString(message.getErrorLabel());
			}
			else
			{
				description += StringUtils.defaultString(message.getErrorText());
			}

			if (StringUtils.isNotBlank(message.getErrorCode()))
			{
				String errorCode = StringUtils.defaultString(message.getErrorCode());
				if (StringUtils.isNotBlank(description))
				{
					description += " (" + errorCode + ")";
				}
				else
				{
					description += errorCode;
				}
			}
			messageType.setDescription(description);
		}
		return messageType;
	}

	private boolean hasDroolsErrorsOrWarnings(ResponseWrapper<?> responseWrapper)
	{
		if (hasErrorsOrWarnings(responseWrapper))
		{
			for (RestMessage message : responseWrapper.getErrors())
			{
				if (message.getErrorCode() != null && message.getErrorCode().contains(DROOLS_BUSINESS_ERROR_LABEL))
				{
					return true;
				}
			}

			for (RestMessage message : responseWrapper.getWarnings())
			{
				if (message.getErrorCode() != null && message.getErrorCode().contains(DROOLS_BUSINESS_ERROR_LABEL))
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasErrorsOrWarnings(ResponseWrapper<?> responseWrapper)
	{
		return responseWrapper != null && (CollectionUtils.isNotEmpty(responseWrapper.getErrors()) || CollectionUtils.isNotEmpty(responseWrapper.getWarnings()));
	}
}
