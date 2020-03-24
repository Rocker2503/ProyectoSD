
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nicolas
 */
public class ConexionBDBackup {
    
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    
    static final String BDURL = "jdbc:mysql://localhost:3306/serverbackup";
    static final String BDDRV = "com.mysql.jdbc.Driver";
    static final String BD_USR = "root";
    static final String BD_PSW = "";

    public ConexionBDBackup() {
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
            System.out.println("ex: "+ex.getSQLState());
            
        }finally{
            try{
                //resultSet.close();
                statement.close();
                connection.close();
            }catch(SQLException ex){
                System.out.println("ex: "+ex.getSQLState());
            }
            
        }
    }
    
    public void selectBD(String query){
        try{
            connection = DriverManager.getConnection(BDURL, BD_USR, BD_PSW);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            
            System.out.println("Reporte global de ventas:");
            while(resultSet.next() ){
                
                String fecha = resultSet.getString("fecha");
                String tipo = resultSet.getString("tipo_combustible");
                String litros = resultSet.getString("litros");
                String total = resultSet.getString("total");
                System.out.printf("%s %s %s %s \n ", fecha, tipo, litros, total);
                /*resultSet.getString(3),
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
