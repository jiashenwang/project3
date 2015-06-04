<%@page import="java.util.List" import="helpers.*" import="models.*" %>
<%@page import="org.json.JSONObject" %>

<%
	String action = (String)request.getParameter("action");
	if(action == null) action = "";
	
	if(action.equals("refresh")){
		String date = (String)session.getAttribute("Personal_Time_Stamp");
		
		JSONObject r = AnalyticsHelper.Refresh(date);
		session.setAttribute("Personal_Time_Stamp", AnalyticsHelper.getGlobalTimeStamp());
		
		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("result", r);
		out.print(result);
		out.flush();
	}else{
		JSONObject result = new JSONObject();
		result.put("success", false);
		out.print(result);
		out.flush();
	}
	
%>