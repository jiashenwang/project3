package helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.jsp.JspWriter;

import models.Analytics;
import models.Product;
import models.State;
import models.User;

public class AnalyticsHelper {

    static final int show_num_row = 50, show_num_col = 50;
    
    public static Analytics AnalysisForStates(String order, String category, int rowOffset, int colOffset, JspWriter out) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Statements that vary among user configurations
        String statesQuery = null, statesQuery2 = null, statesQuery3 = null, orderquery = null;
        String productQuery = null, productQuery2 = null, productQuery3 = null;
        String colCount = null;
        int maxState, maxProduct;
        String cellsQuery = null;
        String rowCount = "50";

        try {
            conn = HelperUtils.connect();
            stmt = conn.createStatement();
            if ("0".equals(category)) {
            	statesQuery3 = "SELECT * FROM pre_states_all ORDER BY sum DESC;";
            	productQuery3 = "SELECT pid, pname, sum FROM pre_products_cid ORDER BY sum DESC LIMIT 50;";
            	cellsQuery = "SELECT s.stateid, p.pid, pre_middle.sum FROM pre_states_all s, (SELECT * FROM pre_products_cid ORDER BY sum DESC LIMIT 50) p, pre_middle WHERE s.stateid = pre_middle.stateid AND p.pid = pre_middle.pid;";
            	colCount = "50";
            } else {
            	statesQuery3 = "SELECT states.id, states.name, sum FROM states LEFT OUTER JOIN (SELECT * FROM pre_state_cate WHERE pre_state_cate.cid ="+category+") AS st ON st.stateid = states.id  ORDER BY sum DESC NULLS LAST;";
            	productQuery3 = "SELECT pid, pname, sum FROM pre_products_cid WHERE pre_products_cid.cid = "+category+" ORDER BY sum DESC LIMIT 50;";
            	cellsQuery = "SELECT s.stateid, p.pid, pre_middle.sum FROM pre_states_all s, (SELECT pid, pname, sum FROM pre_products_cid WHERE pre_products_cid.cid = "+category+" ORDER BY sum DESC LIMIT 50) p, pre_middle WHERE s.stateid = pre_middle.stateid AND p.pid = pre_middle.pid;";
            	colCount = "50";
            }

            // Dump on console query results;
//            System.out.println("==========Query Dump (" + new Date() + ")========");
//            System.out.println("User Query 1 : " + statesQuery);
//            System.out.println("User Query 2 : " + statesQuery2);
//            System.out.println("User Query 3 : " + statesQuery3);
//            System.out.println("Product Query 1 : " + productQuery);
//            System.out.println("Product Query 2 : " + productQuery2);
//            System.out.println("Product Query 3 : " + productQuery3);
//            System.out.println("Cells Query : " + cellsQuery);
//            System.out.println("Row Count Query: " + rowCount);
//            System.out.println("Column Count Query: " + colCount);

            long start = System.currentTimeMillis();
            // Create and fill temp tables
            // Get counts
            maxState = 50;
            maxProduct = 50;

            HashMap<Integer, State> states = new HashMap<Integer, State>();
            ArrayList<Integer> statePosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> statePrices = new HashMap<Integer, Integer>();
            HashMap<Integer, Product> products = new HashMap<Integer, Product>();
            ArrayList<Integer> productPosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> productPrices = new HashMap<Integer, Integer>();

            rs = stmt.executeQuery(statesQuery3);
            //System.out.println("HERE3");
            while (rs.next()) {
                String name = rs.getString(2);
                int sid = rs.getInt(1);
                int amount = rs.getInt(3);
                states.put(sid, new State(sid, name));
                statePosition.add(sid);
                statePrices.put(sid, amount);
            }
            rs.close();

            rs = stmt.executeQuery(productQuery3);
            while (rs.next()) {
                String name = rs.getString(2);
                int pid = rs.getInt(1);
                int amount = rs.getInt(3);
                productPrices.put(pid, amount);
                products.put(pid, new Product(pid, 0, name, null, 0));
                productPosition.add(pid);
            }
            rs.close();
            int[][] prices = new int[show_num_row][show_num_col];
            rs = stmt.executeQuery(cellsQuery);
            while (rs.next()) {
                int uid = rs.getInt(1);
                int pid = rs.getInt(2);
                int amount = rs.getInt(3);
                //System.out.println("uid:"+ uid +" pid:"+ pid + " amount:"+ amount);
                prices[statePosition.indexOf(uid)][productPosition.indexOf(pid)] = amount;
            }
            rs.close();
            long end = System.currentTimeMillis();
            long time_taken = end - start;
            Analytics a = new Analytics(0, maxProduct, maxState, productPrices, null, statePrices, products, null,
                    states, productPosition, null, statePosition, prices, time_taken);
            return a;
        } catch (Exception e) {
            printError(out, e);
            return new Analytics();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                //if (stmt2 != null) {
                  //  stmt2.close();
                //}
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Analytics AnalysisForUsers(String order, String category, int rowOffset, int colOffset, JspWriter out) {
        Connection conn = null;
        Statement stmt = null, stmt2 = null;
        ResultSet rs = null;
        // Statements that vary among user configurations
        String tmpTable1 = null, tmpTable2 = null;
        String userQuery = null, userQuery2 = null, userQuery3 = null;
        String productQuery = null, productQuery2 = null, productQuery3 = null;
        String colCount = null;
        int maxUser, maxProduct;

        //Statement that do not change
        String cellsQuery = "select s.uid, s.pid, coalesce(sum(s.quantity*s.price),0) from u_t u,p_t p, sales s where s.uid=u.id and s.pid=p.id group by s.uid, s.pid;";
        String rowCount = "select count(*) from users";

        try {
            conn = HelperUtils.connect();
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            if ("Alphabetical".equals(order)) {
                tmpTable1 = "CREATE TEMP TABLE p_t (name text, id int);";
                tmpTable2 = "CREATE TEMP TABLE u_t (name text, id int);";
                if ("0".equals(category)) {
                    userQuery = "select name, id from users order by name desc offset " + rowOffset + " limit 20";
                    userQuery2 = "insert into u_t (name, id) " + userQuery;
                    userQuery3 = "select u.name, u.id, coalesce(sum(s.quantity*s.price),0) from  u_t u left outer join sales s on s.uid=u.id group by u.id, u.name order by u.name desc;";
                    productQuery = "select name, id from products order by name desc offset " + colOffset + " limit "
                            + show_num_col;
                    productQuery2 = "insert into p_t (name, id) " + productQuery;
                    productQuery3 = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) from p_t p left outer join sales s on s.pid=p.id  group by p.id, p.name order by p.name desc;";
                    colCount = "select count(*) from products";
                } else {
                    userQuery = "select name, id from users order by name desc offset " + rowOffset + " limit 20";
                    userQuery2 = "insert into u_t (name, id) " + userQuery;
                    userQuery3 = "select u.name, u.id, coalesce(sum(s.quantity*s.price),0) from  u_t u left outer join (select s2.uid as uid, s2.pid as pid, s2.price as price, s2.quantity as quantity  from sales s2, products p where s2.pid = p.id and p.cid = "
                            + category + ") s ON s.uid=u.id group by u.id, u.name;";
                    productQuery = "select name, id from products where cid=" + category
                            + " order by name desc offset " + colOffset + " limit " + show_num_col;
                    productQuery2 = "insert into p_t (name, id) " + productQuery;
                    productQuery3 = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) from p_t p left outer join sales s on s.pid=p.id  group by p.id, p.name order by p.name desc;";
                    colCount = "select count(*) from products where cid =" + category;
                }
            } else {
                tmpTable1 = "CREATE TEMP TABLE p_t (name text, id int, price int);";
                tmpTable2 = "CREATE TEMP TABLE u_t (name text, id int, price int);";
                if ("0".equals(category)) {
                    userQuery = "select u.name, u.id, coalesce(sum(s.quantity*s.price),0) as price from  users u left outer join sales s on s.uid=u.id group by u.id, u.name order by price desc offset "
                            + rowOffset + " limit 20;";
                    userQuery2 = "insert into u_t (name, id, price) " + userQuery;
                    userQuery3 = "select * from u_t;";
                    productQuery = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) as price from products p left outer join sales s on s.pid=p.id  group by p.id, p.name order by price desc offset "
                            + colOffset + " limit 10;";
                    productQuery2 = "insert into p_t (name, id, price) " + productQuery;
                    productQuery3 = "select * from p_t;";
                    colCount = "select count(*) from products";
                } else {
                    userQuery = "select u.name, u.id, coalesce(sum(s.quantity*s.price),0) as price from  users u left outer join (select s2.uid as uid, s2.pid as pid, s2.price as price, s2.quantity as quantity from sales s2, products p where s2.pid = p.id and p.cid = "
                            + category
                            + ") s ON s.uid=u.id group by u.id, u.name order by price desc offset "
                            + rowOffset + " limit 20;";
                    userQuery2 = "insert into u_t (name, id, price) " + userQuery;
                    userQuery3 = "select * from u_t;";
                    productQuery = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) as price from products p left outer join sales s ON s.pid=p.id WHERE p.cid="
                            + category
                            + " group by p.id, p.name order by price desc offset "
                            + colOffset
                            + " limit 10;";
                    productQuery2 = "insert into p_t (name, id, price) " + productQuery;
                    productQuery3 = "select * from p_t;";
                    colCount = "select count(*) from products where cid =" + category;
                }

            }
            // Dump on console query results;
            System.out.println("==========Query Dump (" + new Date() + ")========");
            System.out.println("Temporary table 1 : " + tmpTable1);
            System.out.println("Temporary table 2 : " + tmpTable2);
            System.out.println("User Query 1 : " + userQuery);
            System.out.println("User Query 2 : " + userQuery2);
            System.out.println("User Query 3 : " + userQuery3);
            System.out.println("Product Query 1 : " + productQuery);
            System.out.println("Product Query 2 : " + productQuery2);
            System.out.println("Product Query 3 : " + productQuery3);
            System.out.println("Cells Query : " + cellsQuery);
            System.out.println("Row Count Query: " + rowCount);
            System.out.println("Column Count Query: " + colCount);

            long start = System.currentTimeMillis();
            // Create and fill temp tables
            stmt2.execute(tmpTable1);
            stmt2.execute(tmpTable2);
            stmt2.execute(userQuery2);
            stmt2.execute(productQuery2);

            // Get counts
            maxUser = getRowOrColumnCount(stmt, rowCount);
            maxProduct = getRowOrColumnCount(stmt, colCount);

            HashMap<Integer, User> users = new HashMap<Integer, User>();
            ArrayList<Integer> userPosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> userPrices = new HashMap<Integer, Integer>();
            HashMap<Integer, Product> products = new HashMap<Integer, Product>();
            ArrayList<Integer> productPosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> productPrices = new HashMap<Integer, Integer>();

            rs = stmt.executeQuery(userQuery3);
            while (rs.next()) {
                String name = rs.getString(1);
                int uid = rs.getInt(2);
                int amount = rs.getInt(3);
                userPrices.put(uid, amount);
                users.put(uid, new User(uid, name, null, 0, 0));
                userPosition.add(uid);
            }
            rs.close();

            rs = stmt.executeQuery(productQuery3);
            while (rs.next()) {
                String name = rs.getString(1);
                int pid = rs.getInt(2);
                int amount = rs.getInt(3);
                productPrices.put(pid, amount);
                products.put(pid, new Product(pid, 0, name, null, 0));
                productPosition.add(pid);
            }
            rs.close();
            int[][] prices = new int[show_num_row][show_num_col];
            rs = stmt.executeQuery(cellsQuery);
            while (rs.next()) {
                int uid = rs.getInt(1);
                int pid = rs.getInt(2);
                int amount = rs.getInt(3);
                prices[userPosition.indexOf(uid)][productPosition.indexOf(pid)] = amount;
            }
            rs.close();
            long end = System.currentTimeMillis();
            long time_taken = end - start;
            Analytics a = new Analytics(maxUser, maxProduct, 0, productPrices, userPrices, null, products, users, null,
                    productPosition, userPosition, null, prices, time_taken);
            return a;
        } catch (Exception e) {
            printError(out, e);
            return new Analytics();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    

    private static void printError(JspWriter out, Exception e) {
        try {
            out.println(HelperUtils.printError("Internal Server Error. This shouldn't happen : " + e.getMessage()));
            e.printStackTrace();
        } catch (Exception e2) {
            System.out.println(e2.getLocalizedMessage());
        }
    }

    private static int getRowOrColumnCount(Statement stmt, String query) throws SQLException {
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("There was an error processing query " + query);
    }

    public static String isSelected(String option, String selected) {
        if (option.equals(selected)) {
            return "<option value=\"" + option + "\" selected=\"selected\">" + option + "</option>";
        } else {
            return "<option value=\"" + option + "\">" + option + "</option>";
        }
    }

    public static String isSelected(int value, String option, String selected) {
        int selectedValue = -1;
        try {
            selectedValue = Integer.parseInt(selected);
        } catch (NumberFormatException e) {
        }
        if (value == selectedValue) {
            return "<option value=\"" + value + "\" selected=\"selected\">" + option + "</option>";
        } else {
            return "<option value=\"" + value + "\">" + option + "</option>";
        }
    }
    
    public static String getGlobalTimeStamp(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String date = null;
        try{
        	conn = HelperUtils.connect();
            stmt = conn.createStatement();
            String query = "SELECT * FROM last_updated_time";
            rs = stmt.executeQuery(query);
            while(rs.next()){
            	date = rs.getString(1);
            	System.out.println(rs.getString(1));
            }
            conn.close();
            stmt.close();
            return date;
        }catch(SQLException e){
        	throw new RuntimeException(e);
        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    public static String UpdatePrecomputation(String date){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        if(date !=null){
            try{
            	conn = HelperUtils.connect();
                stmt = conn.createStatement();
                //get the latest sales since last run/refresh
                String query = "SELECT u.state, s.uid, p.cid, s.pid, (s.quantity * s.price) AS sale, s.timestamp"+
                			   " FROM sales s , users u, products p"+
                			   " WHERE s.timestamp > '"+date+"'"+
                			   " AND u.id = s.uid AND p.id = s.pid;";
                rs = stmt.executeQuery(query);
                String lastTimeStamp = null;
                
                //iterate through new sales
                while(rs.next()){
                	//TODO
                	//get the last sales timestamp
                	String usersState = rs.getString(1);
                	System.out.println("stateid: "+usersState);
                	String salesUid = rs.getString(2);
                	String productsCid = rs.getString(3);
                	String salesPid = rs.getString(4);
                	String salesPrice = rs.getString(5);
                	lastTimeStamp = rs.getString(6);
                	
                	
                	//update pre_state_all
                	PreparedStatement pre = null;
                	conn.setAutoCommit(false);
                	
                	query = "UPDATE pre_states_all"+
                			" SET sum = sum+"+salesPrice+", tstamp = '"+lastTimeStamp+"'"+
                			" WHERE stateid = "+usersState+";";
                	pre = conn.prepareStatement(query);
                	pre.executeUpdate();
                	conn.commit();
                	
                	//update pre_state_cate
                	query = "UPDATE pre_state_cate"+ 
                			" SET sum=sum+"+salesPrice+", tstamp = '"+lastTimeStamp+"'"+ 
                			" WHERE stateid = "+usersState+" AND cid = "+productsCid+";";
                	pre = conn.prepareStatement(query);
                	pre.executeUpdate();
                	conn.commit();
                	
                	//update pre_products_cid
                	query = "UPDATE pre_products_cid"+
                			" SET sum=sum+"+salesPrice+", tstamp = '"+lastTimeStamp+"'"+
                			" WHERE pid = "+salesPid+" AND cid = "+productsCid+";";
                	pre = conn.prepareStatement(query);
                	pre.executeUpdate();
                	conn.commit();
                	
                	//update pre_middle
                	query = "UPDATE pre_middle"+
                			" SET sum=sum+"+salesPrice+" , tstamp = '"+lastTimeStamp+"'"+
                			" WHERE stateid = "+usersState+" AND cid = "+productsCid+";";
                	pre = conn.prepareStatement(query);
                	pre.executeUpdate();
                	conn.commit();
                	conn.setAutoCommit(true);
                	
                }
                //update global time stamp if last timestamp is not null
                if(lastTimeStamp != null){
    	            query = "UPDATE last_updated_time SET timestamp= '"+lastTimeStamp+"';";
    	            stmt.execute(query);
                }
                
                conn.close();
                stmt.close();
                return lastTimeStamp;
            }catch(SQLException e){
            	throw new RuntimeException(e);
            } catch (Exception e) {
    			e.printStackTrace();
    		}
            return null;
        }else{
            return null;
        }
    }
}
