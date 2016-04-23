/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.rivetlogic.birthday.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CustomContactLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see CustomContactLocalService
 * @generated
 */
public class CustomContactLocalServiceWrapper
	implements CustomContactLocalService,
		ServiceWrapper<CustomContactLocalService> {
	public CustomContactLocalServiceWrapper(
		CustomContactLocalService customContactLocalService) {
		_customContactLocalService = customContactLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _customContactLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_customContactLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _customContactLocalService.invokeMethod(name, parameterTypes,
			arguments);
	}

	@Override
	public java.util.List<com.liferay.portal.model.Contact> findUsersByDate(
		int month, int day)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _customContactLocalService.findUsersByDate(month, day);
	}

	@Override
	public java.util.Date getUpcomingBirthday(java.util.Date lastDate,
		long tzOffset)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _customContactLocalService.getUpcomingBirthday(lastDate, tzOffset);
	}

	@Override
	public java.util.List<com.liferay.portal.model.Contact> getContactsByWeek(
		int minDateYear, int minDateMonth, int minDateDay, int maxDateYear,
		int maxDateMonth, int maxDateDay)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _customContactLocalService.getContactsByWeek(minDateYear,
			minDateMonth, minDateDay, maxDateYear, maxDateMonth, maxDateDay);
	}

	@Override
	public java.util.List<com.liferay.portal.model.Contact> getContactsByBirthdayMonth(
		int month, int startDay, int endDay)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _customContactLocalService.getContactsByBirthdayMonth(month,
			startDay, endDay);
	}

	@Override
	public java.util.Map<java.lang.Integer, java.lang.Integer> getGroupedMonthBirthdays(
		int year, int month)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _customContactLocalService.getGroupedMonthBirthdays(year, month);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public CustomContactLocalService getWrappedCustomContactLocalService() {
		return _customContactLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedCustomContactLocalService(
		CustomContactLocalService customContactLocalService) {
		_customContactLocalService = customContactLocalService;
	}

	@Override
	public CustomContactLocalService getWrappedService() {
		return _customContactLocalService;
	}

	@Override
	public void setWrappedService(
		CustomContactLocalService customContactLocalService) {
		_customContactLocalService = customContactLocalService;
	}

	private CustomContactLocalService _customContactLocalService;
}