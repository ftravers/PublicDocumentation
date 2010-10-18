<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="guidedsearch.*"%>
<%@page import="openwcm.OpenWcmServlet"%>
<%@page import="java.util.*"%>
<%@page import="javax.portlet.*"%>s
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
    
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
<portlet:defineObjects/>
<a href="GuidedSearchPage.jsp">Search Home</a>


<%

        boolean isTitleOnly = false;
        String isTitleRequired="no";

	int prevPage = 0;
	int totalPages = 0;
	int pageSize = 2;

        String search   = (String)request.getParameter("search");
	String searchText = (String)request.getParameter("searchText");
	String prevPageNumber = (String)request.getParameter("prevPage");
	String category = (String)request.getParameter("category");
	String categoryType = (String)request.getParameter("categoryType");
        String isTitle = (String)request.getParameter("isTitleOnly");
        
        if(searchText == null || searchText.equals("") || searchText.equals("null"))
        searchText="";

        if(isTitle != null) 
        {
            if(isTitle.equals("yes")) 
            {
                isTitleOnly = true;
                isTitleRequired = "yes";
            }
        }
    
%>

<%
	if(prevPageNumber !=null)
	prevPage = Integer.parseInt(prevPageNumber);
	
	int currPage = prevPage + 1;
		
	String username = "sysadmin";
	SearchResultsBuilder srb = new SearchResultsBuilder();
	DocSearchResults searchResults = srb.getDocumentSearchResults(application,username,category,categoryType,searchText,prevPage,pageSize);
%>


<p>Guided Search</p>
<table width="660" border="0" cellspacing="0" cellpadding="0">
<%
    
    GuidedSearchResults gsr = srb.getGuidedSearchList(application,username,searchText);
    Enumeration subjectEnu = gsr.getSubjectList().keys();
           
    while(subjectEnu.hasMoreElements())
    {
        String tmp = (String)subjectEnu.nextElement();
        
        int i = new Integer(gsr.getSubjectList().getProperty(tmp));
    	
        if(tmp != null && tmp.equals(category))
		{
 %>		
  		<tr>
  		<td><%=tmp%>(<%=i%>)</td>
  		</tr>
 <%
 		}else
 		{ 	
                        //set parameters
        PortletURL guidedSearchURL = renderResponse.createActionURL();
        guidedSearchURL.setParameter("searchText",searchText);
        guidedSearchURL.setParameter("search",search);
        guidedSearchURL.setParameter("isTitleOnly",isTitleRequired);
        //guidedSearchURL.setParameter("prevPage",Integer.toString());          
        guidedSearchURL.setParameter("categoryType","xmohe_dokumen");
        guidedSearchURL.setParameter("category",tmp);
        guidedSearchURL.setParameter("isGuided","yes");
                
%>
<tr>
<td><a href=<%=guidedSearchURL.toString()%>"><%=tmp%></a>(<%=i%>)</td><td></td>
<%
    }
    }
%>
</table>

<table>
<%
    Enumeration dTypeEnu = gsr.getdTypeList().keys();
           
    while(dTypeEnu.hasMoreElements())
    {
        String tmp = (String)dTypeEnu.nextElement();
        int i = new Integer(gsr.getdTypeList().getProperty(tmp));

	if(tmp != null && tmp.equals(category))
	{
%>		
<tr>
<td><%=tmp%>(<%=i%>)</td>
</tr>
<%
	}else
	{
        // set parameters
        PortletURL guidedSearchURL = renderResponse.createActionURL();
        guidedSearchURL.setParameter("searchText",searchText);
        guidedSearchURL.setParameter("search",search);
        guidedSearchURL.setParameter("isTitleOnly",isTitleRequired);
        //guidedSearchURL.setParameter("prevPage",Integer.toString(currPage));          
        guidedSearchURL.setParameter("categoryType","xmohe_dokumen");
        guidedSearchURL.setParameter("category",tmp);
        guidedSearchURL.setParameter("isGuided","yes");
        
%>
<tr>
<td><a href="<%=guidedSearchURL.toString()%>"><%=tmp%></a>(<%=i%>)</td>
</tr>
<%
    }
    }
%>
</table>




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

              
              <%for(SearchResult searchResult : searchResults.getDocSearchResults()) {%>
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
			
</body>
</html>
<%
						int lastPage		= 0;
						int displayListSize = 0;
						
						//Identify how many rows available
						int totalRows =	searchResults.getTotalRows();
						
						// identify the exact number of the last page
						if( totalRows > 0)
						{
						lastPage = totalRows/pageSize;
						
						int remaining = totalRows % pageSize;
						
						if(remaining > 0)
						{
							lastPage = lastPage + 1;
						}
						
						if(currPage > lastPage)
						{
							currPage = lastPage;
						}else if(currPage < 1)
						{
							currPage = 1;
						}
						} else if (totalRows <0)
						{
						System.out.println("Total Rows ==" + searchResults.getTotalRows());
						}
						System.out.println("Last PAge " + lastPage);
						System.out.println("currPage " + currPage);
						System.out.println("total Rows " + totalRows);
						System.out.println("Prev page " + prevPage);
						//Prev link
					if(currPage > 1)
					{
					%>
					<a href="DocGuidedSearchPage.jsp?search=search&searchText=<%=searchText%>&prevPage=<%=prevPage-1%>">Prev Page</a>
					<%	
					}
					
						
				for(int i=0;i<lastPage;i++)
				{
					System.out.println("Curr PAge " + currPage + " I  " + (i+1));
					
					if(currPage == (i+1))
					{
					%>
						<%=i+1%>
					<%
					}
					else
					{
					%>
				<a href="DocGuidedSearchPage.jsp?search=search&searchText=<%=searchText%>&prevPage=<%=i%>"><%=i+1%></a>
				<%
					}
				}
				
				//Prev link
				if(currPage < lastPage)
				{
				%>
				<a href="DocGuidedSearchPage.jsp?search=search&searchText=<%=searchText%>&prevPage=<%=currPage%>">Next Page</a>
				<%	
				}
						
				
				%>
				
				
				