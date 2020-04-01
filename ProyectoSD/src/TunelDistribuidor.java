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
    ArrayList<String> sincronia;

    public TunelDistribuidor() throws IOException {
        
        escuchaSurtidores = new ArrayList<>();
        escuchaDistribuidor = new ArrayList<>();
        
        servidor  = new Socket(ip, port);
        servidor.setSoTimeout(0);
        this.context = new ConexionBDDistribuidor();
        this.backupContext = new ConexionBDBackupDistribuidor();
        this.sincronia = new ArrayList<>();

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

        try {
            isServidor = new DataInputStream(servidor.getInputStream());
            osServidor = new DataOutputStream(servidor.getOutputStream());

            while(!servidor.isClosed())
            {
                //Recibe actualizacion precio
                String mensaje = isServidor.readUTF();
                System.out.println("Mensaje leido: "+mensaje);
                
                //Actualizar precio BD
                this.context = new ConexionBDDistribuidor();
           
                try {
                    sincronizarBD();
                    context.insertUpdateBD(mensaje);
                    backupContext.insertUpdateBD(mensaje);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(mensaje);
                    backupContext.insertUpdateBD(mensaje);
                }
                
                //context.insertUpdateBD(mensaje);
                
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
        }
    }
    
        public void enlazarSurtidor(Socket nuevoSurtidor) throws IOException
        {
            if(nuevoSurtidor == null)
                return;
            
            
            nuevoSurtidor.setSoTimeout(0);
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
