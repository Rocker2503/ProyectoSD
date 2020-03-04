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
        String ip = "127.0.0.1";
        InetAddress addr = InetAddress.getByName(ip); 

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
                System.out.println("2");

                System.out.println("Cliente " + sc.getRemoteSocketAddress() + " se ha conectado");
                //mandar 
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                //Scanner scanner = new Scanner(System.in);
                //int modo2 = scanner.nextInt();
                
                //out.writeInt(modo2);
                //recibir modo en lab
                String modo = in.readUTF();
                System.out.println("El modo recibido es " + modo);
                tunel.setServidor(sc);
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
