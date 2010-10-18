package guidedsearch;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import openwcm.OpenWcmServlet;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.model.*;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class SearchResultsBuilder {

    // used by main method, it is only for testing
    /*    private IdcClient setupIdcConnection() throws Exception {
        String csIp = "localhost";
        String csPort = "4444";
        String idcConnectionUrl = "idc://" + csIp + ":" + csPort;

        IdcClientManager idcClientManager = new IdcClientManager();
        IdcClient idcClient = null;
        try {
            idcClient = idcClientManager.createClient(idcConnectionUrl);
        } catch (IdcClientException e) {
            throw new Exception(e);
        }
        if (null == idcClient) {
            String errMsg =
                "Cannot instantiate a connection to the content server with url" +
                idcConnectionUrl;
            throw new Exception(errMsg);
        }
        return idcClient;
    }

    public static void main(String[] args) throws Exception {
        IdcContext idcContext = new IdcContext("sysadmin");
        SearchResultsBuilder srb = new SearchResultsBuilder();
        IdcClient idcClient = srb.setupIdcConnection();
        DataBinder dataBinder = idcClient.createBinder();

        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        dataBinder.putLocal("IsJava", "1");

        String s = "";
        String ss = "(dDocTitle <substring> `"+s+"`  <OR>  dDocName <substring> `"+s+"`)  <OR>  (<ftx>"+s+"</ftx>)";
        dataBinder.putLocal("QueryText",ss);


        SearchResults searchResults = new SearchResults();
        List<SearchResult> searchResultsList = searchResults.getSearchResults();

        try {
            ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
            DataBinder data = idc_response.getResponseAsBinder();
            DataObject localData = data.getLocalData();
            DataResultSet resultSet = data.getResultSet("SearchResults");
            List<DataObject> rows = resultSet.getRows();

            System.out.println("Before for loop");

            Properties subjectList = new Properties();
            Properties titleList = new Properties();

            for (DataObject row: rows)
            {
                String subjek = row.get("xmohe_dokumen");
                String title = row.get("dDocTitle");

                System.out.println("TEST " + title);


                // Subject list generation
                if(subjek == null || subjek.equals(""))
                {
                    subjek = "Others";
                }

                if(subjectList.getProperty(subjek) == null)
                {
                    subjectList.setProperty(subjek,"1");
                }
                else
                {
                    int counter = new Integer(subjectList.getProperty(subjek));
                    counter++;
                    subjectList.setProperty(subjek,counter+"");
                }

                // title list generation
                if(title == null || title.equals(""))
                {
                	title = "Others";
                }

                if(titleList.getProperty(title) == null)
                {
                	titleList.setProperty(title,"1");
                }
                else
                {
                    int counter = new Integer(titleList.getProperty(subjek));
                    counter++;
                    titleList.setProperty(title,counter+"");
                }
            }

           Enumeration e = subjectList.propertyNames();

           while(e.hasMoreElements())
           {
            String tmp = (String)e.nextElement();
            int i = new Integer(subjectList.getProperty(tmp));

            System.out.println("Subject name " + tmp + " Count " + i);
           }

           Enumeration t = titleList.propertyNames();

           while(t.hasMoreElements())
           {
            String tmp = (String)t.nextElement();
            int i = new Integer(titleList.getProperty(tmp));

            System.out.println("title name " + tmp + " Count " + i);
           }

            idc_response.close();

        } catch (IdcClientException e) {
            System.out.println(e);
        }
    }
 */

    private int resultCountLimit = 10000;
    
    public DocSearchResults getDocumentSearchResults(ServletContext application, 
                                                     String username, 
                                                     String category, 
                                                     String categoryType, 
                                                     String searchText, 
                                                     int prevPage, 
                                                     int pageSize) {
        IdcContext idcContext = new IdcContext(username);
        IdcClient idcClient = 
            (IdcClient)application.getAttribute(OpenWcmServlet.IDC_CLIENT);
        DataBinder dataBinder = idcClient.createBinder();

        String queryText = "";

        int startRow = (pageSize * prevPage) + 1;
        int endRow = (pageSize * prevPage) + pageSize;

        if (searchText == null || searchText.equals("") || 
            searchText.equals("null")) {
            queryText = "(" + categoryType + " <matches> `" + category + "`)";
        } else {
            queryText = 
                    "((dDocTitle <substring> `" + searchText + "`  <OR>  dDocName <substring> `" + 
                    searchText + "`)  <OR>  (<ftx>" + searchText + 
                    "</ftx>) AND (" + categoryType + " <matches> `" + 
                    category + "`))";
        }

        System.out.println("QWEUQWE " + queryText);
        dataBinder.putLocal("SortOrder", "Desc");
        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        dataBinder.putLocal("IsJava", "1");
        dataBinder.putLocal("ResultCount", Integer.toString(pageSize));
        dataBinder.putLocal("SortField", "dInDate");

        dataBinder.putLocal("PageNumber", Integer.toString(prevPage + 1));
        dataBinder.putLocal("StartRow", Integer.toString(startRow));
        dataBinder.putLocal("EndRow", Integer.toString(endRow));
        //dataBinder.putLocal("SecurityGroups","public");
        dataBinder.putLocal("IsJava", "1");
        dataBinder.putLocal("QueryText", queryText);

        DocSearchResults docSearchResults = new DocSearchResults();
        List<SearchResult> docSearchResultsList = 
            docSearchResults.getDocSearchResults();
        try {
            ServiceResponse idc_response = 
                idcClient.sendRequest(idcContext, dataBinder);
            DataBinder data = idc_response.getResponseAsBinder();
            DataObject localData = data.getLocalData();

            DataResultSet resultSet = data.getResultSet("SearchResults");

            docSearchResults.setTotalRows(localData.getInteger("TotalRows"));

            List<DataObject> rows = resultSet.getRows();
            for (DataObject row: rows) {
                String contentId = row.get("dDocName");
                String filename = row.get("dOriginalName");
                String title = row.get("dDocTitle");
                String comments = row.get("xComments");
                String subjek = row.get("xmohe_dokumen");
                String url = OpenWcmServlet.formatUrls(application, contentId);
                SearchResult searchResult = 
                    new SearchResult(title, url, filename, comments, subjek);
                docSearchResultsList.add(searchResult);
            }

            idc_response.close();
        } catch (IdcClientException e) {
            System.out.println(e);
        }
        return docSearchResults;

    }


    public GuidedSearchResults getGuidedSearchList(ServletContext application, 
                                                   String username, 
                                                   String searchText) {
        IdcContext idcContext = new IdcContext(username);
        IdcClient idcClient = 
            (IdcClient)application.getAttribute(OpenWcmServlet.IDC_CLIENT);
        DataBinder dataBinder = idcClient.createBinder();

        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        dataBinder.putLocal("IsJava", "1");
        dataBinder.putLocal("ResultCount","10000");

        String queryText;

        if (searchText == null || searchText.equals("") || 
            searchText.equals("null"))
            queryText = "";
        else
            queryText = 
                    "(dDocTitle <substring> `" + searchText + "` <OR> dDocName <substring> `" + 
                    searchText + "`) <OR> (<ftx>" + searchText + "</ftx>)";

        System.out.println("query " + queryText);

        dataBinder.putLocal("QueryText", queryText);


        GuidedSearchResults gSearchResults = new GuidedSearchResults();

        Properties subjectList = new Properties();
        Properties dTypeList = new Properties();

        try {
            ServiceResponse idc_response = 
                idcClient.sendRequest(idcContext, dataBinder);
            DataBinder data = idc_response.getResponseAsBinder();
            DataObject localData = data.getLocalData();
            DataResultSet resultSet = data.getResultSet("SearchResults");

            List<DataObject> rows = resultSet.getRows();
            for (DataObject row: rows) {

                String subject = row.get("xmohe_dokumen");
                String dType = row.get("dExtension");


                // Subject list generation
                if (subject == null || subject.equals("")) {
                    System.out.println("Subject is null");
                } else {
                    if (subjectList.getProperty(subject) == null) {
                        subjectList.setProperty(subject, "1");
                        System.out.println("TEST " + subject);
                    } else {
                        int counter = 
                            new Integer(subjectList.getProperty(subject));
                        counter++;
                        System.out.println("TEST" + counter);
                        subjectList.setProperty(subject, counter + "");
                    }
                }


                // doc type list generation
                if (dType == null || dType.equals("")) {
                    System.out.println("TITLE " + dType);
                } else {
                    if (dTypeList.getProperty(dType) == null)
                        dTypeList.setProperty(dType, "1");
                    else {
                        int counter = 
                            new Integer(dTypeList.getProperty(dType));
                        counter++;
                        dTypeList.setProperty(dType, counter + "");
                    }
                }
            }

            gSearchResults.setSubjectList(subjectList);
            gSearchResults.setdTypeList(dTypeList);

            idc_response.close();
        } catch (IdcClientException e) {
            System.out.println(e);
        }
        return gSearchResults;
    }


    /**
     * @param application
     * @param username
     * @param isTitleOnly
     * @param searchText
     * @param prevPage
     * @param pageSize
     * @return
     */
    public SearchResults getSearchResults(ServletContext application, 
                                          String username, boolean isTitleOnly, 
                                          String searchText, int prevPage, 
                                          int pageSize) {
        IdcContext idcContext = new IdcContext(username);
        IdcClient idcClient = 
            (IdcClient)application.getAttribute(OpenWcmServlet.IDC_CLIENT);
        DataBinder dataBinder = idcClient.createBinder();

        // set up the arguments for IDC Service

        String queryText = "";
        if (searchText == null || searchText.equals("")) {
            queryText = "";
            searchText = "";
        } else if (isTitleOnly) {
            queryText = "dDocTitle <sub> `" + searchText + "`";
        } else {
            queryText = 
                    "(dDocTitle <substring> `" + searchText + "`  <OR>  dDocName <substring> `" + 
                    searchText + "`)  <OR>  (<ftx>" + searchText + "</ftx>)";
        }

        int startRow = (pageSize * prevPage) + 1;
        int endRow = (pageSize * prevPage) + pageSize;

        dataBinder.putLocal("SortOrder", "Desc");
        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        dataBinder.putLocal("IsJava", "1");
        dataBinder.putLocal("ResultCount", Integer.toString(pageSize));
        dataBinder.putLocal("SortField", "dInDate");
        dataBinder.putLocal("QueryText", queryText);

        dataBinder.putLocal("PageNumber", Integer.toString(prevPage + 1));
        dataBinder.putLocal("StartRow", Integer.toString(startRow));
        dataBinder.putLocal("EndRow", Integer.toString(endRow));
        //dataBinder.putLocal("SecurityGroups","public");


        SearchResults searchResults = new SearchResults();
        List<SearchResult> searchResultsList = 
            searchResults.getSearchResults();
        try {
            ServiceResponse idc_response = 
                idcClient.sendRequest(idcContext, dataBinder);
            DataBinder data = idc_response.getResponseAsBinder();
            DataObject localData = data.getLocalData();

            DataResultSet resultSet = data.getResultSet("SearchResults");
            searchResults.setTotalRows(localData.getInteger("TotalRows"));

            List<DataObject> rows = resultSet.getRows();
            for (DataObject row: rows) {
                String contentId = row.get("dDocName");
                String filename = row.get("dOriginalName");
                String title = row.get("dDocTitle");
                String comments = row.get("xComments");
                String subjek = row.get("xmohe_dokumen");
                String searchTarget = row.get("xSearchTarget");
                String collectionId = row.get("xCollectionID");
                
                /* The next section is for OpenWCM checked in content...it will have a search target specified
                 * but will NOT have an xCollectionId specified.
                 * 
                 * Photos on the other hand will have an xCollectionId so we have slightly different search target
                 * construction logic for it...ie. no placeholderId or datafileId syntax required.
                 */
                URL searchTargetUrl = null;
                searchTarget = "http://machine.com" + searchTarget;  // so creating a url will work.
                try {
                    searchTargetUrl = new URL(searchTarget);
                } catch (MalformedURLException e) {
                    // TODO
                }
                String query = searchTargetUrl.getQuery();
                String path = searchTargetUrl.getPath();
                String url = null;
                if(searchTarget != null && (!searchTarget.equals("")))
                    if ( null != collectionId && !"".equalsIgnoreCase(collectionId)) {
                        url = path + "?collectionId=" + query + "&contentId=" + contentId;
                    } else {
                        // This is an openwcm link
                        url = path + "?placeholderId=" + query + "&datafileId=" + contentId;
                    }
                else 
                {
                    url = OpenWcmServlet.formatUrls(application, contentId);
                }
                
                SearchResult searchResult = 
                    new SearchResult(title, url, filename, comments, subjek);
                searchResultsList.add(searchResult);
            }

            idc_response.close();
        } catch (IdcClientException e) {
            System.out.println(e);
        }
        return searchResults;
    }

}
