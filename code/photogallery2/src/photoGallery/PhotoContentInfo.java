package photoGallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import openwcm.OpenWcmServlet;
import openwcm.UcmServletInfo;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class PhotoContentInfo {
	private String collectionId;
	private String parentId;
	private String photoId;
	
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void getContentInfo( 
			ServletRequest request,
			ServletContext application, 
			String username) {
		
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		
		//photoId = "122";
		
		//dataBinder.putLocal( "QueryText", "dID = `" + photoId + "`" );
		//dataBinder.putLocal("dParentCollectionID", collectionId);
		dataBinder.putLocal("IdcService", "DOC_INFO");
		dataBinder.putLocal("dID", "122");
		/*
		dataBinder.putLocal("dDocName", "000076");
		dataBinder.putLocal("dDocTitle", "Moto");
		dataBinder.putLocal("xComments", "Testing");
		*/
		
		dataBinder.putLocal("IsJava", "1");
		
		
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			
			/*DataResultSet resultSet = data.getResultSet("DOC_INFO");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				
				String contentId = row.get("dID");
				//String folderName = row.get("dCollectionName");
				/*
				ChildFolder childFolder = new ChildFolder();
				childFolder.setFolderId(folderId);
				childFolder.setFolderName(folderName);
				childFolders.add(childFolder);
			}
			
			 */
			// parentId = getParentIdFromDataBinder(data);
			idc_response.close();
			
		} catch (IdcClientException e) {
			System.out.println(e);
		}
	}
	
	/*
	public void getChildFolders(
			
		ServletRequest request,
		ServletContext application, 
		String username, 
		
		List<ChildFolder> childFolders) {
		collectionId = request.getParameter(OpenWcmServlet.COLLECTION_ID);
		boolean setParentNull = false;
		if ( null == collectionId ) {
			UcmServletInfo ucmServletInfo = (UcmServletInfo) application.getAttribute(OpenWcmServlet.UCM_SERVLET_INFO);
			collectionId = ucmServletInfo.getPhotoGalleryRootNodeCollectionId();
			// leave the parent id as null as we are at the root
			setParentNull = true;
		}
		
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		
		//photoId = "122";
		
		//dataBinder.putLocal( "QueryText", "dID = `" + photoId + "`" );
		//dataBinder.putLocal("dParentCollectionID", collectionId);
		dataBinder.putLocal("IdcService", "UPDATE_DOCINFO");
		dataBinder.putLocal("dID", "122");
		dataBinder.putLocal("xComments", "Test!");
		//dataBinder.putLocal("IsJava", "1");

		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			/*DataResultSet resultSet = data.getResultSet("DOC_INFO");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				
				String contentId = row.get("dID");
				//String folderName = row.get("dCollectionName");
				/*
				ChildFolder childFolder = new ChildFolder();
				childFolder.setFolderId(folderId);
				childFolder.setFolderName(folderName);
				childFolders.add(childFolder);
			}
			
			
			parentId = getParentIdFromDataBinder(data);
			idc_response.close();
			
		} catch (IdcClientException e) {
			System.out.println(e);
		}
		
		if ( setParentNull ) {
			setParentId(null);
		}
		
	}
	
	*/
	private String getParentIdFromDataBinder(DataBinder data) {
		String parentId = null;
		DataResultSet resultSet = data.getResultSet("PARENT_ID");
		List<DataObject> rows = resultSet.getRows();
		for (DataObject row : rows) {
			parentId = row.get("dCollectionID");
		}
		return parentId;
	}
	
	/*
	//public List<PhotoInfo> getPhotosInFolder(ServletContext application, String username) {
	public getPhotoInfo(ServletContext application, String photoID)	
	
		IdcContext idcContext = new IdcContext(photoID);
		//TODO: move all the stuff I have jammed into the application object into the single UcmServletInfo object put there.
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		
		// dID = 122
		photoId = "122";
		
		dataBinder.putLocal( "QueryText", "dID = `" + photoId + "`" );
		dataBinder.putLocal("IdcService", "UPDATE_DOCINFO");
		dataBinder.putLocal("IsJava", "1");

		//ArrayList<PhotoInfo> photos = new ArrayList<PhotoInfo>();
		
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataResultSet resultSet = data.getResultSet("DOC_INFO");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				String contentId = row.get("dID");
				//String filename = row.get("dDocName");
				//String url = OpenWcmServlet.formatUrls(application, contentId);
				//PhotoInfo photoInfo = new PhotoInfo(filename,contentId,url);
				//photos.add(photoInfo);
				
				
			}
			idc_response.close();
			
		} catch (IdcClientException e) {
			System.out.println(e);
		}
		
		return photos;
	}

	*/
}

