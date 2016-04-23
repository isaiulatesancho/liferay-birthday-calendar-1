package com.rivetlogic.birthday.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.model.Contact;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;


public class CustomContactFinderImpl extends BasePersistenceImpl<Contact> implements CustomContactFinder{

	
	@SuppressWarnings("unchecked")
	public List<Contact> getContactsByDate(int month, int day) throws SystemException{
		
		Session session = null;
        List<Contact> result = null;
        
        try {
        	
        	SessionFactory  sessionFactory = (SessionFactory)PortalBeanLocatorUtil.locate(LIFERAY_SESSION_FACTORY);
        	session = sessionFactory.openSession();
            result = new ArrayList<Contact>();
            String sql =  CustomSQLUtil.get(getQueryName(SQL_BIRTHDAYS_BY_DATE_BASE));
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity(CONTACT_ENTITY, PortalClassLoaderUtil.getClassLoader().loadClass(CONTACT_IMPL_BINARY_NAME));
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(day);
            qPos.add(month);
            
            result = (List<Contact>)QueryUtil.list(q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
            
            if(result == null || result.isEmpty()){
            	LOGGER.debug(NOT_FOUND_CONTACTS_BIRTHDAY_BY_DAY);
            }
            
            return result;

        } catch (Exception e) {
        	LOGGER.error(String.format(ERROR_CONTACTS_BIRTHDAY_BY_DAY, e.getMessage()));
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
	}
	
	@SuppressWarnings("unchecked")
	public Date getUpcomingBirthday(Date lastdate, long tzOffset) throws SystemException{
		Session session = null;
        Date result = null;
        try {
        	SessionFactory  sessionFactory = (SessionFactory)PortalBeanLocatorUtil.locate(LIFERAY_SESSION_FACTORY);
        	session = sessionFactory.openSession();
        	String sql = CustomSQLUtil.get(getQueryName(SQL_UPCOMING_BIRTHDAY));
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity(CONTACT_ENTITY, PortalClassLoaderUtil.getClassLoader().loadClass(CONTACT_IMPL_BINARY_NAME));
            QueryPos qPos = QueryPos.getInstance(q);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastdate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Object values[] = {month, month, day};
            for(Object value : values){
            	 qPos.add(value);
            }
            Integer dayOfMonth = null;
            List<Contact>contactsList = (List<Contact>)QueryUtil.list(q, getDialect(), 0, 1);
            if(null != contactsList && !contactsList.isEmpty()){
            	Contact contact = contactsList.get(0);
            	DateTime dateTime = new DateTime(contact.getBirthday());
            	dayOfMonth = dateTime.getDayOfMonth();
            	if(dateTime.getMonthOfYear() < month || (dateTime.getMonthOfYear() == month && dateTime.getDayOfMonth() <= day)){
            		dateTime = dateTime.withYear(year + 1);
            	}else{
            		dateTime = dateTime.withYear(year);
            	}
            	result = dateTime.toDate();
            }
	           
			//If date should not be returned
			//Avoids returning Feb 29 on non Leap Years.
			if(null != result && null != dayOfMonth){
				DateTime dateTime = new DateTime(result);
				//If it is not a leap year and we got 29, query again
				if(!isLeapYear(dateTime.getYear()) && 29 == dayOfMonth && 2 == dateTime.getMonthOfYear()){
					dateTime = dateTime.withTimeAtStartOfDay().plusDays(1);
			        qPos = QueryPos.getInstance(q);
			        Object values2[] = {dateTime.getMonthOfYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()};
			        for(Object value : values2){
			        	 qPos.add(value);
			        }
			        contactsList = (List<Contact>)QueryUtil.list(q, getDialect(), 0, 1);
			        
		            if(null != contactsList && !contactsList.isEmpty()){
		            	dateTime = new DateTime(contactsList.get(0).getBirthday());
		            	if(dateTime.getDayOfMonth() < month || (dateTime.getDayOfMonth() == month && dateTime.getDayOfMonth() <= day)){
		            		dateTime = dateTime.withYear(year + 1);
		            	}else{
		            		dateTime = dateTime.withYear(year);
		            		result = dateTime.toDate();
		            	}
		            }
				}
			}
            
            return result;

        } catch (Exception e) {
        	LOGGER.error(String.format(ERROR_RETRIEVING_UPCOMING_BIRTHDAY, e.getMessage()));
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
        
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Contact> getContactsByWeek(int year1, int month1, int day1, int year2, int month2, int day2) throws SystemException{
		
		Session session = null;
        List<Contact> result = null;
        
        try {
        	
        	SessionFactory  sessionFactory = (SessionFactory)PortalBeanLocatorUtil.locate(LIFERAY_SESSION_FACTORY);
        	session = sessionFactory.openSession();
            result = new ArrayList<Contact>();
            String sql =  CustomSQLUtil.get(
            		getQueryName(isCurrentYearLeap() ? SQL_BIRTHDAYS_BY_WEEK_LEAP_YEAR : SQL_BIRTHDAYS_BY_WEEK));
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity(CONTACT_ENTITY, PortalClassLoaderUtil.getClassLoader().loadClass(CONTACT_IMPL_BINARY_NAME));
            
            QueryPos qPos = QueryPos.getInstance(q);
            Object values[] = {month1, day1, month2, day2, String.valueOf(month1 != month2), month2, day2, month1, day1};
            for(Object value : values){
            	 qPos.add(value);
            }
            
            result = (List<Contact>)QueryUtil.list(q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
            
            if(result == null || result.isEmpty()){
            	LOGGER.debug(NOT_FOUND_CONTACTS_BIRTHDAY_BY_DAY);
            }
            
            return result;

        } catch (Exception e) {
        	LOGGER.error(String.format(ERROR_CONTACTS_BIRTHDAY_BY_WEEK, e.getMessage()));
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<Contact> getContactsByMonth(int month, int startDay, int endDay) throws SystemException{
		
		Session session = null;
        List<Contact> result = null;
        
        try {

        	SessionFactory  sessionFactory = (SessionFactory)PortalBeanLocatorUtil.locate(LIFERAY_SESSION_FACTORY);
        	session = sessionFactory.openSession();
            result = new ArrayList<Contact>();
            String sql =  CustomSQLUtil.get(getQueryName(SQL_BIRTHDAYS_BY_MONTH_BASE));
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity(CONTACT_ENTITY, PortalClassLoaderUtil.getClassLoader().loadClass(CONTACT_IMPL_BINARY_NAME));
            
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(month);
            qPos.add(startDay);
            qPos.add(endDay);
            
            result = (List<Contact>)QueryUtil.list(q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
            
            if(result == null || result.isEmpty()){
            	LOGGER.debug(NOT_FOUND_CONTACTS_BIRTHDAY_BY_DAY);
            }
            
            return result;

        } catch (Exception e) {
        	LOGGER.error(String.format(ERROR_CONTACTS_BIRTHDAY_BY_MONTH, e.getMessage()));
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> getGroupedBirthdaysOfMonth(int year, int month) throws SystemException{
		Session session = null;
        try {
        	SessionFactory  sessionFactory = (SessionFactory)PortalBeanLocatorUtil.locate(LIFERAY_SESSION_FACTORY);
        	session = sessionFactory.openSession();
        	String sql = CustomSQLUtil.get(
        			getQueryName(isLeapYear(year) ? SQL_GROUPED_BIRTHDAYS_BY_MONTH_LEAP_YEAR : SQL_GROUPED_BIRTHDAYS_BY_MONTH_BASE));
            SQLQuery q = session.createSQLQuery(sql);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(month);
            
            Iterator<Object> itr = (Iterator<Object>) QueryUtil.iterate(q, getDialect(), QueryUtil.ALL_POS,
            		QueryUtil.ALL_POS);
            
            Map<Integer, Integer> resultsMap = new HashMap<Integer, Integer>();
            while (itr.hasNext()) {
            	Object[] obj = (Object[]) itr.next();
            	Integer day = GetterUtil.getInteger(obj[1]);
            	Integer birthdaysCount = GetterUtil.getInteger(obj[0]);
            	resultsMap.put(day, birthdaysCount);
            }

            if(null == resultsMap || resultsMap.isEmpty()){
            	resultsMap = null;
            }
            
            return resultsMap;

        } catch (Exception e) {
        	LOGGER.error(String.format(ERROR_RETRIEVING_UPCOMING_BIRTHDAY, e.getMessage()));
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
        
	}
	
	private boolean isCurrentYearLeap(){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(new Date());
		return isLeapYear(calendar.get(Calendar.YEAR));
	}
	
	private boolean isLeapYear(int year){
	    GregorianCalendar cal = new GregorianCalendar();
	    return cal.isLeapYear(year);
	}
	
	private String getQueryName(String baseStr){
		switch(getDB().getType()){
			case DB_TYPE_SQL_SERVER:
				return baseStr + SQL_QUERY_SUFFIX;
			default:
				return baseStr;
		}
	}
	
	private static final Log LOGGER = LogFactoryUtil.getLog(CustomContactFinderImpl.class);
	
	private static final String CONTACT_ENTITY = "Contact_";
	private static final String CONTACT_IMPL_BINARY_NAME = "com.liferay.portal.model.impl.ContactImpl";
	private static final String LIFERAY_SESSION_FACTORY = "liferaySessionFactory";
	private static final String SQL_BIRTHDAYS_BY_DATE_BASE = "birthdaysByDate";
	private static final String SQL_BIRTHDAYS_BY_MONTH_BASE = "birthdaysByMonth";
	private static final String SQL_GROUPED_BIRTHDAYS_BY_MONTH_BASE = "groupedDaysByMonth";
	private static final String SQL_GROUPED_BIRTHDAYS_BY_MONTH_LEAP_YEAR = "groupedDaysByMonth_leapYear";
	private static final String SQL_BIRTHDAYS_BY_WEEK = "birthdaysByWeek";
	private static final String SQL_BIRTHDAYS_BY_WEEK_LEAP_YEAR = "birthdaysByWeek_leapYear";
	private static final String SQL_UPCOMING_BIRTHDAY = "upcomingBirthday";

	private static final String SQL_QUERY_SUFFIX = "_sqlserver";
	
	private static final String DB_TYPE_SQL_SERVER = "sqlserver";
	
	
	//Debug constants
	private static final String NOT_FOUND_CONTACTS_BIRTHDAY_BY_DAY = "No contacts by date found.";

	//Error constants
	private static final String ERROR_CONTACTS_BIRTHDAY_BY_DAY = "An error ocurrer while retrieving the contacts by date from the database. Error: %s";
	private static final String ERROR_CONTACTS_BIRTHDAY_BY_WEEK = "An error ocurrer while retrieving the contacts by week from the database. Error: %s";
	private static final String ERROR_CONTACTS_BIRTHDAY_BY_MONTH = "An error ocurrer while retrieving the contacts by month from the database. Error: %s";
	private static final String ERROR_RETRIEVING_UPCOMING_BIRTHDAY = "An error ocurrer while retrieving the upcoming birthday from the database. Error: %s";
}

