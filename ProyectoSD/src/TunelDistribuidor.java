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
    private Socket flag;

    public TunelDistribuidor() {

    }

    /*public synchronized boolean hasSenderAndListener() {
        return sender != null && listener != null;
    }*/
    
    public synchronized boolean hasServidor() {
        return servidor != null && surtidor != null && flag != null;
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
    
    void setFlag(Socket flag) {
        this.flag = flag;
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
        
        DataInputStream isFlag = null;
        DataOutputStream osFlag = null;
        try {
            isServidor = new DataInputStream(servidor.getInputStream());
            osServidor = new DataOutputStream(servidor.getOutputStream());
            
            isSurtidor = new DataInputStream(surtidor.getInputStream());
            osSurtidor = new DataOutputStream(surtidor.getOutputStream());
            
            isFlag = new DataInputStream(flag.getInputStream());
            osFlag = new DataOutputStream(flag.getOutputStream());
            
            String line;
            String flag;
            while(true){
            //while (!(line = isServidor.readUTF()).isEmpty() || !(line = isSurtidor.readUTF()).isEmpty()) {
                if((flag = isFlag.readUTF()).equals("1") ){
                    line = isServidor.readUTF();
                    
                    System.out.println("Servidor envia : " + line);
                    //osServidor.writeUTF(line);
                    osSurtidor.writeUTF(line);
                }
                else if((flag = isFlag.readUTF()).equals("2") ){
                    line = isSurtidor.readUTF();
                    System.out.println("Surtidor envia : " + line);
                    osServidor.writeUTF(line);
                    //osSurtidor.writeUTF(line);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
