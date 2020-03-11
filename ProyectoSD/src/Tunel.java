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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gsanh
 */
public class Tunel extends Thread {

    private Socket listener;
    ConexionBDServidor context;

    public Tunel(Socket s) {
        listener = s;
        this.context = new ConexionBDServidor();
    }

    /*public synchronized boolean hasSenderAndListener() {
        return sender != null && listener != null;
    }*/
    
    public synchronized boolean hasServidor() {
        return listener != null;
    }

    public synchronized void setServidor(Socket servidor) {
        this.listener = servidor;
    }

    /*public synchronized void setListener(Socket listener) {
        this.listener = listener;
    }*/

    @Override
    public void run() {
        String msj;
        try{
            DataInputStream dis = new DataInputStream(listener.getInputStream());
            while(!listener.isClosed()){
                msj = dis.readUTF();
                System.out.println("Desde el Distribuidor: " + msj);
                context.insertUpdateBD(msj);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

}
