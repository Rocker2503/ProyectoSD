
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Nicolas
 */
public class Surtidor {
    
    public static void main(String[] args) {
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        
        String ip = "25.49.55.58";
        int port = 69;
        
        try{
            Socket socket = new Socket(ipJuan,port);
            Socket flag = new Socket(ipJuan, port);
            
            System.out.println("info socket: " + socket.getInetAddress());
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            DataInputStream inFlag = new DataInputStream(flag.getInputStream());
            DataOutputStream outFlag = new DataOutputStream(flag.getOutputStream());
            
            String line;
            
            while(true){
                String inSocket = in.readUTF();
                System.out.println("Desde el Distribuidor llega: " + inSocket);
                
                String con = "Conectado";
                out.writeUTF(con);
                
                Scanner scan = new Scanner(System.in);
                String mensaje = scan.nextLine();
                
                outFlag.writeUTF("2");
                out.writeUTF(mensaje);

            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
