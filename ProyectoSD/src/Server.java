
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zerling
 */
public class Server
{
   public static void main(String[] args) throws InterruptedException
    {
        try
        {
            int port = 10012;
            ServerSocket sc = new ServerSocket(port);
            //Tunnel tunnel = new Tunnel();
            //Thread t = new Thread(tunnel);
            ArrayList<String> lista = new ArrayList();
            
            
        
            
            while(true)
            {
                Socket cliente = sc.accept();
                DataInputStream in = new DataInputStream(cliente.getInputStream());
                DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                System.out.println(cliente.getInetAddress());
                
                int opcion = -1;
                String operacion = "";

                while(opcion != 0)
                {
                    opcion = in.readInt();

                    if(opcion==1)
                    {
                        System.out.println("op1");
                        operacion = in.readUTF();
                        System.out.println("persona: "+operacion);
 
                        //out.writeUTF(operacion);
                        lista.add(operacion);
                        System.out.println("tamaño lista: "+lista.size());
                    
                    }
                    else if(opcion==2)
                    {
                        System.out.println("op2");
                        
                        int size = lista.size();
                        out.writeInt(size);
                        System.out.println("tamaño de la lista: "+ size);
                        for (int i = 0; i < size; i++)
                        {
                            out.writeUTF(lista.get(i));

                        }
                        System.out.println("ya mandó todas las personas");
                    }
                }

            }
        }
        catch(IOException e)
        {
            
        }
    }
}
