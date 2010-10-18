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
	PhotoContentInfo pci = new PhotoContentInfo();
	String username = "sysadmin";	

	pci.getContentInfo(request, application, username);
	//String parentId = pg.getParentId();
	//List<Photo> pictures = pg.getPhotosInFolder(application,username);
%>

</head>
<body>
<form name="formupdatecontentinfo" method="post" action="UpdateInfo.jsp">
<table>
	<tr>
		<td>Title</td>
		<td><input type="text" name="fTitle" size="15" id="titleInput"></input></td>
	</tr>
	<tr>
		<td>Comments</td>
		<td><input type="text" name="fComments" size="15" id="commentsInput"></input></td>
	</tr>
</table>
</form>
</body>
</html>