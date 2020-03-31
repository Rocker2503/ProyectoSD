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
    ConexionBDBackup backupContext;

    public Tunel(Socket s) {
        listener = s;
        this.context = new ConexionBDServidor();
        this.backupContext = new ConexionBDBackup();
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
                String query = msj.replace("venta", "venta_general");
                System.out.println("Desde el Distribuidor: " + query);
                try {
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);
                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    backupContext.insertUpdateBD(query);
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

}
