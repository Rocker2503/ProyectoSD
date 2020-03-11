
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Nicolas
 */
public class Surtidor {
    
    int precio93 = 600;
    int precio95 = 650;
    int precio97 = 700;
    int diesel = 450;
    int kerosene = 540;

    
    public static void main(String[] args) {
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        ArrayList<String> ventas = new ArrayList<String>();
        
        String ip = ipNico;
        int port = 69;
                
        try{
            Socket socket = new Socket(ip,port);
            System.out.println("info socket: " + socket.getInetAddress());
            
            SurtidorMenu menu = new SurtidorMenu(socket);
            menu.start();
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            while(true){
                String inSocket = in.readUTF();
                System.out.println("Desde el Distribuidor llega: " + inSocket);
                
                String con = "Conectado";
                out.writeUTF(con);
                
                //Scanner scan = new Scanner(System.in);
                
                
                
                       
            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
