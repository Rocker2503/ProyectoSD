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
import java.time.LocalDate;
import java.util.ArrayList;
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
    ConexionBDDistribuidor context;
    ConexionBDBackupDistribuidor backupContext;
    ArrayList<String> sincronia;

    public TunelSurtidor(Socket servidor, Socket surtidor) {
        this.servidor = servidor;
        this.surtidor = surtidor;
        this.context = new ConexionBDDistribuidor();
        this.backupContext = new ConexionBDBackupDistribuidor();
        this.sincronia = new ArrayList<>();

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
            
            while((!servidor.isClosed()) && (!surtidor.isClosed()) )
            {
                //Recibe venta
                String mensaje = isSurtidor.readUTF();
                
                String[] venta = mensaje.split(" ");
                
                String tipo = venta[0];
                String litros = venta[1];
                String precio = venta[2];
                String hoy = venta[3];
                //System.out.println("Variables: " + tipo + " " + litros + " " + precio + " " + hoy);
                
                int precioInt = Integer.parseInt(precio);
                int litrosInt = Integer.parseInt(litros);
                
                
                int total = litrosInt*precioInt;
                
                System.out.println("Venta: " + tipo + ", " + litros + ", " + precio + ", " + hoy);
                
                //Actualizar venta BD  

                String query = String.format("INSERT INTO venta(fecha,tipo_combustible,litros, total) VALUES('%s', '%s', '%s', '%d')", hoy, tipo, litros, total);
                //System.out.println("Query: " + query);
                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);
                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                
                osServidor.writeUTF(query);
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
