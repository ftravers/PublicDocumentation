package openwcm;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;
/*
 * We use this class in two ways.  First it can be instantiated from inside a JSP and in
 * that case the callWcmPlaceholder(...) gets called.  This retrieves the corresponding HTML
 * and spits it back to the calling client browser.  That HTML may have links in it.  Those
 * will point back to the container, specifically the servlet setup in the web.xml file.
 * 
 * That leads to the second function of this code...which is to act as a servlet.  In that
 * capacity basically it is activates when the links in the returned HTML code above point
 * to images, or what have you, that truly reside on the content server.  When it receives
 * those requests, it parses the URL for the content id, which happens to be the filename
 * without the extension, and then does a GET_FILE service call for that resource.  It then
 * streams that file content back to the browser.
 */
public class OpenWcmServlet extends HttpServlet {
	private static final int BUFFER_SIZE = 1024;

	// We store the IdcClient object into the application object with this key
	public static final String IDC_CLIENT = "IDC_CLIENT";
	
	// We store the link prefix into the application object with this key
	public static final String LINK_PREFIX = "LINK_PREFIX";
	
	/*
	 * This is the application object where we store the IdcClient and link prefix that
	 * we use between calls to callWcmPlaceholder method and the servlet doGet.  It gets
	 * populated at startup in the servlet init() method. 
	 */
	private ServletContext application = null;
	
	/*
	 * The IdcClient contains the connection url to the content server.  This gets created
	 * at servlet init() time and stored into the application object, since it should never
	 * really change.
	 */
	private IdcClient idcClient = null;
	
	/*
	 * These two testing variables are useful if you are just using tomcat...you don't 
	 * need to log in if you use these.  You can demo the functionality of displaying
	 * and editing content from WCM.  Since you need a username, and you aren't logging
	 * in, this supplies it.  In a real circumstance you'd want to pull the username
	 * from the portal you are using and use that.
	 * 
	 * If you are in production change the web.xml file parameter: testing to false.
	 * Then the testing_user param is ignored.
	 */
	private boolean testing = false;
	private String testingUserName = null;


	/*
	 * This method is called by the display technology (JSP).  It will call the service 
	 * WCM_PLACEHOLDER, and return to the browser a string of the HTML which is retrieved by
	 * the service call from the content server.
	 */
	public String callWcmPlaceholder(String username, String dataFileContentId,
			String placeholderContentId, ServletContext application) throws ServletException {
		
		IdcContext idcContext = new IdcContext(username);
		idcClient = (IdcClient) application.getAttribute(IDC_CLIENT);
		String linkPrefix = (String)application.getAttribute(LINK_PREFIX);
		
		DataBinder dataBinder = idcClient.createBinder();
		dataBinder.putLocal("dataFileDocName", dataFileContentId);
		dataBinder.putLocal("placeholderDefinitionDocName", placeholderContentId);
		dataBinder.putLocal("SSDoLinkEvaluation", "1");
		dataBinder.putLocal("SSContributor", "true");
		dataBinder.putLocal("SSLinkUrlEvaluationPrefix", linkPrefix);
		dataBinder.putLocal("IdcService", "WCM_PLACEHOLDER");
		
		String data = null;
		DataBinder data3 = null;
		try {
			ServiceResponse resp1 = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data2 = resp1.getResponseAsBinder();
			String content = data2.getLocal("placeholderContent");
			data = content;
			resp1.close();
		} catch (IdcClientException e) {
			throw new ServletException(e);
		} 
	
		return data;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		application = config.getServletContext();
		setupIdcConnection(config);
		setupOpenWcm(config);
		testingSetup(config);
	}

	private void testingSetup(ServletConfig config) {
		String test = config.getInitParameter("testing");
		testing = Boolean.parseBoolean(test);
		if (testing) {
			testingUserName = config.getInitParameter("testing_user");
		}
	}

	/*
	 * This just sets up what the URL link prefix should be.
	 */
	private void setupOpenWcm(ServletConfig config) {
		String portalHost = config.getInitParameter("portal_host");
		String webapp_root = config.getInitParameter("webapp_root");
		String openwcmServletName = config.getInitParameter("openwcm_servlet_name");
		String linkPrefix = portalHost + "/" + webapp_root + "/" + openwcmServletName + "/"; 
		application.setAttribute(LINK_PREFIX, linkPrefix);
	}

	/*
	 * This creates the IdcClient which has the content server host name
	 * and port specified.  No user information is contained here.  That is
	 * in the context object.
	 */
	private void setupIdcConnection(ServletConfig config)
			throws ServletException {
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
			application.setAttribute(IDC_CLIENT, idcClient);
		} else {
			String errMsg = "Cannot instantiate a connection to the content server with url"
					+ idcConnectionUrl;
			throw new ServletException(errMsg);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/*
	 * This is the code that takes the URL's that have been prefixed in the HTML
	 * code and gets the resources they represent on the content server on behalf
	 * of the requesting client browser.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// Requesting URL
		String url = req.getRequestURL().toString();

		// Extract content id from URL
		String contentId = getContentIdFromFilePath(url);

		IdcContext idcContext = getContext();
		
		idcClient = (IdcClient) application.getAttribute(IDC_CLIENT);
	
		DataBinder dataBinder = idcClient.createBinder();
		dataBinder.putLocal("IdcService", "GET_FILE");
		dataBinder.putLocal("dDocName", contentId);
		dataBinder.putLocal("RevisionSelectionMethod", "latest");
		ServiceResponse response = null;
		try {
			response = idcClient.sendRequest(idcContext, dataBinder);
		} catch (IdcClientException e) {
			throw new ServletException(e);
		}

		// Set the content type of the returned file
		res.setContentType(response.getHeader("Content-type"));

		// Connect the streams together to stream it back to the client
		OutputStream os = res.getOutputStream();
		InputStream is = response.getResponseStream();

		try {
			byte[] b = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = is.read(b)) != -1) {
				os.write(b, 0, bytesRead);
			}
		} finally {
			os.flush();
			os.close();
			is.close();
			response.close();
			res.flushBuffer();
		}
	}
	
	private String getContentIdFromFilePath(String filePath) {
		// sample filePath:
		// http://db/idc/groups/public/documents/adacct/image.png
		String[] strArray = filePath.split("/");
		String lastElement = strArray[strArray.length - 1];
		String contentIdOnly = lastElement.substring(0, lastElement
				.lastIndexOf("."));
		return contentIdOnly;
	}
	/*
	 * This method should be modified if you are placing this code into a portal
	 * environment.  You should update the web.xml to change testing to false so
	 * you fall into the else clause, then you should extract the username (probably
	 * from the req object that the servlet doGet method has...feel free to modify
	 * the parameters of this method call, as it is only called from one place
	 * inside this code.
	 */
	private IdcContext getContext() {
		IdcContext idcContext = null;
		if ( testing ) { 
			idcContext = new IdcContext(testingUserName);
		} else {
			// extract username from somewhere change this...
			idcContext = new IdcContext("");
		}
		return idcContext;
	}
}
