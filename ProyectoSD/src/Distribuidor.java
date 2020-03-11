/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gsanh
 */
public class Distribuidor {

    public static void main(String[] args) throws UnknownHostException, IOException {
        int port = 49775;
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        
        int listenPort = 69;
        
        ArrayList<Socket> surtidores = new ArrayList<Socket>();
        ServerSocket serverDistribuidor = null;
        Socket surtidor = null;
        TunelDistribuidor tunelDistribuidor;
        
        ConexionBDDistribuidor context = new ConexionBDDistribuidor();
        InetAddress addr = InetAddress.getByName(ipJuan);
        //context.insertUpdateBD("INSERT INTO precio(id,precio) VALUES('1','1000')");

// and now you can pass it to your socket-constructor
        try{
            serverDistribuidor = new ServerSocket(listenPort, 0, addr);
            
            //DataInputStream inServer = serverDistribuidor.getInputStream();
            //DataOutputStream outServer = serverDistribuidor.getOutputStream();

            //Aqui incluir print de inicio Distribuidor
            tunelDistribuidor = new TunelDistribuidor();
            tunelDistribuidor.start();
            
            while(!serverDistribuidor.isClosed()){
                System.out.println("Conectado"); 
                surtidor = serverDistribuidor.accept();
                System.out.println("Se ha conectado: " + surtidor.getLocalSocketAddress());
                
                //tunelDistribuidor.setServidor(surtidor);  
                
                tunelDistribuidor.enlazarSurtidor(surtidor);
                surtidores.add(surtidor);
                
                //Configuracion inicial
                DataInputStream in = new DataInputStream(surtidor.getInputStream());
                DataOutputStream out = new DataOutputStream(surtidor.getOutputStream());
                
                String serverIn = in.readUTF();
                System.out.println("Servidor envio: " + serverIn);
                
                                
                out.writeUTF(serverIn);                
                System.out.println("Se ha enviado al surtidor: " + serverIn);
               
                
                String inSurtidor = in.readUTF();
                
                out.writeUTF(inSurtidor);               
                System.out.println("Enviado del surtidor: " + inSurtidor);

            }
            //t.join();
            surtidor.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
