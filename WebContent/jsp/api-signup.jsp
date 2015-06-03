<%@page import="java.util.List" import="helpers.*" import="models.*" %>
<%@page import="org.json.JSONObject" %>

<%
	String action = (String)request.getParameter("action");
	if(action == null) action = "";
	
	if(action.equals("checkuser")){
		String username = (String)request.getParameter("username");
		if(username == null) username = "";
		boolean found = false;
		found = UsernameHelper.findUser(username);
		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("found", found);  
	    out.print(result);
	    out.flush();
	}else if(action.equals("signup")){
		String reason="";
    	String name = null, role = null, state = null;
    	Integer age = null;
		JSONObject result = new JSONObject();
    	try {
    		name = request.getParameter("name");
    	} catch (Exception e) {
    		name = null;
    	}
    	try {
    		role = request.getParameter("role");
    	} catch (Exception e) {
    		role = null;
    	}
    	try {
    		age = Integer.parseInt(request.getParameter("age"));
    	} catch (Exception e) {
    		age = null;
    	}
    	try {
    		state = request.getParameter("state");
    	} catch (Exception e) {
    		state = null;
    	}
    	if (name != null && age != null && role != null && state != null){
    		reason = helpers.SignupHelper2.signup(name, age, role, state);
    		result.put("success", SignupHelper2.success);
    		result.put("reason", reason);
    	}else{
    		result.put("success", false);
    		if(name != null)
    			reason+="username is null. ";
    		if(age != null)
    			reason+="age is null. ";
    		if(role != null)
    			reason+="role is null. ";
    		if(state != null)
    			reason+="state is null. ";
    		result.put("reason", reason);
    	}
		
		out.print(result);
		out.flush(); 
	}else{
		JSONObject result = new JSONObject();
		result.put("success", false);
		out.print(result);
		out.flush();
	}
	
%>