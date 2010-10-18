package GuidedSearch;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import openwcm.OpenWcmServlet;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class SearchResultsBuilder 
{
	private static int totalRows = 0;
	
	public static int getTotalRows() {
		return totalRows;
	}

	public static void setTotalRows(int totalRows) {
		SearchResultsBuilder.totalRows = totalRows;
	}

	private IdcClient setupIdcConnection() throws Exception 
	{
		String csIp = "ucmel";
		String csPort = "4444";
		String idcConnectionUrl = "idc://" + csIp + ":" + csPort;

		IdcClientManager idcClientManager = new IdcClientManager();
		IdcClient idcClient = null;
		try {
			idcClient = idcClientManager.createClient(idcConnectionUrl);
		} catch (IdcClientException e) {
			throw new Exception(e);
		}
		if (null == idcClient) 
		{
			String errMsg = "Cannot instantiate a connection to the content server with url" + idcConnectionUrl;
			throw new Exception(errMsg);
		}
		return idcClient;
	}
	
	public static void main(String [] args) throws Exception
	{
		IdcContext idcContext = new IdcContext("sysadmin");
		SearchResultsBuilder srb = new SearchResultsBuilder();
		IdcClient idcClient = srb.setupIdcConnection();
		DataBinder dataBinder = idcClient.createBinder();
		
		dataBinder.putLocal("IdcService", "GET_OPTION_LIST");
		dataBinder.putLocal("IsJava", "1");
		
		dataBinder.putLocal("Resultcount", "10");
		
		//SearchResults searchResults = new SearchResults();
		//List<SearchResult> searchResultsList = searchResults.getSearchResults();
		try 
		{
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataObject localData = data.getLocalData();
			DataResultSet resultSet =data.getResultSet("OptionList");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) 
			{
				System.out.println(row.get("dName") + row.get("dOption"));
				//String contentId = row.get("dDocName");
				//String filename = row.get("dOriginalName");
				//String title = row.get("dDocTitle");
				//SearchResult searchResult = new SearchResult(title, "url", filename);
				//searchResultsList.add(searchResult);
			}
			idc_response.close();
			//for(SearchResult searchResult : searchResultsList) 
			//{
			//System.out.println(" TITLE " + searchResult.getTitle());			
			//}
		} catch (IdcClientException e) 
		{
			System.out.println(e);
		}
	}
	
	public SearchResults getSearchResults(ServletContext application, String username, Map<String,String> additionalArgs) {
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		if (null != additionalArgs)
		{
			for (Map.Entry<String, String> param : additionalArgs.entrySet()) 
			{
				dataBinder.putLocal(param.getKey(), param.getValue());
			}
		}
		SearchResults searchResults = new SearchResults();
		List<SearchResult> searchResultsList = searchResults.getSearchResults();
		try 
		{
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataObject localData = data.getLocalData();
			
			DataResultSet resultSet = data.getResultSet("SearchResults");
			
			this.totalRows = localData.getInteger("TotalRows");
			
			System.out.println (" Rows " + this.totalRows);
			
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) 
			{
				String contentId = row.get("dDocName");
				String filename = row.get("dOriginalName");
				String title = row.get("dDocTitle");
				String url = OpenWcmServlet.formatUrls(application, contentId);
				SearchResult searchResult = new SearchResult(title, url, filename);
				searchResultsList.add(searchResult);
			}
			idc_response.close();
		} catch (IdcClientException e) 
		{
			System.out.println(e);
		}
		return searchResults;
	}
}