package openwcm;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class OpenWcmHelper extends HttpServlet {
	public static final String COLLECTION_ID = "collectionId";
	public static final String PARENT_COLLECTION_ID = "parentCollectionId";
	public static final String SITE_ID = "siteId";
	public static final String PLACEHOLDER_ID = "placeholderId";
	public static final String DATAFILE_ID = "datafileId";
	public static final String IDC_CLIENT = "IDC_CLIENT";


	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String servletName = config.getServletName();
		String servletContextName = config.getServletContext().getServletContextName();
		String linkPrefix = "/" + servletContextName + "/" + servletName + "/";
		String siteId = config.getInitParameter(OpenWcmHelper.SITE_ID);
		String testingUserName = config.getInitParameter(OpenWcmInfo.TESTING_USER);
		String test = config.getInitParameter("testing");
		boolean testing = Boolean.parseBoolean(test);
		// UCM Connection Specific Setup
		IdcClient idcClient = setupIdcConnection(config);
		
		
		OpenWcmInfo openWcmInfo = new OpenWcmInfo();
		openWcmInfo.setTesting(testing);
		openWcmInfo.setTestingUsername(testingUserName);
		openWcmInfo.setServletName(servletName);
		openWcmInfo.setServletContextName(servletContextName);
		openWcmInfo.setLinkPrefix(linkPrefix);
		openWcmInfo.setSiteId(siteId);
		openWcmInfo.setIdcClient(idcClient);

		
		config.getServletContext().setAttribute(OpenWcmInfo.UCM_SERVLET_INFO, openWcmInfo);
	}
	
	// put the following method in each web application servlet:
	private IdcClient setupIdcConnection(ServletConfig config) throws ServletException {
		String csIp = config.getInitParameter("conent_server_host");
		String csPort = config.getInitParameter("conent_server_port");
		String idcConnectionUrl = "idc://" + csIp + ":" + csPort;

		IdcClientManager idcClientManager = new IdcClientManager();
		IdcClient idcClient = null;
		try {
			idcClient = idcClientManager.createClient(idcConnectionUrl);
		} catch (IdcClientException e) {
			throw new ServletException(e);
		}

		if (null != idcClient) {
			config.getServletContext().setAttribute(IDC_CLIENT, idcClient);
		} else {
			String errMsg = "Cannot instantiate a connection to the content server with url" + idcConnectionUrl;
			throw new ServletException(errMsg);
		}
		return idcClient;
	}
	/*
	 * This method is called by the display technology (JSP). It will call the
	 * service WCM_PLACEHOLDER, and return to the browser a string of the HTML
	 * which is retrieved by the service call from the content server. You can
	 * also test this out in the URL of a browser first:
	 * http://ucmel/idc/idcplg?
	 * IdcService=WCM_PLACEHOLDER&dataFileDocName=XXX&placeholderDefinitionDocName
	 * =XXX&siteId=XXX
	 */
	public String callWcmPlaceholder(
			String username, 
			String dataFileContentId, 
			String placeholderContentId,
			ServletContext application,
			Map<String, String> additionalArgs
			) throws ServletException {

		IdcContext idcContext = new IdcContext(username);
		OpenWcmInfo openWcmInfo = (OpenWcmInfo)application.getAttribute(OpenWcmInfo.UCM_SERVLET_INFO);
		String linkPrefix = openWcmInfo.getLinkPrefix();
		
		IdcClient idcClient = openWcmInfo.getIdcClient();
		DataBinder dataBinder = idcClient.createBinder();

		dataBinder.putLocal(OpenWcmInfo.SITE_ID, openWcmInfo.getSiteId());
		dataBinder.putLocal("dataFileDocName", dataFileContentId);
		dataBinder.putLocal("placeholderDefinitionDocName", placeholderContentId);
		dataBinder.putLocal("SSDoLinkEvaluation", "true");
		dataBinder.putLocal("SSContributor", "true");
		dataBinder.putLocal("SSLinkUrlEvaluationPrefix", linkPrefix);
		dataBinder.putLocal("IdcService", "WCM_PLACEHOLDER");
		if (null != additionalArgs) {
			for (Map.Entry<String, String> param : additionalArgs.entrySet()) {
				dataBinder.putLocal(param.getKey(), param.getValue());
			}
		}
		String data = null;
		try {
			ServiceResponse serviceResponse = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data2 = serviceResponse.getResponseAsBinder();
			data = data2.getLocal("placeholderContent");
			serviceResponse.close();
		} catch (IdcClientException e) {
			throw new ServletException(e);
		}
		return data;
	}
}
