
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
    
    int precio93;
    int precio95;
    int precio97;
    int diesel;
    int kerosene;
    
    public Surtidor(int p93, int p95, int p97, int pd, int pk){
        this.precio93 = p93;
        this.precio95 = p95;
        this.precio97 = p97;
        this.diesel = pd;
        this.kerosene = pk;
    }
    
    public static void main(String[] args) {
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        
        String ip = ipJuan;
        int port = 69;
                
        try{
            Socket socket = new Socket(ip,port);
            System.out.println("info socket: " + socket.getInetAddress());
            Surtidor surt = new Surtidor(200,240,280,100,150);
            
            SurtidorMenu menu = new SurtidorMenu(socket, surt.precio93, surt.precio93, surt.precio93, surt.diesel, surt.kerosene);
            menu.start();
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            while(true){
                String inSocket = in.readUTF();
                System.out.println("Desde el Distribuidor llega: " + inSocket);
                
            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
