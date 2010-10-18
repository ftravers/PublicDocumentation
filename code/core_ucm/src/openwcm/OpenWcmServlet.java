package openwcm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	private static final String TESTING_USER = "testing_user";
	private static final String PHOTO_GALLERY_ROOT_COLLECTION_ID = "photoGalleryRootCollectionId";
	public static final String COLLECTION_ID = "collectionId";
	public static final String PARENT_COLLECTION_ID = "parentCollectionId";
	public static final String DATAFILE_ID = "datafileId";
	public static final String PLACEHOLD_ID = "placeholdId";
	public static final String PHOTO_GALLERY_BREADCRUMB = "photoGalleryBreadcrumb";

	private static final int BUFFER_SIZE = 1024;

	// We store the IdcClient object into the application object with this key
	public static final String IDC_CLIENT = "IDC_CLIENT";

	// We store the link prefix into the application object with this key
	public static final String LINK_PREFIX = "LINK_PREFIX";
	public static final String SERVLET_CONTEXT_NAME = "WEBAPP_NAME";
	public static final String CONTENT_ID = "contentId";
	public static final String UCM_SERVLET_INFO = "ucmServletInfo";
	private static final String SITE_ID = "siteId";

	/*
	 * if you are just using tomcat...you don't need to log in if you use these.
	 * You can demo the functionality of displaying and editing content from
	 * WCM. Since you need a username, and you aren't logging in, this supplies
	 * it. In a real circumstance you'd want to pull the username from the
	 * portal you are using and use that.
	 * 
	 * If you are in production change the web.xml file parameter: testing to
	 * false. Then the testing_user param is ignored.
	 */

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

		IdcClient idcClient = getIdcClient(application);

		String linkPrefix = (String) application.getAttribute(LINK_PREFIX);
		UcmServletInfo info = (UcmServletInfo) application.getAttribute(OpenWcmServlet.UCM_SERVLET_INFO);
		
		
		
		DataBinder dataBinder = idcClient.createBinder();
		dataBinder.putLocal(SITE_ID, info.getSiteId());
		
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

	private IdcClient getIdcClient(ServletContext application) {
		UcmServletInfo info = (UcmServletInfo) application.getAttribute(OpenWcmServlet.UCM_SERVLET_INFO);
		return info.getIdcClient();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String servletName = config.getServletName();
		String servletContextName = config.getServletContext().getServletContextName();
		String photoGalleryRootCollectionId = config.getInitParameter(PHOTO_GALLERY_ROOT_COLLECTION_ID);
		String linkPrefix = "/" + servletContextName + "/" + servletName + "/";
		IdcClient idcClient = setupIdcConnection(config);
		String siteId = config.getInitParameter(SITE_ID);
		String testingUserName = config.getInitParameter(TESTING_USER);
		String test = config.getInitParameter("testing");
		boolean testing = Boolean.parseBoolean(test);
		UcmServletInfo ucmServletInfo = new UcmServletInfo(testingUserName, testing, servletName, servletContextName,
				photoGalleryRootCollectionId, idcClient, linkPrefix, siteId);
		config.getServletContext().setAttribute(UCM_SERVLET_INFO, ucmServletInfo);

	}

	/*
	 * This creates the IdcClient which has the content server host name and
	 * port specified. No user information is contained here. That is in the
	 * context object.
	 */
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

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	/*
	 * This is the code that takes the URL's that have been prefixed in the HTML
	 * code and gets the resources they represent on the content server on
	 * behalf of the requesting client browser.
	 */
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Requesting URL
		String url = req.getRequestURL().toString();
		String contentId = req.getParameter(CONTENT_ID);

		if (null == contentId || "".equalsIgnoreCase(contentId)) {
			// Extract content id from URL
			contentId = getContentIdFromFilePath(url);
		}
		IdcClient idcClient = getIdcClient(this.getServletContext());

		String username = getUsername(this.getServletContext(), req);
		sendContentServerFileIntoResponse(username, res, contentId, idcClient);
	}

	/*
	 * This method should be modified if you are placing this code into a portal
	 * environment. You should update the web.xml to change testing to false so
	 * you fall into the else clause, then you should extract the username
	 * (probably from the req object that the servlet doGet method has...feel
	 * free to modify the parameters of this method call, as it is only called
	 * from one place inside this code.
	 */
	private String getUsername(ServletContext servletContext, HttpServletRequest req) {
		UcmServletInfo ucmServletInfo = getUcmServletInfo(servletContext);
		if (ucmServletInfo.isTesting())
			return ucmServletInfo.getTestingUsername();
		/*
		 * The rest of this code should be changed once this code is ported to
		 * oracle portal to extract the username from the request object and
		 * return that instead. Change the web.xml so that the init parameter:
		 * 'testing' is set to false.
		 */
		return null;
	}

	private UcmServletInfo getUcmServletInfo(ServletContext servletContext) {
		UcmServletInfo ucmServletInfo = (UcmServletInfo) servletContext.getAttribute(OpenWcmServlet.UCM_SERVLET_INFO);
		return ucmServletInfo;
	}

	private void sendContentServerFileIntoResponse(String username, HttpServletResponse res, String contentId,
			IdcClient idcClient) throws ServletException, IOException {
		ServiceResponse response = getContentServerFile(username, contentId, idcClient);

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

	private ServiceResponse getContentServerFile(String username, String contentId, IdcClient idcClient)
			throws ServletException {
		IdcContext idcContext = new IdcContext(username);
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
		return response;
	}

	private String getContentIdFromFilePath(String filePath) {
		// sample filePath:
		// http://db/idc/groups/public/documents/adacct/image.png
		String[] strArray = filePath.split("/");
		String lastElement = strArray[strArray.length - 1];
		String contentIdOnly = lastElement.substring(0, lastElement.lastIndexOf("."));
		return contentIdOnly;
	}

	public static String formatUrls( ServletContext application, String contentId ) {
		UcmServletInfo ucmServletInfo = (UcmServletInfo) application.getAttribute(UCM_SERVLET_INFO);
		String servletContextName = ucmServletInfo.getServletContextName();
		String servletName = ucmServletInfo.getServletName();
		String url = "/" + servletContextName + "/" + servletName +"?" + CONTENT_ID + "=" + contentId;
		return url;
	}

}
