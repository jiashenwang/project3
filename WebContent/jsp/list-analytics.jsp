<%@page import="java.util.*" import="helpers.*" import="models.*"%>
<%
    String action = request.getParameter("action");
    if (action != null) {
        String rowType = request.getParameter("rowType");
        String order = request.getParameter("order");
        String category = request.getParameter("category");
        Integer rowOffset = null, colOffset = null;
        try {
            rowOffset = Integer.parseInt(request.getParameter("row_offset"));
            colOffset = Integer.parseInt(request.getParameter("col_offset"));            
        } catch (NumberFormatException e) {}
        if (rowType == null || category == null || rowOffset == null
                || colOffset == null || order == null) {
            out.println(HelperUtils.printError("Bad request. Please try again."));
        } else {
            Analytics analytics;
            if (rowType.equals("States")) {
                analytics = AnalyticsHelper.AnalysisForStates(order, category, rowOffset, colOffset, out);
            } else {
                analytics = AnalyticsHelper.AnalysisForUsers(order, category, rowOffset, colOffset, out);
            }
            String catStr = (category.equals("0")) ? "All" : category;
            String result = "Searching " + rowType;
            result += ",category=\"" + catStr + "\"";
            result += ",order=\"" + order + "\"";
            result += ",time=" + analytics.getTime_taken() + "ms)";
            out.println(HelperUtils.printSuccess(result));
            int size = 10 - analytics.getProducts().size();
            int [][] prices = analytics.getPrices();
            int i = 0, j = 0;
%>
<table class="table table-bordered" align="center">
	<thead>
		<tr align="center">
			<th class="col-md-1"><B></B></th>
			<%
			    for (Integer pid : analytics.getProductPositions()) {
			        Integer price = analytics.getProductPrices().get(pid);
			        price = (price != null) ? price : 0;
			%>
			<th class="col-md-1"><B><%=analytics.getProducts().get(pid).getName()%></B> <div style="font-weight: normal;"> <%= price %> </div></th>
			<%
			    }
			%>
			<%
			    for (int k = 0; k < size; k++) {
			%>
			<th class="col-md-1"><B></B></th>
			<%
			    }
			%>
		</tr>
	</thead>
	<tbody>
	<% if (rowType.equals("States")) { %>
        <%
            for (Integer sid : analytics.getStatePositions()) {
                Integer price = analytics.getStatePrices().get(sid);
                price = (price != null) ? price : 0;
        %>
        <tr align="center">
            <td class="col-md-1"><B><%=analytics.getStates().get(sid).getName()%></B> <div> <%= price %> </div> </td>
            <%
                for (Integer pid : analytics.getProductPositions()) {
            %>
            <td class="col-md-1"> <%= prices[i][j] %></td>
            <%
                j++;
                }
            %>
            <%
                for (int k = 0; k < size; k++) {
            %>
            <td class="col-md-1"></td>
            <%
                }
            %>
        </tr>
        <%
            i++;
            j = 0;
            }
        %>
	<% } else { %>
		<%
		    for (Integer uid : analytics.getUserPositions()) {
		        Integer price = analytics.getUserPrices().get(uid);
		        price = (price != null) ? price : 0;
		%>
		<tr align="center">
			<td class="col-md-1"><B><%=analytics.getUsers().get(uid).getUserName()%></B> <div> <%= price %> </div> </td>
			<%
			    for (Integer pid : analytics.getProductPositions()) {
			%>
			<td class="col-md-1"> <%= prices[i][j] %></td>
			<%
			    j++;
			    }
			%>
			<%
			    for (int k = 0; k < size; k++) {
			%>
			<td class="col-md-1"></td>
			<%
			    }
			%>
		</tr>
		<%
		    i++;
		    j = 0;
		    }
		%>
		<% } %>
	</tbody>
</table>
<%
int rowCount = (rowType.equals("States")) ? analytics.getTotalStateCount() : analytics.getTotalUserCount();
int colCount = analytics.getTotalProductCount();
%>
<% if (colOffset + 10 < colCount) { %>
<div class="row">
	<div class="span12">
<form class="pull-right" action="analytics" method="POST">
<input type="hidden" name="rowType" value="<%=rowType%>">
<input type="hidden" name="order" value="<%=order%>">
<input type="hidden" name="category" value="<%=category%>">
<input type="hidden" name="row_offset" value="<%=rowOffset%>">
<input type="hidden" name="col_offset" value="<%=colOffset + 10%>">
<input type="hidden" name="action" value="search">
<input style="margin : 10px 15px;" type="submit"
                value="Next 10 Products">
</form>
<% } %>
<% if (rowOffset + 20 < rowCount) { %>
<form  action="analytics" method="POST">
<input type="hidden" name="rowType" value="<%=rowType%>">
<input type="hidden" name="order" value="<%=order%>">
<input type="hidden" name="category" value="<%=category%>">
<input type="hidden" name="row_offset" value="<%=rowOffset + 20%>">
<input type="hidden" name="col_offset" value="<%=colOffset%>">
<input type="hidden" name="action" value="search">
<input style="margin : 10px 15px;" type="submit"
				value="Next 20 <%=rowType%>">
</form>
<% } %>
        </div>
    </div>
<%
        }
    }
%>