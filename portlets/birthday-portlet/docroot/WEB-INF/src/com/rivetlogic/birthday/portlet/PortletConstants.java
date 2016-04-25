package com.rivetlogic.birthday.portlet;

public class PortletConstants {

	
	public static final String TYPE = "type";
	public static final String GET_BY_MONTH = "month";
	public static final String GET_BY_WEEK = "week";
	public static final String GET_BY_DAY = "day";
	public static final String GET_DAYS_BY_MONTH = "days";
	
	
	public static final String PARAM_OFFSET = "offset";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_DATE = "date";
	public static final String PARAM_DAY = "day";
	public static final String PARAM_MONTH = "month";
	public static final String PARAM_YEAR = "year";
	public static final String PARAM_DAY1 = "day1";
	public static final String PARAM_MONTH1 = "month1";
	public static final String PARAM_YEAR1 = "year1";
	public static final String PARAM_DAY2 = "day2";
	public static final String PARAM_MONTH2 = "month2";
	public static final String PARAM_YEAR2 = "year2";
	public static final String PARAM_TIMEZONE_OFFSET = "timeZoneOffset";
	
	public static final String PAGINATION_START_DAY = "startDay";
	public static final String PAGINATION_END_DAY = "endDay";
	
	public static int MAX_PAGINATION_LIMIT = 7;
	
	public static final String MESSAGE = "message";
	
	/* Errors */
	public static final String ERROR_BAD_PARAMETER_VALUE = "Bad parameter value. Parameter: %S. Value: %S.";
	public static final String ERROR_SERVING_RESOURCE = "Error while serving resource. Error: %s.";
	public static final String ERROR_RETRIEVING_BIRTHDAYS = "Error while retrieving users";
	public static final String NO_BIRTHDAYS_FOUND = "No birthdays were found.";
	public static final String MISSING_REQUIRED_PARAMETER = "Missing required parameter %s";
	public static final String BAD_VALUE_FOR_DATE_PARAM = "Bad value for date parameter %s. Expected format: %s.";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String DATE_FORMAT_SPECIFICATION = "YYYY-MM-DDThh:mm:ss.SSSTZD";
}
