// package nonogrampkg;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;  
import java.sql.SQLException;  

public class DB {
    public DB(){

    }

    public static void create_db(String path){
        Class.forName("oracle.jdbc.driver.OracleDriver");

        Connection con = null;
        try{
            String user = "admin-main";
            String password = "admin-main-password";
            con = DriverManager.getConnection(path, user, password);
            if(con != null){
                System.out.println("Successfully Created");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
    }

    public static void main(String[] args){

        create_db("nonograms.db");
    }
}
