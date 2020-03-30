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
    
    public String escucharSocket(DataInputStream dis)
    {
        String men = "";
        try
        {
            men = dis.readUTF();
        }
        catch(IOException ex)
        {
            System.out.println("Conexion perdida con el Distribuidor");
        }
        return men;
    }    
    
    public boolean socketConectado(DataInputStream dis)
    {
        String men = "";
        try
        {
            men = dis.readUTF();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        if(men.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    

    /*public synchronized void setListener(Socket listener) {
        this.listener = listener;
    }*/

    @Override
    public void run() {
        String msj;
        try{
            DataInputStream dis = new DataInputStream(listener.getInputStream());
            while(this.socketConectado(dis)){
                
                msj = this.escucharSocket(dis);
                if(!msj.equals(""))
                {
                    String query = msj.replace("venta", "venta_general");
                    System.out.println("Desde el Distribuidor: " + query);
                }
                else
                {
                    System.out.println("conexion perdida");
                }
                /*try
                {
                    msj = dis.readUTF();
                    String query = msj.replace("venta", "venta_general");
                    System.out.println("Desde el Distribuidor: " + query);
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);
                    System.out.println("readUTF: " + msj);

                }
                catch(IOException ex)
                {
                    System.out.println("Exception");
                    ex.printStackTrace();
                }*/
                


            }
        }
        catch(IOException ex){
            System.out.println("Conexion perdida con el Distribuidor");
            //ex.printStackTrace();
        }

    }

}
