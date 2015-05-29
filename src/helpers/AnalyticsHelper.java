package helpers;

import java.sql.Connection;
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

    static final int show_num_row = 20, show_num_col = 10;

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

    public static Analytics AnalysisForStates(String order, String category, int rowOffset, int colOffset, JspWriter out) {
        Connection conn = null;
        Statement stmt = null, stmt2 = null;
        ResultSet rs = null;
        // Statements that vary among user configurations
        String tmpTable1 = null, tmpTable2 = null;
        String statesQuery = null, statesQuery2 = null, statesQuery3 = null;
        String productQuery = null, productQuery2 = null, productQuery3 = null;
        String colCount = null;
        int maxState, maxProduct;

        //Statement that do not change
        String cellsQuery = "select u.state, s.pid, sum(s.quantity*s.price) from s_t st, users u, p_t p, sales s where st.id = u.state and s.uid=u.id and s.pid=p.id group by u.state, s.pid;";
        String rowCount = "select count(*) from users";

        try {
            conn = HelperUtils.connect();
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            if ("Alphabetical".equals(order)) {
                tmpTable1 = "CREATE TEMP TABLE p_t (name text, id int);";
                tmpTable2 = "CREATE TEMP TABLE s_t (name text, id int);";
                if ("0".equals(category)) {
                    statesQuery = "select st.name, st.id from  states st order by name desc offset " + rowOffset
                            + " limit 20;";
                    statesQuery2 = "insert into s_t (name, id) " + statesQuery;
                    statesQuery3 = "select s_t.name, s_t.id, coalesce(sum(s.quantity*s.price),0) from s_t LEFT OUTER JOIN (select u.state as state, s.quantity as quantity, s.price as price FROM users u JOIN sales s ON s.uid=u.id) s ON s.state=s_t.id group by s_t.id, s_t.name;";
                    productQuery = "select name, id from products order by name asc offset " + colOffset + " limit 10;";
                    productQuery2 = "insert into p_t (name, id) " + productQuery;
                    productQuery3 = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) from p_t p left outer join sales s on s.pid=p.id  group by p.id, p.name order by p.name desc;";
                    colCount = "select count(*) from products";
                } else {
                    statesQuery = "select name, id from states order by name asc offset " + rowOffset + " limit 20;";
                    statesQuery2 = "insert into s_t (name, id) " + statesQuery;
                    statesQuery3 = "select st.name, st.id, coalesce(sum(s.quantity*s.price),0) as price from s_t st LEFT OUTER JOIN ( select u.state as state, s2.price as price, s2.quantity as quantity  from users u, sales s2, products p where u.id = s2.uid and s2.pid = p.id and p.cid = "
                            + category + ") s on s.state = st.id group by st.id, st.name;";
                    productQuery = "select name, id from products where cid=" + category
                            + " order by name desc offset " + colOffset + " limit " + show_num_col;
                    productQuery2 = "insert into p_t (name, id) " + productQuery;
                    productQuery3 = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) from p_t p left outer join sales s on s.pid=p.id  group by p.id, p.name order by p.name desc;";
                    colCount = "select count(*) from products where cid =" + category;
                }
            } else {
                tmpTable1 = "CREATE TEMP TABLE p_t (name text, id int, price int);";
                tmpTable2 = "CREATE TEMP TABLE s_t (name text, id int, price int);";
                if ("0".equals(category)) {
                    statesQuery = "select st.name, st.id, sum(s.quantity*s.price) as price from states st left outer join users u on st.id = u.state left outer join sales s on s.uid=u.id group by st.id, st.name order by price desc offset "
                            + rowOffset + " limit 20;";
                    statesQuery2 = "insert into s_t (name, id, price) " + statesQuery;
                    statesQuery3 = "select * from s_t;";
                    productQuery = "select p.name, p.id, coalesce(sum(s.quantity*s.price),0) as price from products p left outer join sales s on s.pid=p.id  group by p.id, p.name order by price desc offset "
                            + colOffset + " limit 10;";
                    productQuery2 = "insert into p_t (name, id, price) " + productQuery;
                    productQuery3 = "select * from p_t;";
                    colCount = "select count(*) from products";
                } else {
                    statesQuery = "select st.name, st.id, coalesce(sum(s.quantity*s.price),0) as price from states st LEFT OUTER JOIN ( select u.state as state, s2.pid as pid, s2.price as price, s2.quantity as quantity  from users u, sales s2, products p where u.id = s2.uid and s2.pid = p.id and p.cid = "
                            + category
                            + ") s on s.state = st.id group by st.id, st.name order by price offset "
                            + rowOffset + " limit 20;";
                    statesQuery2 = "insert into s_t (name, id, price) " + statesQuery;
                    statesQuery3 = "select * from s_t;";
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
            System.out.println("User Query 1 : " + statesQuery);
            System.out.println("User Query 2 : " + statesQuery2);
            System.out.println("User Query 3 : " + statesQuery3);
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
            stmt2.execute(statesQuery2);
            stmt2.execute(productQuery2);

            // Get counts
            maxState = 50;
            maxProduct = getRowOrColumnCount(stmt, colCount);

            HashMap<Integer, State> states = new HashMap<Integer, State>();
            ArrayList<Integer> statePosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> statePrices = new HashMap<Integer, Integer>();
            HashMap<Integer, Product> products = new HashMap<Integer, Product>();
            ArrayList<Integer> productPosition = new ArrayList<Integer>();
            HashMap<Integer, Integer> productPrices = new HashMap<Integer, Integer>();

            rs = stmt.executeQuery(statesQuery3);
            while (rs.next()) {
                String name = rs.getString(1);
                int sid = rs.getInt(2);
                int amount = rs.getInt(3);
                states.put(sid, new State(sid, name));
                statePosition.add(sid);
                statePrices.put(sid, amount);
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
}
