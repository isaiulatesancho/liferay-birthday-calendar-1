package com.rivetlogic.birthday.portlet;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.rivetlogic.birthday.model.UserBirthday;
import com.rivetlogic.birthday.service.UsersBirthdayService;
import com.rivetlogic.birthday.service.UsersBirthdayServiceImpl;
import com.rivetlogic.birthday.service.result.PaginatedResult;

import static com.rivetlogic.birthday.portlet.PortletConstants.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Portlet implementation class BirthdayPortlet
 */
public class BirthdayPortlet extends MVCPortlet {
 
	//TODO
	private UsersBirthdayService usersBirthdayService = new UsersBirthdayServiceImpl();
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException ,PortletException {
		super.doView(renderRequest, renderResponse);
	}
	
	@Override
	public void render(RenderRequest request, RenderResponse response) throws PortletException ,IOException {
		super.render(request, response);
	}
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws IOException, PortletException {
		try {
			doServeResource(request, response);
		} catch (Exception e) {
			LOGGER.error(String.format(ERROR_SERVING_RESOURCE, e.getMessage()));
		}
	}
	
	private void doServeResource(ResourceRequest request, ResourceResponse response) throws Exception{
		
		String type = ParamUtil.getString(request, TYPE);
		
		if (null != type && !type.isEmpty()) {
			switch (type) {
				case GET_BY_DAY:
					getBirthdaysByDate(request,
							response);
					break;
				case GET_BY_WEEK:
					getBirthdaysByWeek(request, response);
					break;
				case GET_BY_MONTH:
					getGroupedMonthBirthdays(request, response);
					break;
				case GET_DAYS_BY_MONTH:
					getGroupedDaysBirthDays(request, response);
					break;
			}
		} else {
			printJsonResponse(String.format(ERROR_BAD_PARAMETER_VALUE, TYPE, type),
					String.valueOf(HttpServletResponse.SC_BAD_REQUEST),
					response);
		}
	}
	
	private void getGroupedDaysBirthDays(ResourceRequest request, ResourceResponse response) throws Exception {
		int year = getIntParam(request, response, PARAM_YEAR);
		int month = getIntParam(request, response, PARAM_MONTH);
		int[] days = getIntArrParam(request, response, PARAM_DAY);
		long tzOffset = getIntParam(request, response, PARAM_TIMEZONE_OFFSET) * Time.MINUTE;//Time zone offset in milliseconds
		int offset = getPaginationParam(request, PARAM_OFFSET, null);
		int limit = getPaginationParam(request, PARAM_LIMIT, null);
		try{
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			PaginatedResult<UserBirthday>paginatedResult =  usersBirthdayService.getGroupedDaysBirthdays(year, month, days, tzOffset, themeDisplay, offset, limit);
			if(null == paginatedResult){
				printErrorResponse(NO_BIRTHDAYS_FOUND, String.valueOf(HttpServletResponse.SC_NOT_FOUND), response);
				return;
			}
		
			JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();
			printJsonResponse(jsonSerializer.serialize(paginatedResult), String.valueOf(HttpServletResponse.SC_OK), response);
		}catch(Exception e){
			printErrorResponse(ERROR_RETRIEVING_BIRTHDAYS, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), response);
			throw e;
		}
	}
	
	private int getPaginationParam(ResourceRequest request, String paramName, Integer defaultValue){
		if(PARAM_OFFSET.equals(paramName) || PARAM_LIMIT.equals(paramName))
			return ParamUtil.getInteger(request, paramName,  QueryUtil.ALL_POS);
		else
			return ParamUtil.getInteger(request, paramName,  defaultValue);
	}
	
	private void getGroupedMonthBirthdays(ResourceRequest request, ResourceResponse response) throws Exception {
		int year = getIntParam(request, response, PARAM_YEAR);
		int month = getIntParam(request, response, PARAM_MONTH);
		long tzOffset = getIntParam(request, response, PARAM_TIMEZONE_OFFSET) * Time.MINUTE;//Time zone offset in milliseconds
		try{
			PaginatedResult<UserBirthday>paginatedResult =  usersBirthdayService.getGroupedMonthBirthdays(year, month, tzOffset);
			if(null == paginatedResult){
				printErrorResponse(NO_BIRTHDAYS_FOUND, String.valueOf(HttpServletResponse.SC_NOT_FOUND), response);
				return;
			}
		
			JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();
			printJsonResponse(jsonSerializer.serialize(paginatedResult), String.valueOf(HttpServletResponse.SC_OK), response);
		}catch(Exception e){
			printErrorResponse(ERROR_RETRIEVING_BIRTHDAYS, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), response);
			throw e;
		}
	}
	
	private void getBirthdaysByDate(ResourceRequest request, ResourceResponse response) throws Exception{
		int day = getIntParam(request, response, PARAM_DAY);
		int month = getIntParam(request, response, PARAM_MONTH);
		int year = getIntParam(request, response, PARAM_YEAR);
		long tzOffset = getIntParam(request, response, PARAM_TIMEZONE_OFFSET) * Time.MINUTE;//Time zone offset in milliseconds
		try{
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			PaginatedResult<UserBirthday>paginatedResult = 
					usersBirthdayService.getBirthdaysByDate(month, day, year, themeDisplay , tzOffset);
			if(null == paginatedResult){
				printErrorResponse(NO_BIRTHDAYS_FOUND, String.valueOf(HttpServletResponse.SC_NOT_FOUND), response);
				return;
			}
			JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();
			printJsonResponse(jsonSerializer.serialize(paginatedResult), String.valueOf(HttpServletResponse.SC_OK), response);
		}catch(Exception e){
			printErrorResponse(ERROR_RETRIEVING_BIRTHDAYS, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), response);
			throw e;
		}
	}
	
	private void getBirthdaysByWeek(ResourceRequest request, ResourceResponse response) throws Exception{
		int minDateYear = getIntParam(request, response, PARAM_YEAR1);
		int minDateMonth = getIntParam(request, response, PARAM_MONTH1);
		int minDateDay = getIntParam(request, response, PARAM_DAY1);
		int maxDateYear = getIntParam(request, response, PARAM_YEAR2);
		int maxDateMonth = getIntParam(request, response, PARAM_MONTH2);
		int maxDateDay = getIntParam(request, response, PARAM_DAY2);
		long tzOffset = getIntParam(request, response, PARAM_TIMEZONE_OFFSET) * Time.MINUTE;//Time zone offset in milliseconds
		try{
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			PaginatedResult<UserBirthday>paginatedResult = usersBirthdayService.getBirthdaysByWeek(minDateYear, minDateMonth, minDateDay,
					maxDateYear, maxDateMonth, maxDateDay, tzOffset, themeDisplay);
			if(null == paginatedResult){
				printErrorResponse(NO_BIRTHDAYS_FOUND, String.valueOf(HttpServletResponse.SC_NOT_FOUND), response);
				return;
			}
			JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();
			printJsonResponse(jsonSerializer.serialize(paginatedResult), String.valueOf(HttpServletResponse.SC_OK), response);
		}catch(Exception e){
			printErrorResponse(ERROR_RETRIEVING_BIRTHDAYS, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), response);
			throw e;
		}
		
	}
	
	private int getIntParam(ResourceRequest request, ResourceResponse response, String paramName) throws Exception{
		int result =  ParamUtil.getInteger(request, paramName, -1);
		if(result == -1){
			printJsonResponse(String.format(MISSING_REQUIRED_PARAMETER, paramName), String.valueOf(HttpServletResponse.SC_BAD_REQUEST), response);
			throw new Exception(String.format(MISSING_REQUIRED_PARAMETER, paramName));
		}
		return result;
	}
	
	private int[] getIntArrParam(ResourceRequest request, ResourceResponse response, String paramName) throws Exception{
		int []result =  ParamUtil.getIntegerValues(request, paramName, null);
		if(result == null || result.length == 0){
			printJsonResponse(String.format(MISSING_REQUIRED_PARAMETER, paramName), String.valueOf(HttpServletResponse.SC_BAD_REQUEST), response);
			throw new Exception(String.format(MISSING_REQUIRED_PARAMETER, paramName));
		}
		return result;
	}
	
	
	private void printErrorResponse(String message, String statusCode, ResourceResponse response){
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		jsonObject.put(MESSAGE, message);
		printJsonResponse(jsonObject.toString(), statusCode, response);
	}
	
	private void printJsonResponse(String jsonStr, String statusCode,
			ResourceResponse response) {
		if (null == statusCode)
			statusCode = String.valueOf(HttpServletResponse.SC_OK);
		response.setProperty(ResourceResponse.HTTP_STATUS_CODE, statusCode);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			LOGGER.error(e);
		}
		if (null != out && !out.checkError()) {
			response.setContentType(ContentTypes.APPLICATION_JSON);
			out.print(jsonStr);
			out.flush();
			out.close();
		}
	}
	
	private static final Log LOGGER = LogFactoryUtil.getLog(BirthdayPortlet.class);
	
}
