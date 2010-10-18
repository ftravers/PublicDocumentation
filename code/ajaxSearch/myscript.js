

function submitForm()
{ 
    var xhr; 
    var jsonData;

    try {  xhr = new ActiveXObject('Msxml2.XMLHTTP');   }
    catch (e) 
    {
        try {   xhr = new ActiveXObject('Microsoft.XMLHTTP');    }
        catch (e2) 
        {
          try {  xhr = new XMLHttpRequest();     }
          catch (e3) {  xhr = false;   }
        }
     }
  
    xhr.onreadystatechange  = function()
    { 
         if(xhr.readyState  == 4)
         {
              if(xhr.status  == 200) {
                  document.ajax.dyn="Received:"  + xhr.responseText; 
                  jsonData = xhr.responseText;
                  var f = "enton";
                  }
              else 
                 document.ajax.dyn="Error code " + xhr.status;
         }
    }; 

	xhr.open(GET, "file:///Users/fentontravers/Documents/workspace2/ajaxSearch/",  true);
	//xhr.open(GET, "http://ucmel/idc/idcplg?IdcService=GET_SEARCH_RESULTS&SortField=dInDate&SortOrder=Desc&ResultCount=20&QueryText=&listTemplateId=&ftx=&TargetedQuickSearchSelection=&SearchQueryFormat=Universal&MiniSearchText=&go=Search&IsJson=1",  true); 
	xhr.send(null);

} 

