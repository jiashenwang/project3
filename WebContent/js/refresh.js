
var id_buffer=[];

function refresh(){
	for(var i=0; i<id_buffer.length; i++){
		var cell = document.getElementById(id_buffer[i]);
		if(cell!=null){
			cell.style.color = "black";
		}
	}
	id_buffer = [];
	//var date =document.getElementById("date").value;
	
	//document.getElementById("table").style.color = "#000000";
	var url = "./jsp/api-refresh.jsp?"+"action="+"refresh";
	var xmlHttp=new XMLHttpRequest();
	xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState != 4) return;
		
		if (xmlHttp.status != 200) {
			console.log("HTTP status is " + xmlHttp.status + " instead of 200");
			return;
		};
		var responseDoc = xmlHttp.responseText;
		var response = eval('(' + responseDoc + ')');

		var select = document.getElementById("s");
		console.log(response.result.pre_states_all);
		console.log(response.result.pre_state_cate);
		console.log(response.result.pre_middle);
		console.log(response.result.pre_products_cid);
		if(select.value=="0" ){
			for(var i=0; i<response.result.pre_states_all.length; i++){
				var cell = document.getElementById("s"+response.result.pre_states_all[i].stateid);
				if(cell!=null){
					cell.innerHTML = response.result.pre_states_all[i].sum;
					cell.style.color = 'red';
				}
				id_buffer.push("s"+response.result.pre_states_all[i].stateid);
			}
		}else{
			for(var i=0; i<response.result.pre_state_cate.length; i++){
				var cell = document.getElementById("s"+response.result.pre_state_cate[i].stateid);
				if(cell!=null){
					cell.innerHTML = response.result.pre_state_cate[i].sum;
					cell.style.color = 'red';
				}
				id_buffer.push("s"+response.result.pre_state_cate[i].stateid);
			}
		}
		for(var i=0; i<response.result.pre_middle.length; i++){
			var cell = document.getElementById(response.result.pre_middle[i].stateid+";"+response.result.pre_middle[i].pid);
			if(cell!=null){
				cell.innerHTML = response.result.pre_middle[i].sum;
				cell.style.color = 'red';
			}
			id_buffer.push(response.result.pre_middle[i].stateid+";"+response.result.pre_middle[i].pid);
		}
		for(var i=0; i<response.result.pre_products_cid.length; i++){
			var cell = document.getElementById("p"+response.result.pre_products_cid[i].pid);
			if(cell!=null){
				cell.innerHTML = response.result.pre_products_cid[i].sum;
				cell.style.color = 'red';
			}
			id_buffer.push("p"+response.result.pre_products_cid[i].pid);
		}
		
	};	
	xmlHttp.open("GET",url,true);
	xmlHttp.send(null);
	
	
}