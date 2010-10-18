package openwcm;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class TestCore {
	public String test(
			ServletRequest request,
			ServletContext application, 
			String username		
	) {
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();

		dataBinder.putLocal( "QueryText", "" );
		dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
		dataBinder.putLocal("IsJava", "1");

		StringBuffer sb = new StringBuffer();
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataResultSet resultSet = data.getResultSet("SearchResults");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				String contentId = row.get("dDocName");
				String title = row.get("dDocTitle");
				sb.append(contentId + "--" + title + "<p>");
			}
			idc_response.close();
		} catch (IdcClientException e) {
			System.out.println(e);
		}
		return sb.toString();
	}
}
