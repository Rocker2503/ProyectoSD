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
import java.net.SocketException;
import java.util.ArrayList;
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
    ArrayList<String> sincronia;

    public Tunel(Socket s) throws SocketException {
        listener = s;
        listener.setSoTimeout(0);
        this.context = new ConexionBDServidor();
        this.backupContext = new ConexionBDBackup();
        this.sincronia = new ArrayList<>();
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
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);
                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

    private void sincronizarBD() {
        if(!this.sincronia.isEmpty()){
            System.out.println("Sincronizar BD principal");
            
            String local;
            ArrayList<String> sincronizados = new ArrayList<>();
            
            for (int i = 0; i < this.sincronia.size(); i++) {
                local = this.sincronia.get(i);
                System.out.println("local: " + local);
                try {
                 this.context.insertUpdateBD(local);
                 sincronizados.add(local);
                } catch (Exception e) {
                }
            }
            this.sincronia.removeAll(sincronizados);
        }
    }

}
