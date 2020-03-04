
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
        
        String ip = "127.0.0.2";
        int port = 69;
        
        try{
            Socket socket = new Socket(ip,port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            while(true){
                Scanner s = new Scanner(System.in);
                String str = s.nextLine();

                out.writeUTF(str);
                System.out.println("Enviado: " + str);
            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
