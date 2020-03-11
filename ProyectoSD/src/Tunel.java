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
public class Tunel implements Runnable {

    private Socket servidor;
    private Socket flag;

    public Tunel() {

    }

    /*public synchronized boolean hasSenderAndListener() {
        return sender != null && listener != null;
    }*/
    
    public synchronized boolean hasServidor() {
        return servidor != null && flag != null;
    }

    public synchronized void setServidor(Socket servidor) {
        this.servidor = servidor;
    }
    
    void setFlag(Socket flag) {
        this.flag = flag;
    }

    /*public synchronized void setListener(Socket listener) {
        this.listener = listener;
    }*/

    @Override
    public void run() {
        DataInputStream is = null;
        DataOutputStream os = null;
        DataInputStream isFlag = null;
        DataOutputStream osFlag = null;
        try {
            is = new DataInputStream(servidor.getInputStream());
            os = new DataOutputStream(servidor.getOutputStream());
            
            isFlag = new DataInputStream(flag.getInputStream());
            osFlag = new DataOutputStream(flag.getOutputStream());
            
            String line;

            while ((line = is.readUTF()).compareTo("close") != 0) {
                System.out.println("Sender envia : " + line);
                //indica que lea del servidor
                osFlag.writeUTF("1");
                os.writeUTF(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
