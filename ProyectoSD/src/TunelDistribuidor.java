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
 * @author Juan
 */
public class TunelDistribuidor implements Runnable {

    private Socket servidor;
    private Socket surtidor;

    public TunelDistribuidor() {

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
            isServidor = new DataInputStream(servidor.getInputStream());
            osServidor = new DataOutputStream(servidor.getOutputStream());
            
            isSurtidor = new DataInputStream(surtidor.getInputStream());
            osSurtidor = new DataOutputStream(surtidor.getOutputStream());
            String line;
            
            while(true){
            //while (!(line = isServidor.readUTF()).isEmpty() || !(line = isSurtidor.readUTF()).isEmpty()) {
                if((line = isServidor.readUTF()).isEmpty() ){
                    line = isSurtidor.readUTF();
                }
                if((line = isSurtidor.readUTF()).isEmpty() ){
                    line = isServidor.readUTF();
                }
                System.out.println("Sender envia : " + line);
                osServidor.writeUTF(line);
                osSurtidor.writeUTF(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
