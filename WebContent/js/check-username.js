var userDuplicate = false;
var userNameCheck = false;
var userRoleCheck = false;
var userAgeCheck = false;
var userAgeIsInt = true;
var userStateCheck = false;

function checkUser(){	
	var input = document.getElementById("name");
	var username = input.value;
	if(username == ""){
		document.getElementById('username').innerHTML = "Name not provided";
		document.getElementById('username').style.color = 'red';
		userDuplicate = false;
		userNameCheck = false;
	}else{
		var url = "./jsp/api-signup.jsp?username=" + username+"&action="+"checkuser";
		var xmlHttp=new XMLHttpRequest();
		xmlHttp.onreadystatechange=function() {
			if (xmlHttp.readyState != 4) return;
			
			if (xmlHttp.status != 200) {
				console.log("HTTP status is " + xmlHttp.status + " instead of 200");
				return;
			};
			var responseDoc = xmlHttp.responseText;
			//console.log(responseDoc);
			var response = eval('(' + responseDoc + ')');
			if(response.success){
				if(response.found){
					document.getElementById('username').innerHTML = "Username Exists       ";
					document.getElementById('username').style.color = 'red';
					userDuplicate = true;
					userNameCheck = false;
				}else{
					document.getElementById('username').innerHTML = "Username Entered";
					document.getElementById('username').style.color = 'green';
					userDuplicate = false;
					userNameCheck = true;
				}
			}else{
				document.getElementById('username').innerHTML = "Connection Problem";
				document.getElementById('username').style.color = 'red';
				userDuplicate = false;
				userNameCheck = false;
			}
		};
		
		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
	}
}
function checkAge(){
	var input = document.getElementById("age");
	var age = input.value;
	if(age != ""){
		if(isNormalInteger(age) && age >0){
			document.getElementById('userage').innerHTML = "Age Entered";
			document.getElementById('userage').style.color = 'green';
			userAgeCheck = true;
			userAgeIsInt = true;
		}else{
			document.getElementById('userage').innerHTML = "Pleaser enter valid integer for age";
			document.getElementById('userage').style.color = 'red';
			userAgeCheck = false;
			userAgeIsInt = false;
		}
	}else{
		document.getElementById('userage').innerHTML = "Age not provided";
		document.getElementById('userage').style.color = 'red';
		userAgeCheck = false;
		userAgeIsInt = true;
	}
}
function checkRole(){
	var input = document.getElementById("role");
	var role = input.options[input.selectedIndex].text;
	if(role == ""){
		document.getElementById('userrole').innerHTML = "Role not provided";
		document.getElementById('userrole').style.color = 'red';
		userRoleCheck = false;
	}else{
		document.getElementById('userrole').innerHTML = "role selected";
		document.getElementById('userrole').style.color = 'green';
		userRoleCheck = true;
	}
}
function checkState(){
	var input = document.getElementById("state");
	var role = input.options[input.selectedIndex].text;
	if(role == ""){
		document.getElementById('userstate').innerHTML = "State not provided";
		document.getElementById('userstate').style.color = 'red';
		userStateCheck = false;
	}else{
		document.getElementById('userstate').innerHTML = "State selected";
		document.getElementById('userstate').style.color = 'green';
		userStateCheck = true;
	}
}
function signUp(){
	var success = document.getElementById('success');
	var unsuccess = document.getElementById('unsuccess'); 
	if(userNameCheck && userRoleCheck && userAgeCheck && userStateCheck){
		success.style.display='none';
		unsuccess.style.display='none';
		
		var username = document.getElementById("name").value;
		var role = document.getElementById("role").options[document.getElementById("role").selectedIndex].text;
		var age = document.getElementById("age").value;
		var state = document.getElementById("state").options[document.getElementById("state").selectedIndex].text;

		var url = "./jsp/api-signup.jsp?action=signup&"+"name="+username+"&role="+role+"&age="+age+"&state="+state;
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
			if(response.success){
				success.style.display='block';
				unsuccess.style.display='none';
				document.getElementById('success').innerHTML = response.reason;
			}else{
				success.style.display='none';
				unsuccess.style.display='block';
				document.getElementById('unsuccess').innerHTML = response.reason;
			}
		};
		
		xmlHttp.open("POST",url,true);
		xmlHttp.send(null);
	}else{
		success.style.display='none';
		unsuccess.style.display='block';
		if(!userNameCheck){
			if(userDuplicate){
				unsuccess.innerHTML = "Username existed, ";
			}else{
				unsuccess.innerHTML = "Username Not Provided, ";
			}
		}
		if(!userRoleCheck){
			var t = document.createTextNode("Role Not Provided, ");
			unsuccess.appendChild(t);
		}
		if(!userAgeCheck){
			if(userAgeIsInt){
				var t = document.createTextNode("Age Not Provided, ");
				unsuccess.appendChild(t);
			}else{
				var t = document.createTextNode("Age Is Not An Integer, ");
				unsuccess.appendChild(t);
			}
		}
		if(!userStateCheck){
			var t = document.createTextNode("State Not Provided, ");
			unsuccess.appendChild(t);
		}
	}
}
function isNormalInteger(str) {
    var n = ~~Number(str);
    return String(n) === str && n >= 0;
}