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


public class _untitled1 extends com.orionserver.http.OrionHttpJspPage {


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
    _untitled1 page = this;
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
          
      
      out.write(__oracle_jsp_text[7]);
      
      	if(prevPageNumber !=null)
      	prevPage = Integer.parseInt(prevPageNumber);
      	
      	int currPage = prevPage + 1;
      		
      	String username = "sysadmin";
      	SearchResultsBuilder srb = new SearchResultsBuilder();
      	DocSearchResults searchResults = srb.getDocumentSearchResults(application,username,category,categoryType,searchText,prevPage,pageSize);
      
      out.write(__oracle_jsp_text[8]);
      
          
          GuidedSearchResults gsr = srb.getGuidedSearchList(application,username,searchText);
          Enumeration subjectEnu = gsr.getSubjectList().keys();
                 
          while(subjectEnu.hasMoreElements())
          {
              String tmp = (String)subjectEnu.nextElement();
              
              int i = new Integer(gsr.getSubjectList().getProperty(tmp));
          	
              if(tmp != null && tmp.equals(category))
      		{
       
      out.write(__oracle_jsp_text[9]);
      out.print(tmp);
      out.write(__oracle_jsp_text[10]);
      out.print(i);
      out.write(__oracle_jsp_text[11]);
      
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
                      
      
      out.write(__oracle_jsp_text[12]);
      out.print(guidedSearchURL.toString());
      out.write(__oracle_jsp_text[13]);
      out.print(tmp);
      out.write(__oracle_jsp_text[14]);
      out.print(i);
      out.write(__oracle_jsp_text[15]);
      
          }
          }
      
      out.write(__oracle_jsp_text[16]);
      
          Enumeration dTypeEnu = gsr.getdTypeList().keys();
                 
          while(dTypeEnu.hasMoreElements())
          {
              String tmp = (String)dTypeEnu.nextElement();
              int i = new Integer(gsr.getdTypeList().getProperty(tmp));
      
      	if(tmp != null && tmp.equals(category))
      	{
      
      out.write(__oracle_jsp_text[17]);
      out.print(tmp);
      out.write(__oracle_jsp_text[18]);
      out.print(i);
      out.write(__oracle_jsp_text[19]);
      
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
              
      
      out.write(__oracle_jsp_text[20]);
      out.print(guidedSearchURL.toString());
      out.write(__oracle_jsp_text[21]);
      out.print(tmp);
      out.write(__oracle_jsp_text[22]);
      out.print(i);
      out.write(__oracle_jsp_text[23]);
      
          }
          }
      
      out.write(__oracle_jsp_text[24]);
      for(SearchResult searchResult : searchResults.getDocSearchResults()) {
      out.write(__oracle_jsp_text[25]);
      out.print(searchResult.getUrl() );
      out.write(__oracle_jsp_text[26]);
      out.print(searchResult.getTitle() );
      out.write(__oracle_jsp_text[27]);
      out.print(searchResult.getFilename());
      out.write(__oracle_jsp_text[28]);
      }
      out.write(__oracle_jsp_text[29]);
      
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
      					
      out.write(__oracle_jsp_text[30]);
      out.print(searchText);
      out.write(__oracle_jsp_text[31]);
      out.print(prevPage-1);
      out.write(__oracle_jsp_text[32]);
      	
      					}
      					
      						
      				for(int i=0;i<lastPage;i++)
      				{
      					System.out.println("Curr PAge " + currPage + " I  " + (i+1));
      					
      					if(currPage == (i+1))
      					{
      					
      out.write(__oracle_jsp_text[33]);
      out.print(i+1);
      out.write(__oracle_jsp_text[34]);
      
      					}
      					else
      					{
      					
      out.write(__oracle_jsp_text[35]);
      out.print(searchText);
      out.write(__oracle_jsp_text[36]);
      out.print(i);
      out.write(__oracle_jsp_text[37]);
      out.print(i+1);
      out.write(__oracle_jsp_text[38]);
      
      					}
      				}
      				
      				//Prev link
      				if(currPage < lastPage)
      				{
      				
      out.write(__oracle_jsp_text[39]);
      out.print(searchText);
      out.write(__oracle_jsp_text[40]);
      out.print(currPage);
      out.write(__oracle_jsp_text[41]);
      	
      				}
      						
      				
      				
      out.write(__oracle_jsp_text[42]);

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
  private static final char __oracle_jsp_text[][]=new char[43][];
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
    "s\n".toCharArray();
    __oracle_jsp_text[5] = 
    "\n    \n<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n\n<title>Search</title>\n<style type=\"text/css\">\n<!--\nbody,td,th {\n\tfont-family: Arial, Helvetica, sans-serif;\n\tfont-size: 11px;\n\tcolor: #000000;\n}\nbody {\n\tbackground-color: #FFFFFF;\n\tmargin-left: 0px;\n\tmargin-top: 0px;\n\tmargin-right: 00px;\n\tmargin-bottom: 0px;\n}\n-->\n</style>\n<link rel=\"stylesheet\" href=\"css/internalCSS.css\" type=\"text/css\" />\n<style type=\"text/css\">\n<!--\n.style1 {color: #CCCCCC}\n.style2 {color: #FFFFFF}\n-->\n</style>\n</head>\n<body>\n".toCharArray();
    __oracle_jsp_text[6] = 
    "\n<a href=\"GuidedSearchPage.jsp\">Search Home</a>\n\n\n".toCharArray();
    __oracle_jsp_text[7] = 
    "\n\n".toCharArray();
    __oracle_jsp_text[8] = 
    "\n\n\n<p>Guided Search</p>\n<table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n".toCharArray();
    __oracle_jsp_text[9] = 
    "\t\t\n  \t\t<tr>\n  \t\t<td>".toCharArray();
    __oracle_jsp_text[10] = 
    "(".toCharArray();
    __oracle_jsp_text[11] = 
    ")</td>\n  \t\t</tr>\n ".toCharArray();
    __oracle_jsp_text[12] = 
    "\n<tr>\n<td><a href=".toCharArray();
    __oracle_jsp_text[13] = 
    "\">".toCharArray();
    __oracle_jsp_text[14] = 
    "</a>(".toCharArray();
    __oracle_jsp_text[15] = 
    ")</td><td></td>\n".toCharArray();
    __oracle_jsp_text[16] = 
    "\n</table>\n\n<table>\n".toCharArray();
    __oracle_jsp_text[17] = 
    "\t\t\n<tr>\n<td>".toCharArray();
    __oracle_jsp_text[18] = 
    "(".toCharArray();
    __oracle_jsp_text[19] = 
    ")</td>\n</tr>\n".toCharArray();
    __oracle_jsp_text[20] = 
    "\n<tr>\n<td><a href=\"".toCharArray();
    __oracle_jsp_text[21] = 
    "\">".toCharArray();
    __oracle_jsp_text[22] = 
    "</a>(".toCharArray();
    __oracle_jsp_text[23] = 
    ")</td>\n</tr>\n".toCharArray();
    __oracle_jsp_text[24] = 
    "\n</table>\n\n\n\n\n<table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n               <tr>\n                <td ><img src=\"images/bluebar-left.gif\" width=\"14\" height=\"20\" /></td>\n                <td  bgcolor=\"#0066CC\" class=\"whitetext2\" width=\"100%\">Search Result</td>\n                <td><img src=\"images/bluebar-right.gif\" width=\"14\" height=\"20\" /></td>\n              </tr>\n              <tr>\n                <td valign=\"top\">&nbsp;</td>\n                <td>&nbsp;</td>\n                <td>&nbsp;</td>\n              </tr>\n\n              \n              ".toCharArray();
    __oracle_jsp_text[25] = 
    "\n              <tr>\n              \t<td width=\"25\" valign=\"top\">\n                \t<img src=\"images/bullet1.gif\" width=\"14\" height=\"14\" />\n                </td>\n                <td width=\"646\">\n                \t<a href=\"".toCharArray();
    __oracle_jsp_text[26] = 
    "\"  class=\"bluelink\">".toCharArray();
    __oracle_jsp_text[27] = 
    "</a>\n                \t<br>".toCharArray();
    __oracle_jsp_text[28] = 
    "</br>\n                \t<br>\n                </td>\t\t\t\t\n               </tr>\n              ".toCharArray();
    __oracle_jsp_text[29] = 
    "\n            </table>\n\t\t\t\n</body>\n</html>\n".toCharArray();
    __oracle_jsp_text[30] = 
    "\n\t\t\t\t\t<a href=\"DocGuidedSearchPage.jsp?search=search&searchText=".toCharArray();
    __oracle_jsp_text[31] = 
    "&prevPage=".toCharArray();
    __oracle_jsp_text[32] = 
    "\">Prev Page</a>\n\t\t\t\t\t".toCharArray();
    __oracle_jsp_text[33] = 
    "\n\t\t\t\t\t\t".toCharArray();
    __oracle_jsp_text[34] = 
    "\n\t\t\t\t\t".toCharArray();
    __oracle_jsp_text[35] = 
    "\n\t\t\t\t<a href=\"DocGuidedSearchPage.jsp?search=search&searchText=".toCharArray();
    __oracle_jsp_text[36] = 
    "&prevPage=".toCharArray();
    __oracle_jsp_text[37] = 
    "\">".toCharArray();
    __oracle_jsp_text[38] = 
    "</a>\n\t\t\t\t".toCharArray();
    __oracle_jsp_text[39] = 
    "\n\t\t\t\t<a href=\"DocGuidedSearchPage.jsp?search=search&searchText=".toCharArray();
    __oracle_jsp_text[40] = 
    "&prevPage=".toCharArray();
    __oracle_jsp_text[41] = 
    "\">Next Page</a>\n\t\t\t\t".toCharArray();
    __oracle_jsp_text[42] = 
    "\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t".toCharArray();
    }
    catch (Throwable th) {
      System.err.println(th);
    }
}
}
