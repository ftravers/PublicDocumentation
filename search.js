var searching = true

function load_content(content_name) {
    searching = false
    ajaxRequest.open("GET", content_name + ".html", true);
	  ajaxRequest.send(null); 
}
function set_details_box(body) {
    var details_box = document.getElementById('details');
    details_box.innerHTML = body   
    var searchResultsDiv = document.getElementById('results');
    searchResultsDiv.style.display = "none"
    details_box.style.display = "block"
    var searchAgainButton = document.getElementById('search_again_button');
    searchAgainButton.style.display = "inline"

}
function setSearchResults(body) {
    var details_box = document.getElementById('details');
    details_box.style.display = "none"

		var searchResultsDiv = document.getElementById('results');
    searchResultsDiv.style.display = "block"

		searchResultsDiv.innerHTML = body;    
    var searchAgainButton = document.getElementById('search_again_button');
    searchAgainButton.style.display = "none"
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
        else { 
            set_details_box(response)
        } 
		}
}

function doSearch() {
    var search_box = document.getElementById('search_input')
    searching = true
    text = search_box.value

    if ( !text ) {
        setSearchResults("");
        return;
    }

    var ajax_loader = document.getElementById('ajaxLoader');
    ajax_loader.innerHTML = "<img src='ajax-loader.gif'/>"; 
    var query = "/srch?category=public&query=" + text;
	  ajaxRequest.open("GET", query, true);
	  ajaxRequest.send(null); 
}