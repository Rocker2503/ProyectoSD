/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileReader;
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
public class Servidor {

    public static void main(String[] args) throws UnknownHostException {
        int port = 49775;
        String ip = "25.48.255.90";
        //ConexionBDServidor context = new ConexionBDServidor();
        //InetAddress ad = InetAddress.getLocalHost();
        InetAddress addr = InetAddress.getByName(ip); 
        //context.insertarDistribuidor("INSERT INTO distribuidores(id,nombre) VALUES('1','San Javier')");

// and now you can pass it to your socket-constructor
        try {
            ServerSocket listener = new ServerSocket(port, 0, addr);
            System.out.println("puerto: " + listener.getLocalSocketAddress());
            System.out.println("Servidor iniciado y escuchando en el puerto " + port);
            Tunel tunel = new Tunel();
            //t2
            //t3
            Thread t = new Thread(tunel);
            //th2
            //th3
            while (true) {
                System.out.println("1");
                Socket sc = listener.accept();
                tunel.setServidor(sc);
                
                System.out.println("2");

                System.out.println("Cliente " + sc.getRemoteSocketAddress() + " se ha conectado");
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                
                
                //Configuración inicial
                
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                
                //Manda mensaje al distribuidor
                out.writeUTF(input);
                System.out.println("Escribe al distribuidor: " + input);
                
                //Espera respuesta del distribuidor
                String modo = in.readUTF();
                System.out.println("El mensaje recibido es: " + modo);
                
                
                System.out.println("5");

                if (tunel.hasServidor()) {
                    System.out.println("Thread start");
                    t.start();
                    break;
                }
                System.out.println("6");

            }
            t.join();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
