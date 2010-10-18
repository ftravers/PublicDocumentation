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
	PhotoGallery pg = new PhotoGallery();
	String username = "sysadmin";	
	List<ChildFolder> folders = new ArrayList<ChildFolder>();
	pg.getChildFolders(request, application, username, folders);
	String parentId = pg.getParentId();
	List<Photo> pictures = pg.getPhotosInFolder(application,username);
%>
<script>
	function setPhoto( url ) {
		var photoImgElement = document.getElementById("currentPhoto");
		photoImgElement.src = url;

		var commentsInputBox = document.getElementById("commentsInput");
		//commentsInputBox

		var titleInputBox = document.getElementById("titleInput");
		//titleInputBox
	}
	var photoList = new Array;
	var counter = 0;
	var currPhoto = 0;
	<%for(Photo photo : pictures) {%>
		photoList[counter] = new Object;
		photoList[counter].url = "<%= photo.getUrl()%>";
		photoList[counter].name = "<%=photo.getFilename() %>";
		photoList[counter].comments = "<%=photo.getComments() %>";
		photoList[counter].title = "<%=photo.getTitle() %>";
		photoList[counter].ID = "<%=photo.getID() %>";
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
<script>
function resize(which, max) {
	  var elem = document.getElementById(which);
	  if (elem == undefined || elem == null) return false;
	  if (max == undefined) max = 100;
	  if (elem.width > elem.height) {
	    if (elem.width > max) elem.width = max;
	  } else {
	    if (elem.height > max) elem.height = max;
	  }
	}
</script>

<script type="text/javascript">
function updateInfo() {
	
var ajaxRequest;  // The variable that makes Ajax possible!
	
	try{
		// Opera 8.0+, Firefox, Safari
		ajaxRequest = new XMLHttpRequest();
	} catch (e){
		// Internet Explorer Browsers
		try{
			ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try{
				ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e){
				// Something went wrong
				alert("Your browser broke!");
				return false;
			}
		}
	}
	/* Create a function that will receive data sent from the server
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			document.form.time.value = ajaxRequest.responseText;
		}
	}
	*/
	
	var title = document.getElementById('fTitle').value;
	var comment = document.getElementById('fComments').value;
	var ID = document.getElementById('fID').value;
	var contentID = document.getElementById('fContentID').value;
	
	var queryString = "?title=" + title + "&comment=" + comment + "&ID=" + ID + "&contentID=" + contentID;
	ajaxRequest.open("GET", "UpdateInfo.jsp" + queryString, true);
	ajaxRequest.send(null); 

}

function showhide(divid, state){
	document.getElementById(divid).style.display=state;
	}

</script>

<link rel="stylesheet" href="css/basic.css" type="text/css" />
<link rel="stylesheet" href="css/galleriffic-2.css" type="text/css" />
<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/jquery.galleriffic.js"></script>
<script type="text/javascript" src="js/jquery.opacityrollover.js"></script>
<!-- We only want the thunbnails to display when javascript is disabled -->
<script type="text/javascript">
	document.write('<style>.noscript { display: none; }</style>');
</script>
</head>
<body>
<div id="page">
<div id="container">
<h1>Photo Gallery</h1>
<!-- <h2>Thumbnail rollover effects and slideshow crossfades</h2> --> <!-- Start Advanced Gallery Html Containers -->
			
<div id="gallery" class="content">
<div id="controls" class="controls"></div>
<div class="slideshow-container">
<div id="loading" class="loader"></div>
<div id="slideshow" class="slideshow"></div>
</div>
<div id="caption" class="caption-container"></div>
</div>
<div id="thumbs" class="navigation">
<table>
					<%if (null != pg.getParentId()) {
					%>

					<tr>
						<td><a href="javascript:history.go(-1)"  onMouseOver="self.status=document.referrer;return true"><img src="images/up.gif" border = 0>
						
						(up)</a>
						</td>
					</tr>
					
					<%} %>
					<%for(ChildFolder childFolder : folders) {%>
					<tr>
						<td><img src="images/forward.gif"><a
							href="PhotoGalleryBai.jsp?
							<%=OpenWcmServlet.COLLECTION_ID%>=<%=childFolder.getFolderId()%>">
						<%=childFolder.getFolderName() %> </a></td>
					</tr>
					<%}%>
				</table>

<ul class="thumbs noscript">
<%for(Photo photo : pictures) {%>
	<li><a class="thumb" name="leaf"
		href="<%= photo.getUrl()%>"
		title="<%=photo.getFilename() %>"> <img 
		src="<%= photo.getUrl()%>"
		alt="Title #0" /> </a>
	<div class="caption">
		<div class="download">
			<a href="#" onClick="showhide('form', 'block'); return false"">Update</a> | 
			<a href="<%= photo.getUrl()%>">Download Original</a> 
		</div>
		<div class="image-title"><%= photo.getTitle()%></div> <!-- photo title -->
		<div class="image-desc">filename : <%=photo.getFilename() %></div>
		<div class="image-desc">Create date : <%=photo.getCreateDate() %></div>
		<br></br>
		<div id="form" style="display:none">
		<table>
			<tr>
				<td>Title</td>
				<td><input type="text" name="fTitle" size="40" id="fTitle" value="<%=photo.getTitle() %>"></input></td>
			</tr>
			<tr>
				<td>Comments</td>
				<td><input type="text" name="fComments" size="40" id="fComments" value="<%=photo.getComments() %>"></input></td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<input type="hidden" id="fID" value="<%=photo.getID() %>"></input>
					<input type="hidden" id="fContentID" value="<%=photo.getContentId() %>"></input>
					<input type="submit" id="submitInput" value="Submit" onclick="updateInfo()">
				</td>
			</tr>
		</table>
		</div>
	
	</div>
	</li>

<%}%>
</ul>
</div>
<div style="clear: both;"></div>
</div>
</div>

<div id="footer">&copy; 2010 Ministry of Higher Education</div>
<script type="text/javascript">
	jQuery(document).ready(function($) {
		// We only want these styles applied when javascript is enabled
			$('div.navigation').css( {
				'width' : '300px',
				'float' : 'left'
			});
			$('div.content').css('display', 'block');

			// Initially set opacity on thumbs and add
			// additional styling for hover effect on thumbs
			var onMouseOutOpacity = 0.67;
			$('#thumbs ul.thumbs li').opacityrollover( {
				mouseOutOpacity : onMouseOutOpacity,
				mouseOverOpacity : 1.0,
				fadeSpeed : 'fast',
				exemptionSelector : '.selected'
			});

			// Initialize Advanced Galleriffic Gallery
			var gallery = $('#thumbs').galleriffic(
					{
						delay : 0,
						numThumbs : 15,
						preloadAhead : 10,
						enableTopPager : true,
						enableBottomPager : true,
						maxPagesToShow : 7,
						imageContainerSel : '#slideshow',
						controlsContainerSel : '#controls',
						captionContainerSel : '#caption',
						loadingContainerSel : '#loading',
						renderSSControls : true,
						renderNavControls : true,
						//playLinkText : 'Play Slideshow',
						pauseLinkText : 'Pause Slideshow',
						prevLinkText : '&lsaquo; Previous Photo',
						nextLinkText : 'Next Photo &rsaquo;',
						nextPageLinkText : 'Next &rsaquo;',
						prevPageLinkText : '&lsaquo; Prev',
						enableHistory : false,
						autoStart : false,
						syncTransitions : true,
						defaultTransitionDuration : 900,
						onSlideChange : function(prevIndex, nextIndex) {
							// 'this' refers to the gallery, which is an extension of $('#thumbs')
						this.find('ul.thumbs').children().eq(prevIndex).fadeTo(
								'fast', onMouseOutOpacity).end().eq(nextIndex)
								.fadeTo('fast', 1.0);
					},
					onPageTransitionOut : function(callback) {
						this.fadeTo('fast', 0.0, callback);
					},
					onPageTransitionIn : function() {
						this.fadeTo('fast', 1.0);
					}
					});
		});
</script>
</body>
</html>