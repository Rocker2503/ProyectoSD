/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan
 */
public class TunelSurtidor extends Thread {

    private Socket servidor;
    private Socket surtidor;

    public TunelSurtidor(Socket server, Socket surtidor) {
        this.surtidor = surtidor;

    }

    /*public synchronized boolean hasSenderAndListener() {
        return sender != null && listener != null;
    }*/
    
    public synchronized boolean hasServidor() {
        return servidor != null && surtidor != null;
    }

    public synchronized void setServidor(Socket servidor) {
        this.servidor = servidor;
    }
    
    public synchronized boolean hasSurtidor() {
        return servidor != null;
    }

    public synchronized void setSurtidor(Socket surtidor) {
        this.surtidor = surtidor;
    }

    /*public synchronized void setListener(Socket listener) {
        this.listener = listener;
    }*/

    @Override
    public void run() {
        DataInputStream isServidor = null;
        DataOutputStream osServidor = null;
        
        DataInputStream isSurtidor = null;
        DataOutputStream osSurtidor = null;
        try {
            //isServidor = new DataInputStream(servidor.getInputStream());
            //osServidor = new DataOutputStream(servidor.getOutputStream());
            
            isSurtidor = new DataInputStream(surtidor.getInputStream());
            osSurtidor = new DataOutputStream(surtidor.getOutputStream());
            
            while(hasServidor())
            {
                String mensaje = isSurtidor.readUTF();
            }
            
            /*String line;
            
            Scanner scanner = new Scanner(System.in);
            int modo;
            do {
                System.out.println("Ingrese en que modo quiere ingresar");
                System.out.println("1 - Modo Servidor");
                System.out.println("2 - Modo Surtidor");
                modo = scanner.nextInt();
            } while (modo != 1 && modo != 2);

            while(true){
                switch (modo) {
                    case 1:
                        while ((line = isServidor.readUTF()).compareTo("0") != 0) {
                            System.out.println("El programa esta escuchando al servidor: " + line);
                        }
                        modo = 2;
                        break;
                    case 2:
                        while ((line = isSurtidor.readUTF()).compareTo("0") != 0) {
                            System.out.println("El programa esta escuchando al surtidor: " + line);
                            osServidor.writeUTF(line);
                        }
                        modo = 1;
                        break;
                }
            }*/
        } catch (IOException ex) {
            Logger.getLogger(TunelSurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
