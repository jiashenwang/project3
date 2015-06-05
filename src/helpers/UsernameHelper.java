package helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// check if useranme is already exists
public class UsernameHelper {
	public static boolean findUser(String username){
		Connection conn = null;
        Statement stmt = null;
        String name = username;
        ResultSet rs = null;
        boolean result = false;
        
        if(name == null){
        	name = "";
        }
        
        String query = "SELECT * FROM users WHERE name='"+name+"';";
        
        try {
			conn = HelperUtils.connect();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			rs = stmt.executeQuery(query);
        	conn.commit();
        	// check if any result returned id yes return true, else false
        	while(rs.next()){
        		result = true;
        	}
        	
			stmt.close();
            conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			return result;
		}
	}
}
