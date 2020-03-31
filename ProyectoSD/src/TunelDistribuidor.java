/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan
 */
public class TunelDistribuidor extends Thread {
    
    String ipNico = "25.48.255.90";
    String ipJuan = "25.49.16.34";
    String ipAlvaro = "25.49.55.58";
    int port = 49775;
    
    String ip = ipJuan;

    private Socket servidor;
    //private Socket surtidor;
    
    ArrayList<TunelSurtidor> escuchaSurtidores;
    ArrayList<Socket> escuchaDistribuidor;
    ConexionBDDistribuidor context;
    ConexionBDBackupDistribuidor backupContext;

    public TunelDistribuidor() throws IOException {
        
        escuchaSurtidores = new ArrayList<>();
        escuchaDistribuidor = new ArrayList<>();
        
        servidor  = new Socket(ip, port);
        this.context = new ConexionBDDistribuidor();
        this.backupContext = new ConexionBDBackupDistribuidor();

    }

    /*public synchronized boolean hasSenderAndListener() {
        return sender != null && listener != null;
    }*/
    
    public synchronized boolean hasServidor() {
        return servidor != null;
    }

    public synchronized void setServidor(Socket servidor) {
        this.servidor = servidor;
    }
    
    /*public synchronized boolean hasSurtidor() {
        return servidor != null;
    }

    public synchronized void setSurtidor(Socket surtidor) {
        this.surtidor = surtidor;
    }*/

    /*public synchronized void setListener(Socket listener) {
        this.listener = listener;
    }*/

    @Override
    public void run() {
        DataInputStream isServidor = null;
        DataOutputStream osServidor = null;
        ObjectInputStream oii = null;

        try {
            isServidor = new DataInputStream(servidor.getInputStream());
            osServidor = new DataOutputStream(servidor.getOutputStream());
            oii = new ObjectInputStream(servidor.getInputStream());

            while(!servidor.isClosed())
            {
                //Recibe actualizacion precio
                String mensaje = isServidor.readUTF();
                System.out.println("Mensaje leido: "+mensaje);
                
                LogCaida log = (LogCaida)oii.readObject();
                System.out.println("Surtidor caido: " + log.getFechaI().toString());
                System.out.println("Tiempo caido: " + log.tiempoCaidoSeg());
                
                //Actualizar precio BD
                context.insertUpdateBD(mensaje);
                backupContext.insertUpdateBD(mensaje);
                
                //Mandar precio al surtidor
                osServidor.writeUTF(mensaje);
                setPrecioSurtidores(mensaje);
            }
            /*
            String line;
            
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
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(TunelDistribuidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public void enlazarSurtidor(Socket nuevoSurtidor) throws IOException
        {
            if(nuevoSurtidor == null)
                return;

            TunelSurtidor tunel = new TunelSurtidor(this.servidor, nuevoSurtidor);
           //tunel.setSurtidor(nuevoSurtidor);
            tunel.start();
            escuchaSurtidores.add(tunel);
            this.escuchaDistribuidor.add(nuevoSurtidor);
        }
    
        private void setPrecioSurtidores(String mensaje) throws IOException
        {
            DataOutputStream output = null;
            for (int i = 0; i < this.escuchaDistribuidor.size(); i++)
            {
                output = new DataOutputStream(escuchaDistribuidor.get(i).getOutputStream());
                output.writeUTF(mensaje);
                
            }
    }
}
