<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="photoGallery.*"%>
<%@page import="openwcm.*"%>
<%@page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>MOHE Photo Gallery</title>

<%
	String title = request.getParameter("title");
	String comment = request.getParameter("comment");
	String ID = request.getParameter("ID");
	String contentID = request.getParameter("contentID");
	
	UpdateInfo ui = new UpdateInfo();
	String username = "sysadmin";	

	//List<ChildFolder> folders = new ArrayList<ChildFolder>();
	ui.getUpdateInfo(request, application, username, title, comment, ID, contentID);
	//String parentId = pg.getParentId();
	//List<Photo> pictures = pg.getPhotosInFolder(application,username);
%>

<link rel="stylesheet" href="css/basic.css" type="text/css" />
<link rel="stylesheet" href="css/galleriffic-2.css" type="text/css" />
<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/jquery.galleriffic.js"></script>
<script type="text/javascript" src="js/jquery.opacityrollover.js"></script>
<!-- We only want the thunbnails to display when javascript is disabled -->
</head>
<body>
<div id="page"> test
</div>
</body>
</html>