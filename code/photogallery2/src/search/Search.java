package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import openwcm.OpenWcmServlet;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;



public class Search {
	
	private SearchResults searchResults;
	
	public static void main(String [] args) {

	}
	
	public void test(ServletContext application) {
		Map<String,String> additionalArgs = new HashMap<String,String>();
		additionalArgs.put("SortOrder", "Desc");
		additionalArgs.put("QueryText", "");
		additionalArgs.put("IdcService", "GET_SEARCH_RESULTS");
		additionalArgs.put("IsJava", "1");
		Search search = new Search();
		SearchResults sr = search.getSearchResults(application,"sysadmin", additionalArgs);
	}
	
	public SearchResults doSearch( ServletRequest request, ServletContext application, String username ) {

		Map<String,String> additionalArgs = new HashMap<String,String>();
		
		String sortOrder = request.getParameter(SearchResults.SORT_ORDER);
		if (null == sortOrder) {
			sortOrder = SearchResults.DEFAULT_SEARCH_ORDER;
		}
		additionalArgs.put(SearchResults.SORT_ORDER, sortOrder);
		
		// the same kind of checking can be done for the rest of the parameters...
		additionalArgs.put("QueryText", "");
		additionalArgs.put("IdcService", "GET_SEARCH_RESULTS");
		additionalArgs.put("IsJava", "1");
		additionalArgs.put("ResultCount", "20");
		additionalArgs.put("SortField", "dInDate");
		additionalArgs.put("PageNumber", "2");
		additionalArgs.put("StartRow", "21");
		additionalArgs.put("EndRow", "40");
		
		return getSearchResults(application, username, additionalArgs);
	}
	
	public SearchResults getSearchResults(ServletContext application, String username, Map<String,String> additionalArgs) {
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		if (null != additionalArgs) {
			for (Map.Entry<String, String> param : additionalArgs.entrySet()) {
				dataBinder.putLocal(param.getKey(), param.getValue());
			}
		}
		SearchResults searchResults = new SearchResults();
		List<SearchResult> searchResultsList = searchResults.getSearchResults();
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataObject localData = data.getLocalData();
			
			DataResultSet resultSet = data.getResultSet("SearchResults");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				String contentId = row.get("dDocName");
				String filename = row.get("dOriginalName");
				String title = row.get("dDocTitle");
				String url = OpenWcmServlet.formatUrls(application, contentId);
				SearchResult searchResult = new SearchResult(title, url, filename);
				searchResultsList.add(searchResult);
			}
			idc_response.close();
		} catch (IdcClientException e) {
			System.out.println(e);
		}
		return searchResults;
	}
}
