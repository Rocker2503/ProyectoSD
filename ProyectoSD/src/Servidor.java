/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author gsanh
 */
public class Servidor {

    public static void main(String[] args) throws UnknownHostException {
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        int port = 49775;
        
        String ip = ipAlvaro;
        
        ArrayList<Tunel> listeners = new ArrayList<>();
        Tunel tunel = null;
        ServerSocket servidor = null;
        Socket distribuidor = null;
        
        ConexionBDServidor context = new ConexionBDServidor();
        InetAddress addr = InetAddress.getByName(ip); 

        try{

            servidor = new ServerSocket(port,0,addr);
            ServidorMenu menu = new ServidorMenu(servidor);
            
            distribuidor = servidor.accept();
            System.out.println("El servidor esta listo para recibir distribuidores");
            
            tunel = new Tunel(distribuidor);
            tunel.setServidor(distribuidor);
            tunel.start();
            
            listeners.add(tunel);
            menu.agregarSocket(distribuidor);
            menu.start();
            
            while(tunel.hasServidor()){
                //Se espera un surtidor
                distribuidor = servidor.accept();
                System.out.println("Cliente se conecta al puerto");
                
                //Tunel del distribuidor
                tunel = new Tunel(distribuidor);
                
                tunel.start();
                
                menu.agregarSocket(distribuidor);
            }
            
            distribuidor.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        
    }
}
