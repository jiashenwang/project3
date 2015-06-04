function refresh(){
	//var date =document.getElementById("date").value;

	var url = "./jsp/api-refresh.jsp?"+"action="+"refresh";
	var xmlHttp=new XMLHttpRequest();
	xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState != 4) return;
		
		if (xmlHttp.status != 200) {
			console.log("HTTP status is " + xmlHttp.status + " instead of 200");
			return;
		};
		var responseDoc = xmlHttp.responseText;
		console.log(responseDoc);
		var response = eval('(' + responseDoc + ')');
//			if(response.success){
//				console.log(response);
//			}
	};	
	xmlHttp.open("GET",url,true);
	xmlHttp.send(null);
	
	
}