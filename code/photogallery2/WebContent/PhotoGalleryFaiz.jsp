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
	}
	var photoList = new Array;
	var counter = 0;
	var currPhoto = 0;
	<%for(Photo photo : pictures) {%>
		photoList[counter] = new Object;
		photoList[counter].url = "<%= photo.getUrl()%>";
		photoList[counter].name = "<%=photo.getFilename() %>";
		photoList[counter].comments = "<%=photo.getComments() %>";
		counter++;
<%}%>
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
							href="PhotoGalleryFaiz.jsp?
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
		<a href="<%= photo.getUrl()%>">Download Original</a>
	</div>
	<div class="download">
	</div>
	<div class="image-title"><%= photo.getTitle()%></div>
	<div class="image-desc">Description : <%=photo.getFilename() %></div>
	<div class="image-desc">Author : <%=photo.getCreateDate() %></div>
	
	
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
						delay : 2500,
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
						playLinkText : 'Play Slideshow',
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