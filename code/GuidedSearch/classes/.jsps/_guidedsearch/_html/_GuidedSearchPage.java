package _guidedsearch._html;

import oracle.jsp.runtime.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import oracle.jsp.el.*;
import javax.servlet.jsp.el.*;
import guidedsearch.*;
import openwcm.OpenWcmServlet;
import java.util.*;
import javax.portlet.*;


public class _GuidedSearchPage extends com.orionserver.http.OrionHttpJspPage {


  // ** Begin Declarations


  // ** End Declarations

  public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {

    response.setContentType( "text/html; charset=ISO-8859-1");
    /* set up the intrinsic variables using the pageContext goober:
    ** session = HttpSession
    ** application = ServletContext
    ** out = JspWriter
    ** page = this
    ** config = ServletConfig
    ** all session/app beans declared in globals.jsa
    */
    PageContext pageContext = JspFactory.getDefaultFactory().getPageContext( this, request, response, null, true, JspWriter.DEFAULT_BUFFER, true);
    // Note: this is not emitted if the session directive == false
    HttpSession session = pageContext.getSession();
    int __jsp_tag_starteval;
    ServletContext application = pageContext.getServletContext();
    JspWriter out = pageContext.getOut();
    _GuidedSearchPage page = this;
    ServletConfig config = pageContext.getServletConfig();
    javax.servlet.jsp.el.VariableResolver __ojsp_varRes = (VariableResolver)new OracleVariableResolverImpl(pageContext);

    try {


      out.write(__oracle_jsp_text[0]);
      out.write(__oracle_jsp_text[1]);
      out.write(__oracle_jsp_text[2]);
      out.write(__oracle_jsp_text[3]);
      out.write(__oracle_jsp_text[4]);
      out.write(__oracle_jsp_text[5]);
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.PortletConfig portletConfig = null;
      {
        oracle.portlet.server.containerimpl.taglib.DefineObjectsTag __jsp_taghandler_1=(oracle.portlet.server.containerimpl.taglib.DefineObjectsTag)OracleJspRuntime.getTagHandler(pageContext,oracle.portlet.server.containerimpl.taglib.DefineObjectsTag.class,"oracle.portlet.server.containerimpl.taglib.DefineObjectsTag");
        __jsp_taghandler_1.setParent(null);
        __jsp_tag_starteval=__jsp_taghandler_1.doStartTag();
        if (__jsp_taghandler_1.doEndTag()==javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
          return;
        renderResponse = (javax.portlet.RenderResponse) pageContext.findAttribute("renderResponse");
        renderRequest = (javax.portlet.RenderRequest) pageContext.findAttribute("renderRequest");
        portletConfig = (javax.portlet.PortletConfig) pageContext.findAttribute("portletConfig");
        OracleJspRuntime.releaseTagHandler(pageContext,__jsp_taghandler_1,1);
      }
      out.write(__oracle_jsp_text[6]);
      
      
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
          
      
      out.write(__oracle_jsp_text[7]);
      out.print(formURL.toString());
      out.write(__oracle_jsp_text[8]);
      out.print(category);
      out.write(__oracle_jsp_text[9]);
      out.print(categoryType);
      out.write(__oracle_jsp_text[10]);
      out.print(searchText);
      out.write(__oracle_jsp_text[11]);
      
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
      
      out.write(__oracle_jsp_text[12]);
      
          //is Title Only code was heres
      
      out.write(__oracle_jsp_text[13]);
      
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
      
      out.write(__oracle_jsp_text[14]);
      out.print(searchResults.getTotalRows());
      out.write(__oracle_jsp_text[15]);
      
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
      
      out.write(__oracle_jsp_text[16]);
      out.print(guidedSearchURL.toString());
      out.write(__oracle_jsp_text[17]);
      out.print(tmp);
      out.write(__oracle_jsp_text[18]);
      out.print(i);
      out.write(__oracle_jsp_text[19]);
      
          }
      
      
      out.write(__oracle_jsp_text[20]);
      
      	
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
      
      out.write(__oracle_jsp_text[21]);
      out.print(guidedSearchURL.toString());
      out.write(__oracle_jsp_text[22]);
      out.print(tmp);
      out.write(__oracle_jsp_text[23]);
      out.print(i);
      out.write(__oracle_jsp_text[24]);
      
          }
      
      
      out.write(__oracle_jsp_text[25]);
      for(SearchResult searchResult : searchResults.getSearchResults()) {
      out.write(__oracle_jsp_text[26]);
      out.print(searchResult.getUrl());
      out.write(__oracle_jsp_text[27]);
      out.print(searchResult.getTitle() );
      out.write(__oracle_jsp_text[28]);
      out.print(searchResult.getFilename());
      out.write(__oracle_jsp_text[29]);
      out.print(searchResult.getComments());
      out.write(__oracle_jsp_text[30]);
      }
      out.write(__oracle_jsp_text[31]);
      
      
                      //Prev link
              if(currPage > 1)
              {
                  formURL.setParameter("prevPage",Integer.toString(prevPage-1));
              
      out.write(__oracle_jsp_text[32]);
      out.print(formURL.toString());
      out.write(__oracle_jsp_text[33]);
      	
              }
              
              for(int i=0;i<lastPage;i++)
              {
                      if(currPage == (i+1))
                      {
                      
              
      out.write(__oracle_jsp_text[34]);
      out.print(i+1);
      out.write(__oracle_jsp_text[35]);
      
                      }
                      else
                      {
                      formURL.setParameter("prevPage",i+"");
                      
      out.write(__oracle_jsp_text[36]);
      out.print(formURL.toString());
      out.write(__oracle_jsp_text[37]);
      out.print(i+1);
      out.write(__oracle_jsp_text[38]);
      
                      }
              }
      
      //Next link
      if(currPage < lastPage)
      {
      
      formURL.setParameter("prevPage",currPage+"");
      
      out.write(__oracle_jsp_text[39]);
      out.print(formURL.toString());
      out.write(__oracle_jsp_text[40]);
      	
      }
      }
      
      out.write(__oracle_jsp_text[41]);

    }
    catch (Throwable e) {
      if (!(e instanceof javax.servlet.jsp.SkipPageException)){
        try {
          if (out != null) out.clear();
        }
        catch (Exception clearException) {
        }
        pageContext.handlePageException(e);
      }
    }
    finally {
      OracleJspRuntime.extraHandlePCFinally(pageContext, true);
      JspFactory.getDefaultFactory().releasePageContext(pageContext);
    }

  }
  private static final char __oracle_jsp_text[][]=new char[42][];
  static {
    try {
    __oracle_jsp_text[0] = 
    "\n\n".toCharArray();
    __oracle_jsp_text[1] = 
    "\n".toCharArray();
    __oracle_jsp_text[2] = 
    "\n".toCharArray();
    __oracle_jsp_text[3] = 
    "\n".toCharArray();
    __oracle_jsp_text[4] = 
    "\n".toCharArray();
    __oracle_jsp_text[5] = 
    "\n\n<html>\n<body>\n".toCharArray();
    __oracle_jsp_text[6] = 
    "\n".toCharArray();
    __oracle_jsp_text[7] = 
    "\nMoHE Content Search Portlet\n<form method=\"post\" action=\"".toCharArray();
    __oracle_jsp_text[8] = 
    "\">\n<input name=\"search\" value=\"search\" type=\"hidden\">\n<input name=\"category\" value=\"".toCharArray();
    __oracle_jsp_text[9] = 
    "\" type=\"hidden\">\n<input name=\"categoryType\" value=\"".toCharArray();
    __oracle_jsp_text[10] = 
    "\" type=\"hidden\">\nSearch : <input name=\"searchText\" value=\"".toCharArray();
    __oracle_jsp_text[11] = 
    "\"><button type=\"Submit\">Go</button>\n\n".toCharArray();
    __oracle_jsp_text[12] = 
    "\n".toCharArray();
    __oracle_jsp_text[13] = 
    "\n</form>\n\n".toCharArray();
    __oracle_jsp_text[14] = 
    "\n\n<BR>\n<!--This region wil display the number of result -->\n<table width=\"488\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n  <tr>\n    <td width=\"123\"><p>Guided Search</p> </td>\n    <td width=\"355\">  Total results found [".toCharArray();
    __oracle_jsp_text[15] = 
    "]</td>\n  </tr>\n</table>\n<br>\n<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n  <tr>\n  <!--This region will display subject list and document type list -->\n    <td  valign=\"top\">\n    \n    Subject List\n<table width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n".toCharArray();
    __oracle_jsp_text[16] = 
    "\n<tr>\n<td><a href=\"".toCharArray();
    __oracle_jsp_text[17] = 
    "\">".toCharArray();
    __oracle_jsp_text[18] = 
    "</a>(".toCharArray();
    __oracle_jsp_text[19] = 
    ")</td><td></td>\n".toCharArray();
    __oracle_jsp_text[20] = 
    "\n</table>\n<BR></BR>\nDocument Type List\n<table>\n".toCharArray();
    __oracle_jsp_text[21] = 
    "\n<tr>\n<td><a href=\"".toCharArray();
    __oracle_jsp_text[22] = 
    "\">".toCharArray();
    __oracle_jsp_text[23] = 
    "</a>(".toCharArray();
    __oracle_jsp_text[24] = 
    ")</td><td></td>\n".toCharArray();
    __oracle_jsp_text[25] = 
    "\n</table> </td>\n\n<!--This region will display a search results -->\n    <td width=\"400\" valign=\"top\">\n    \n    \n    <p>Search Results</p>\n<table>\n<tr>\n<td>\n<!-- This is the left hand navigation element -->\n\n<!-- Put the Subject and DocType select lists here -->\n\n</td>\n<td>\n<!-- This is the body element -->\n\n<table>\n".toCharArray();
    __oracle_jsp_text[26] = 
    "\n\t<tr><td>\n\t<ul>\n\t<li><a href=\"".toCharArray();
    __oracle_jsp_text[27] = 
    "\">".toCharArray();
    __oracle_jsp_text[28] = 
    "</a></li>\n\t".toCharArray();
    __oracle_jsp_text[29] = 
    "\n        ".toCharArray();
    __oracle_jsp_text[30] = 
    "\n\t</ul>\n\t</td></tr>\n".toCharArray();
    __oracle_jsp_text[31] = 
    "\n</table>\n\n</td>\n\n</tr>\n\n</table>\n\n".toCharArray();
    __oracle_jsp_text[32] = 
    "\n        <a href=\"".toCharArray();
    __oracle_jsp_text[33] = 
    "\">Prev Page</a>\n        ".toCharArray();
    __oracle_jsp_text[34] = 
    "\n                ".toCharArray();
    __oracle_jsp_text[35] = 
    "\n        ".toCharArray();
    __oracle_jsp_text[36] = 
    "\n                <a href=\"".toCharArray();
    __oracle_jsp_text[37] = 
    "\">".toCharArray();
    __oracle_jsp_text[38] = 
    "</a>\n                ".toCharArray();
    __oracle_jsp_text[39] = 
    "\n<a href=\"".toCharArray();
    __oracle_jsp_text[40] = 
    "\">Next Page</a>\n".toCharArray();
    __oracle_jsp_text[41] = 
    "\n</td>\n  </tr>\n</table>\n\n</body>\n</html>".toCharArray();
    }
    catch (Throwable th) {
      System.err.println(th);
    }
}
}
