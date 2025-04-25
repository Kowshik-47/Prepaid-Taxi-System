package PrepaidTaxi;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DataBase {
    Connection conn;
    Statement st;
    
    DataBase() throws ClassNotFoundException{
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prepaid taxi","root","");
            st = conn.createStatement();
        } catch (SQLException se){
            System.out.println(se);
        }
    }
    
    public void closeConnection(){
        try{
            conn.close();
        } catch (SQLException e){
            System.out.println(e);
        }
    }
    
    public int insertNew(String query){
        try{
            return st.executeUpdate(query);
        } catch (SQLException e){
            System.out.println(e);
        }
        return 0;
    }
    
    public void deleteData(String query){
        try{
            st.executeUpdate(query);
        } catch (SQLException e){
            System.out.println(e);
        }
    }
    
    public ResultSet getData(String query){
        try{
            return st.executeQuery(query);
        } catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    public int updateData(String query){
        try{
            st.executeUpdate(query);
            return 1;
        } catch (SQLException e){
            System.out.println(e);
            return 0;
        }
    }  
}