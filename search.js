var searching = true
function load_content(content_name) {
    searching = false
    ajaxRequest.open("GET", content_name + ".html", true);
	  ajaxRequest.send(null); 
}
function set_details_box(body) {
    var details_box = document.getElementById('details');
    details_box.innerHTML = body   
}

function setSearchResults(body) {
		var ajaxDisplay = document.getElementById('results');
		ajaxDisplay.innerHTML = body;    
}

ajaxRequest = new XMLHttpRequest();

ajaxRequest.onreadystatechange = function(){

		if(ajaxRequest.readyState == 4) {
        var response = ajaxRequest.responseText
        if (searching) { 
            setSearchResults(response);
            var ajax_loader = document.getElementById('ajaxLoader');    
            ajax_loader.innerHTML = "";
        }
        else { set_details_box(response) }            
		}
}

function doSearch(input) {
    searching = true
    text = input.value
    if ( !text ) {
        setSearchResults("");
        return;
    }
    var ajax_loader = document.getElementById('ajaxLoader');
    ajax_loader.innerHTML = "<img src='ajax-loader.gif'/>";
	  ajaxRequest.open("GET", "/srch?query=" + text, true);
	  ajaxRequest.send(null); 
}
