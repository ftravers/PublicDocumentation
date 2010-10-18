<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="photoGallery.*"%>
<%@page import="openwcm.*"%>
<%@page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Photo Gallery</title>
<%
	PhotoGallery pg = new PhotoGallery();
	String username = "sysadmin";	
	List<ChildFolder> folders = new ArrayList<ChildFolder>();
	pg.getChildFolders(request, application, username, folders);
	
	List<Photo> pictures = pg.getPhotosInFolder(application,username);
%>
<script>
	function setPhoto( url ) {
		var photoImgElement = document.getElementById("currentPhoto");
		photoImgElement.src = url;
	}
	var photoList = new Array;
	var counter = 0;
	var currPhoto = 0;
	<%for(Photo photo : pictures) {%>
		photoList[counter] = new Object;
		photoList[counter].url = "<%= photo.getUrl()%>";
		photoList[counter].name = "<%=photo.getFilename() %>";
		counter++;
<%}%>
	function setNextPhoto() {
		var photoImgElement = document.getElementById("currentPhoto");

		// have to substract 1 from counter because from the counter above, it have 1 more value than the index of photo
		if (currPhoto < (counter-1))
			currPhoto++;
		photoImgElement.src = photoList[currPhoto].url;
		
	}
	function setPrevPhoto() {
		var photoImgElement = document.getElementById("currentPhoto");
		if (currPhoto > 0)
			currPhoto--;
		photoImgElement.src = photoImgElement.src = photoList[currPhoto].url;
		
	}
</script>
</head>
<body>

<table>

	<tr>
		<td>
		<table>
			<tr>
				<td>
				<table>
					<%if (null != pg.getParentId()) {
					%>

					<tr>
						<td><img src="images/up.gif"> <a
							href="PhotoGallery.jsp?<%=OpenWcmServlet.COLLECTION_ID%>=<%=pg.getParentId()%>">(up)</a>
						</td>
					</tr>
					
					<%} %>
					<%for(ChildFolder childFolder : folders) {%>
					<tr>
						<td><img src="images/forward.gif"><a
							href="PhotoGallery.jsp?
							<%=OpenWcmServlet.COLLECTION_ID%>=<%=childFolder.getFolderId()%>">
						<%=childFolder.getFolderName() %> </a></td>
					</tr>
					<%}%>
				</table>
				</td>
			</tr>

			<tr>
				<td>
				<table id="photosTable">
					<%for(Photo photo : pictures) {%>
					<tr>
						<td>
						<div onClick="setPhoto('<%= photo.getUrl()%>')"><%=photo.getFilename() %></div>
						</td>
					</tr>
					<%}%>
				</table>
				</td>
			</tr>
		</table>


		</td>



		<td><!-- This is where the picture itself will be displayed --> <img
			id="currentPhoto" src="" width="500px" /> <br>
		<div onClick="setNextPhoto()">next</div>
		<div onClick="setPrevPhoto()">prev</div>
		</td>
	</tr>
</table>

</body>
</html>