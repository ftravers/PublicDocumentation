<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="search.*"%>
<%@page import="openwcm.OpenWcmServlet"%>
<%@page import="java.util.*"%>

<%
	Search search = new Search();
	String username = "sysadmin";
	
	SearchResults searchResults = search.doSearch(request, application, username);
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<p>Search Results</p>
<table>
<tr>
<td>
<!-- This is the left hand navigation element -->

<!-- Put the Subject and DocType select lists here -->

</td>
<td>
<!-- This is the body element -->

<table>
<%for(SearchResult searchResult : searchResults.getSearchResults()) {%>
	<tr><td>
	<ul>
	<li><a href="<%=searchResult.getUrl() %>"><%=searchResult.getTitle() %></a></li>
	<li><%=searchResult.getFilename() %></li>
	</ul>
	</td></tr>
<%}%>
</table>

</td>

</tr>
</table>
</body>
</html>