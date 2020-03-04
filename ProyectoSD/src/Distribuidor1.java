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
public class Distribuidor1 {

    public static void main(String[] args) throws UnknownHostException, IOException {
        int port = 10013;
        String ip = "127.0.0.1";
        String ip2 = "127.0.0.2";
        InetAddress addr = InetAddress.getByName(ip2);

// and now you can pass it to your socket-constructor
        try (ServerSocket listener = new ServerSocket(port, 0, addr)) {
        
            Socket ss = new Socket(ip, port);
            DataInputStream inSocket = new DataInputStream(ss.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(ss.getOutputStream());

            System.out.println("Servidor iniciado y escuchando en el puerto " + port);
            Tunel tunel = new Tunel();
            //t2
            //t3
            Thread t = new Thread(tunel);
            //th2
            //th3
            while (true) {
                System.out.println("1");
                //outSocket
                Socket sc = listener.accept();
                System.out.println("2");

                System.out.println("Cliente " + sc.getRemoteSocketAddress() + " se ha conectado");
                //mandar 
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                Scanner scanner = new Scanner(System.in);
                int modo2 = scanner.nextInt();
                
                outSocket.writeInt(modo2);
                out.writeInt(modo2);
                //recibir modo en lab
                int modo = in.readInt();
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
