<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="guidedsearch.*"%>
<%@page import="openwcm.OpenWcmServlet"%>
<%@page import="java.util.*"%>
<%@page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>

<html>
<body>
<portlet:defineObjects/>
<%

    boolean isTitleOnly = false;
    String isTitleRequired="no";

    String search = (String)renderRequest.getPortletSession().getAttribute("search");
    String searchText = (String)renderRequest.getPortletSession().getAttribute("searchText");
    String prevPageString = (String)renderRequest.getPortletSession().getAttribute("prevPage");
    String isTitle = (String)renderRequest.getPortletSession().getAttribute("isTitleOnly");
    String category = (String)renderRequest.getPortletSession().getAttribute("category");
    String categoryType = (String)renderRequest.getPortletSession().getAttribute("categoryType");
    
    PortletURL formURL = renderResponse.createActionURL();
    
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
    
    if(category == null || category.equals(""))
    category = "";
    if(categoryType == null || categoryType.equals(""))
    categoryType = "";
    
%>
MoHE Content Search Portlet
<form method="post" action="<%=formURL.toString()%>">
<input name="search" value="search" type="hidden">
<input name="category" value="<%=category%>" type="hidden">
<input name="categoryType" value="<%=categoryType%>" type="hidden">
Search : <input name="searchText" value="<%=searchText%>"><button type="Submit">Go</button>

<%
    if(search != null)
    {
        formURL.setParameter("searchText",searchText);
        formURL.setParameter("search",search);
        formURL.setParameter("isTitleOnly",isTitleRequired);
        
        if(category!=null)
        formURL.setParameter("category",category);
        
        if(categoryType != null)
        formURL.setParameter("categoryType",categoryType);
        
	int prevPage = 0;
	int totalPages = 0;
	int pageSize = 10;
	int startRow = 0;
	int endRow = 0;
	int currPage = 1;
        
        if(prevPageString !=null)
        {
            prevPage = Integer.parseInt(prevPageString);
            currPage = prevPage + 1;
        }
	startRow = (pageSize * prevPage) + 1;
	endRow = (pageSize * prevPage) + pageSize;
	
	String username = "sysadmin";
	SearchResultsBuilder srb = new SearchResultsBuilder();
        
	SearchResults searchResults = srb.getSearchResults(application, username, isTitleOnly, searchText,prevPage,pageSize);
	
	//out.println("Page number " + searchResults.getPageNumber() + " " + searchResults.getQueryText());
%>
<%
    //is Title Only code was heres
%>
</form>

<%
    // lastPage
    int lastPage		= 0;
                
    //Identify how many rows available
    int totalRows =	searchResults.getTotalRows();
    
    // identify the exact number of the last page
    if( totalRows > 0)
    {
            // find out the last page
            lastPage = totalRows/pageSize;
    
            // find out if any remaining, for ex. 5/2 = 2 + 1. Display 3 pages
            int remaining = totalRows % pageSize;
            if(remaining > 0)
            {
                    lastPage = lastPage + 1;
            }
                
            // if currpage is bigger than last page, set currpage = lastpage. this happens when there is only one page
            if(currPage > lastPage)
            {
                    currPage = lastPage;
            }else if(currPage < 1)
            {
                    currPage = 1;
            }
    }

    formURL.setParameter("prevPage",Integer.toString(currPage));                
                
    System.out.println("Last PAge " + lastPage);
    System.out.println("currPage " + currPage);
    System.out.println("total Rows " + totalRows);
    System.out.println("Prev page " + prevPage);
%>

<BR>
<!--This region wil display the number of result -->
<table width="488" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="123"><p>Guided Search</p> </td>
    <td width="355">  Total results found [<%=searchResults.getTotalRows()%>]</td>
  </tr>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
  <!--This region will display subject list and document type list -->
    <td  valign="top">
    
    Subject List
<table width="300" border="0" cellspacing="0" cellpadding="0">
<%
	GuidedSearchResults gsr = srb.getGuidedSearchList(application,username,searchText);

    Enumeration subjectEnu = gsr.getSubjectList().keys();
           
    while(subjectEnu.hasMoreElements())
    {
        String tmp = (String)subjectEnu.nextElement();
        int i = new Integer(gsr.getSubjectList().getProperty(tmp));
        
        //set parameters
        PortletURL guidedSearchURL = renderResponse.createActionURL();
        guidedSearchURL.setParameter("searchText",searchText);
        guidedSearchURL.setParameter("search",search);
        guidedSearchURL.setParameter("isTitleOnly",isTitleRequired);
        guidedSearchURL.setParameter("prevPage","0"); 
        guidedSearchURL.setParameter("categoryType","xmohe_dokumen");
        guidedSearchURL.setParameter("category",tmp);
        guidedSearchURL.setParameter("isGuided","yes");
%>
<tr>
<td><a href="<%=guidedSearchURL.toString()%>"><%=tmp%></a>(<%=i%>)</td><td></td>
<%
    }

%>
</table>
<BR></BR>
Document Type List
<table>
<%
	
    Enumeration dTypeEnu = gsr.getdTypeList().keys();
           
    while(dTypeEnu.hasMoreElements())
    {
    	
        String tmp = (String)dTypeEnu.nextElement();
        System.out.println("Tmp" + tmp);
        int i = new Integer(gsr.getdTypeList().getProperty(tmp));
        
                //set parameters
        PortletURL guidedSearchURL = renderResponse.createActionURL();
        guidedSearchURL.setParameter("searchText",searchText);
        guidedSearchURL.setParameter("search",search);
        guidedSearchURL.setParameter("isTitleOnly",isTitleRequired);
        guidedSearchURL.setParameter("prevPage","0");          
        guidedSearchURL.setParameter("categoryType","dExtension");
        guidedSearchURL.setParameter("category",tmp);
        guidedSearchURL.setParameter("isGuided","yes");
%>
<tr>
<td><a href="<%=guidedSearchURL.toString()%>"><%=tmp%></a>(<%=i%>)</td><td></td>
<%
    }

%>
</table> </td>

<!--This region will display a search results -->
    <td width="400" valign="top">
    
    
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
	<li><a href="<%=searchResult.getUrl()%>"><%=searchResult.getTitle() %></a></li>
	<%=searchResult.getFilename()%>
        <%=searchResult.getComments()%>
	</ul>
	</td></tr>
<%}%>
</table>

</td>

</tr>

</table>

<%

                //Prev link
        if(currPage > 1)
        {
            formURL.setParameter("prevPage",Integer.toString(prevPage-1));
        %>
        <a href="<%=formURL.toString()%>">Prev Page</a>
        <%	
        }
        
        for(int i=0;i<lastPage;i++)
        {
                if(currPage == (i+1))
                {
                
        %>
                <%=i+1%>
        <%
                }
                else
                {
                formURL.setParameter("prevPage",i+"");
                %>
                <a href="<%=formURL.toString()%>"><%=i+1%></a>
                <%
                }
        }

//Next link
if(currPage < lastPage)
{

formURL.setParameter("prevPage",currPage+"");
%>
<a href="<%=formURL.toString()%>">Next Page</a>
<%	
}
}
%>
</td>
  </tr>
</table>

</body>
</html>