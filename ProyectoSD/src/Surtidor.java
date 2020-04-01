import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
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
        
        String ip = ipNico;
        int port = 69;
                
        try{
            Socket socket = new Socket(ip,port);
            socket.setSoTimeout(0);
            System.out.println("info socket: " + socket.getInetAddress());
            Surtidor surt = new Surtidor(200,240,280,100,150);
            
            SurtidorMenu menu = new SurtidorMenu(socket, surt.precio93, surt.precio93, surt.precio93, surt.diesel, surt.kerosene);
            menu.start();
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            while(true){
               
                try{
                    while(!socket.isClosed())
                    {
                        String inSocket = in.readUTF();
                        System.out.println("Desde el Distribuidor llega: " + inSocket);
                        String[] venta = inSocket.split("precio =");
                        //System.out.println("venta: "+venta[0]);
                        //System.out.println("venta: "+venta[1]);

                        String[] precio = venta[1].split("WHERE");
                        //System.out.println("precio: "+precio[0]);
                        int pre = Integer.parseInt(precio[0].replace(" ", ""));
                        //System.out.println("precio: "+precio[1]);
                        String[] combustible = precio[1].split("tipo_combustible = ");
                        System.out.println("Tipo combustible: "+combustible[1]);
                        String comb = combustible[1];
                        if(comb.equals("'Gas93'"))
                        {
                            menu.setPrecio93(pre);
                            System.out.println("precio 93: "+menu.getPrecio93());

                        }
                        if (comb.equals("'Gas95'"))
                        {
                           menu.setPrecio95(pre);
                            System.out.println("precio 95: "+menu.getPrecio95());
                        }
                        if (comb.equals("'Gas97'"))
                        {
                           menu.setPrecio97(pre);
                            System.out.println("precio 97: "+menu.getPrecio97());
                        }
                        if (comb.equals("'Kerosene'"))
                        {
                           menu.setKerosene(pre);
                            System.out.println("precio Ker: "+menu.getKerosene());
                        }
                        if (comb.equals("'Diesel'"))
                        {
                           menu.setDiesel(pre);
                            System.out.println("precio Dis: "+menu.getDiesel());
                        }
                    }
                }
                catch(SocketException ex){
                    ex.printStackTrace();
                }             
            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
