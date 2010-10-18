<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="openwcm.OpenWcmServlet"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>News Detail Page</title>
</head>
<body>
<h1>News Detail Page</h1>
<%
	openwcm.OpenWcmServlet openwcm = new openwcm.OpenWcmServlet();

	// In a real circumstance you'd get the username from somewhere else...here it is hardcoded.
	String username = "sysadmin";

	String placeholderContentId = request.getParameter(OpenWcmServlet.PLACEHOLD_ID);
	String datafileContentId = request.getParameter(OpenWcmServlet.DATAFILE_ID);
	Map<String,String> additionalArgs = new HashMap<String,String>();

	String output = openwcm.callWcmPlaceholder(username, datafileContentId, placeholderContentId, application, additionalArgs);
%>
<%= output %>
</body>
</html>