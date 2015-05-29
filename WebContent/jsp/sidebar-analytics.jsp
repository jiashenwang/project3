<%@page import="java.util.List" import="helpers.*" import="models.*"%>
<%
    List<CategoryWithCount> categories = CategoriesHelper.listCategoriesNoCount();
	String rowType = request.getParameter("rowType");
	String order = request.getParameter("order");
	String category = request.getParameter("category");
%>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bottom-nav">
			<form action="analytics" method="POST" >
				<ul class="nav nav-list">
					<li><select name="rowType" id="search_key">
					       <%= AnalyticsHelper.isSelected("Customers",rowType) %>
					       <%= AnalyticsHelper.isSelected("States",rowType) %>
					</select></li>
                    <li><select name="order" id="search_key_0">
                           <%= AnalyticsHelper.isSelected("Alphabetical",order) %>
                           <%= AnalyticsHelper.isSelected("TopK",order) %>
                    </select>
					<li><select name="category" id="search_key_2">
							<option value="0" selected="selected">All Categories</option>
							<%
							    for (CategoryWithCount c : categories) {
							        out.println(AnalyticsHelper.isSelected(c.getId(), c.getName(), category));
							    }
							%>
					</select></li>
				</ul>
				<input type="hidden" name="row_offset" value="0">
				<input type="hidden" name="col_offset" value="0"> 
				<input type="hidden" name="action" value="search">
				<input style="margin : 10px 15px;" type="submit" value="Search">
			</form>
			</form>
		</div>
	</div>
</div>