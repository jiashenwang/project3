package helpers;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupHelper2 {
    public static boolean success = false;
    public static String signup(String name, Integer age, String role, String state) {

        Connection conn = null;
        Statement stmt;
        try {
            int stateId = getStateId(state);
            String SQL = "INSERT INTO users (name, role, age, state) VALUES('" + name + "','" + role + "'," + age
                    + ",'" + stateId + "');";
            try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
            	success = false;
                return ("Could not register PostgreSQL JDBC driver with the DriverManager");
            }
            stmt = conn.createStatement();
            try {
                conn.setAutoCommit(false);
                stmt.execute(SQL);
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            	success = false;
                return e.getLocalizedMessage();
            }
            conn.close();
        } catch (Exception e) {
            String output = "A problem happened while interacting with the database : \n" + e.getLocalizedMessage();
            success = false;
            return output;
        }
        success = true;
        String output = "Registered successfully! ";
        output += "Name: " + name + " Role: " + role
                + " Age: " + age + " State: " + state;
        return output;
    }

    public static int getStateId(String stateName) throws SQLException {
        // Look up the state id.
        Connection conn = null;
        String query = "SELECT id FROM states WHERE name=\'" + stateName + "\'";
        try {
            conn = HelperUtils.connect();
        } catch (Exception e) {
            System.out.println("Could not register PostgreSQL JDBC driver with the DriverManager");
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("There is no state with this name : " + stateName);
        }
    }
}