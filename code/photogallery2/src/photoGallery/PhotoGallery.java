package photoGallery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import openwcm.OpenWcmServlet;
import openwcm.UcmServletInfo;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class PhotoGallery {
	private String collectionId;
	private String parentId;
	
	
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

		dataBinder.putLocal("dParentCollectionID", collectionId);
		dataBinder.putLocal("IdcService", "FCF_FETCH_CHILD_FOLDERS");
		dataBinder.putLocal("IsJava", "1");
		
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataResultSet resultSet = data.getResultSet("CHILD_FOLDERS");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				String folderId = row.get("dCollectionID");
				String folderName = row.get("dCollectionName");
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
	private String getParentIdFromDataBinder(DataBinder data) {
		String parentId = null;
		DataResultSet resultSet = data.getResultSet("PARENT_ID");
		List<DataObject> rows = resultSet.getRows();
		for (DataObject row : rows) {
			parentId = row.get("dCollectionID");
		}
		return parentId;
	}
	public List<Photo> getPhotosInFolder(ServletContext application, String username) {
		IdcContext idcContext = new IdcContext(username);
		//TODO: move all the stuff I have jammed into the application object into the single UcmServletInfo object put there.
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		
		dataBinder.putLocal( "QueryText", "xCollectionID = `" + collectionId + "`" );
		dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
		dataBinder.putLocal("IsJava", "1");

		ArrayList<Photo> photos = new ArrayList<Photo>();
		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
			DataResultSet resultSet = data.getResultSet("SearchResults");
			List<DataObject> rows = resultSet.getRows();
			for (DataObject row : rows) {
				String contentId = row.get("dDocName");
				String filename = row.get("dOriginalName");
				String comments = row.get("xComments");
				String title = row.get("dDocTitle");
				String createDate = row.get("dCreateDate");
				String ID = row.get("dID");
				String author = row.get("dDocAuthor");
				String url = OpenWcmServlet.formatUrls(application, contentId);
				Photo photo = new Photo(filename,contentId,url, comments, title, createDate, ID, author);
				photos.add(photo);
			}
			idc_response.close();
		} catch (IdcClientException e) {
			System.out.println(e);
		}
		return photos;
	}


}

