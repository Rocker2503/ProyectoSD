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
        
        ArrayList<Tunel> listeners = new ArrayList<>();
        Tunel tunel = null;
        ServerSocket servidor = null;
        Socket sc = null;
        
        ConexionBDServidor context = new ConexionBDServidor();
        InetAddress addr = InetAddress.getByName(ipJuan); 

        try{
            servidor = new ServerSocket(port,0,addr);
            System.out.println("El servidor esta listo para recibir clientes");
            ServidorMenu menu = new ServidorMenu(servidor);
            
            sc = servidor.accept();
            
            tunel = new Tunel(sc);
            tunel.start();
            
            listeners.add(tunel);
            menu.agregarSocket(sc);
            menu.start();
            
            while(!servidor.isClosed()){
                sc = servidor.accept();
                System.out.println("Cliente se conecta al puerto");
                
                tunel = new Tunel(sc);
                
                tunel.start();
                
                menu.agregarSocket(sc);
            }
            
            sc.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        
    }
}
