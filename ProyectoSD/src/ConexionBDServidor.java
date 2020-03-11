
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Nicolas
 */
public class ConexionBDServidor {
    
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    
    static final String BDURL = "jdbc:mysql://localhost:3306/serversdp2";
    static final String BDDRV = "com.mysql.jdbc.Driver";
    static final String BD_USR = "root";
    static final String BD_PSW = "";
    
    public ConexionBDServidor(){
        connection = null;
        statement = null;
        resultSet = null;
    }
    
    public void insertUpdateBD(String query){
        try{
            connection = DriverManager.getConnection(BDURL, BD_USR, BD_PSW);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch(SQLException ex){
        }finally{
            try{
                //resultSet.close();
                statement.close();
                connection.close();
            }catch(SQLException ex){
                
            }
            
        }
    }
    
    public void selectBD(String query){
        try{
            connection = DriverManager.getConnection(BDURL, BD_USR, BD_PSW);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            
            while(resultSet.next() ){
            System.out.printf("%s\t%s\t%s\t%f\n",
                resultSet.getString(1));
                /*resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getFloat(4));*/
            }
        }
        catch(SQLException ex){
        }finally{
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }catch(SQLException ex){
                
            }
            
        }
    }
    
}
