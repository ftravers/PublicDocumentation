<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="GuidedSearch.*"%>
<%@page import="openwcm.OpenWcmServlet"%>
<%@page import="java.util.*"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Search</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	color: #000000;
}
body {
	background-color: #FFFFFF;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 00px;
	margin-bottom: 0px;
}
-->
</style>
<link rel="stylesheet" href="css/internalCSS.css" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #CCCCCC}
.style2 {color: #FFFFFF}
-->
</style>
</head>
<body>
<form action="">
<%
	int prevPage = 0;
	int totalPages = 0;
	int pageSize = 5;
	int startRow = 0;
	int endRow = 0;

	String s = (String)request.getParameter("searchText");
	String prevPageNumber = (String)request.getParameter("prevPageNumber");
	
	System.out.println(" S=========== " + s);
	
	System.out.println(prevPageNumber);
	if(prevPageNumber !=null)
	prevPage = Integer.parseInt(prevPageNumber);
	
	
	int currPage = prevPage + 1;
	
	startRow = (pageSize * prevPage) + 1;
	endRow = (pageSize * prevPage) + pageSize;
	
	Map<String,String> additionalArgs = new HashMap<String,String>();
	String collectionId = request.getParameter(SearchResults.SORT_ORDER);
	additionalArgs.put("SortOrder", "Desc");
	additionalArgs.put("QueryText", "<qsch>"+s+"</qsch>");
	additionalArgs.put("IdcService", "GET_SEARCH_RESULTS");
	additionalArgs.put("IsJava", "1");
	additionalArgs.put("ResultCount",Integer.toString(pageSize));
	additionalArgs.put("SortField", "dInDate");
	
	
	System.out.println(" Prev page " + prevPage);
	System.out.println(" Start Row " + startRow);
	System.out.println(" end Row " + endRow);
	System.out.println(" curr page " + currPage);
	
	additionalArgs.put("PageNumber", Integer.toString(prevPage+1));
	additionalArgs.put("StartRow",Integer.toString(startRow));
	additionalArgs.put("EndRow", Integer.toString(endRow));
	
	
	String username = "sysadmin";
	SearchResultsBuilder srb = new SearchResultsBuilder();
	SearchResults searchResults = srb.getSearchResults(application, username, additionalArgs);
	
	System.out.println("Total Rows ================" + srb.getTotalRows());
%>
</Form>

<table width="660" border="0" cellspacing="0" cellpadding="0">
               <tr>
                <td ><img src="images/bluebar-left.gif" width="14" height="20" /></td>
                <td  bgcolor="#0066CC" class="whitetext2" width="100%">Search Result</td>
                <td><img src="images/bluebar-right.gif" width="14" height="20" /></td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              
              <%for(SearchResult searchResult : searchResults.getSearchResults()) {%>
              <tr>
              	<td width="25" valign="top">
                	<img src="images/bullet1.gif" width="14" height="14" />
                </td>
                <td width="646">
                	<a href="<%=searchResult.getUrl() %>"  class="bluelink"><%=searchResult.getTitle() %></a>
                	<br><%=searchResult.getFilename()%></br>
                	<br>
                </td>				
               </tr>
              <%}%>
            </table>
			
<table>
	
	<tr>
		<td>
			<!-- This is the left hand navigation element -->

			<!-- Put the Subject and DocType select lists here -->

		</td>		
		
	</tr>
	<tr>
		<td>
			<a href="GuidedSearchPage.jsp?SearchText=<%=s%>&prevPageNumber=<%=Integer.toString(prevPage-1)%>">Prev</a>
	
			<%
				int displayListSize = 0;
		
				//Identify how many rows available
				int totalRows =	srb.getTotalRows();
		
				// identify the exact number of the last page
				if( totalRows > 0)
				{
				int lastPage = pageSize/totalRows;
				
				if(currPage > lastPage)
				{
					currPage = lastPage;
				}else if(currPage < 1)
				{
					currPage = 1;
				}
				} else if (totalRows <0)
				{
				System.out.println("Total Rows ==" + srb.getTotalRows());
				}
				
				// check whether the page no is within the range
				
			%>
					<a href="SearchForm.jsp">Home</a>
		</td>
	</tr>

</table>
</body>
</html>