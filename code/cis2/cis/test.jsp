
<%	
	openwcm.OpenWcmServlet openwcm = new openwcm.OpenWcmServlet();
	
	// In a real circumstance you'd get the username from somewhere else...here it is hardcoded.
	String username = "sysadmin";
	String datafileContentId = "FT3_CONTRIB_DATA_FILE_2";
	String placeholderContentId = "FT3_PLCHLDRDEF_2";
	String output = openwcm.callWcmPlaceholder(username, datafileContentId, placeholderContentId, application);
%>

<p>
<a href="http://db/idc/idcplg?IdcService=WCM_BEGIN_EDIT_SESSION&dDocName=FT3_CONTRIB_DATA_FILE_2" target="new">Edit this page</a>
</p>

<%= output %>

blah
