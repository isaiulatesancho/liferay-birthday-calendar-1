<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />

<aui:container>
	<aui:row>
		<aui:col width="20">
			<img src="<%=renderRequest.getContextPath()%>/rivetlogic-logo.png"/>
		</aui:col>
		<aui:col width="80">
			<p>
			Thank you for using this application.<br/>
			We welcome feature/bug reports concerning this app and other improvements you may want. 
			Discussion helps clarify the ways the app can be used and also helps define directions for future development.<br/></br>
			Please post your concerns at <a href="http://issues.rivetlogic.com/browse/LRA">issues.rivetlogic.com/browse/LRA</a>.
			You may also want to check the app documentation <a href="http://wiki.rivetlogic.com/display/LRA/Liferay+Apps+Home">here</a>
			</p>
			<p>
			Rivet Logic's Liferay Marketplace apps team.<br/>
			<a href="http://www.rivetlogic.com" target="_blank">http://www.rivetlogic.com</a>
			</p>
		</aui:col>
	</aui:row>
</aui:container>