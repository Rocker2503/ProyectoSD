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
        ConexionBDDistribuidor context = new ConexionBDDistribuidor();
        InetAddress addr = InetAddress.getByName(ipJuan);
        //context.insertUpdateBD("INSERT INTO precio(id,precio) VALUES('1','1000')");

// and now you can pass it to your socket-constructor
        try{
            
            Socket ss = new Socket(ipAlvaro, port);
            ServerSocket listener = new ServerSocket(listenPort, 0, addr);
            DataInputStream inServer = new DataInputStream(ss.getInputStream());
            DataOutputStream outServer = new DataOutputStream(ss.getOutputStream());

            //Aqui incluir print de inicio Distribuidor
            TunelDistribuidor tunel = new TunelDistribuidor();
            //tunel2
            //tunel3
            Thread t = new Thread(tunel);
            //thread2
            //thread3
            while (true) {
                System.out.println("Conectado"); 
                Socket sc = listener.accept();
                tunel.setServidor(sc);   
                tunel.setSurtidor(ss);
                System.out.println("Se ha conectado: " + listener.getLocalSocketAddress());
                
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                
                String serverIn = inServer.readUTF();
                System.out.println("Servidor envio: " + serverIn);
                
                                
                out.writeUTF(serverIn);                
                System.out.println("Se ha enviado al surtidor: " + serverIn);
               
                
                String inSurtidor = in.readUTF();
                
                outServer.writeUTF(inSurtidor);               
                System.out.println("Enviado del surtidor: " + inSurtidor);
                                               
                
                //probando escucha modo servidor o surtidor
                /*int flag = 0;
                while(flag != 1){
                    if(!inServer.readUTF().isEmpty() || !in.readUTF().isEmpty() )
                    {
                        System.out.println("escucha algo");
                        flag = 1;
                    }
                }
                flag = 0;*/

                if (tunel.hasServidor()) {
                    System.out.println("Thread start");
                    t.start();
                    break;
                }
                //System.out.println("6");

            }
            t.join();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
