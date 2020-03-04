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
        String ip = "25.49.16.34";
        String ip2 = "25.48.255.90";
        int listenPort = 69;
        InetAddress addr = InetAddress.getByName(ip2);

// and now you can pass it to your socket-constructor
        try{
            
            Socket ss = new Socket(ip, port);
            ServerSocket listener = new ServerSocket(listenPort, 0, addr);
            DataInputStream inServer = new DataInputStream(ss.getInputStream());
            DataOutputStream outServer = new DataOutputStream(ss.getOutputStream());

            //Aqui incluir print de inicio Distribuidor
            Tunel tunel = new Tunel();
            //tunel2
            //tunel3
            Thread t = new Thread(tunel);
            //thread2
            //thread3
            while (true) {
                System.out.println("Conectando..."); 
                              
                String serverIn = inServer.readUTF();
                System.out.println("Servidor envio: " + serverIn);
                
                
                Socket sc = listener.accept();
                tunel.setServidor(sc);
                
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                                
                out.writeUTF(serverIn);                
                System.out.println("Se ha enviado al surtidor: " + serverIn);
               
                
                String inSurtidor = in.readUTF();
                
                outServer.writeUTF(inSurtidor);               
                System.out.println("Enviado al surtidor: " + inSurtidor);
                                               
                //recibir modo en lab

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
