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
    private LogSurtidor log;

    public TunelSurtidor(Socket servidor, Socket surtidor) {
        this.servidor = servidor;
        this.surtidor = surtidor;
        this.context = new ConexionBDDistribuidor();
        this.log = new LogSurtidor();

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
    
    public boolean socketInConectado(DataInputStream dis)
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
    
        public boolean socketOutConectado(DataOutputStream dis)
    {
        String men = "";
        try
        {
            dis.writeUTF(men);
            return true;
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
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
                String mensaje = "";
                if(socketInConectado(isSurtidor))
                {
                    mensaje = isSurtidor.readUTF();
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
                    context.insertUpdateBD(query);
                    
                    if(socketOutConectado(osServidor))
                    {
                        try
                        {
                            osServidor.writeUTF(query);
                        }
                        catch(IOException ex)
                        {
                            System.out.println("Error de conexion con el servidor");
                        }
                    }
                    else
                    {
                        //this.log.setFecha(fecha);
                        System.out.println("Error de conexion (escritura) con el servidor");

                    }

                }
                else
                {
                    System.out.println("Error de conexion (Lectura) con el servidor");
                }
                
             
                
                
                

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
}
