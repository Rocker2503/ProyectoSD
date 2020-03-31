
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nicolas
 */
public class ServidorMenu extends Thread{
    private ArrayList<Socket> distribuidores;
    private ServerSocket servidor;
    private Scanner scanner;
    private ConexionBDServidor context;
    private ConexionBDBackup backupContext;
    ArrayList<String> sincronia;
    
    public ServidorMenu(ServerSocket s){
        this.servidor = s;
        this.distribuidores = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.context = new ConexionBDServidor();
        this.backupContext = new ConexionBDBackup();
        this.sincronia = new ArrayList<>();
    }

    @Override
    public void run() {
        int seleccion;
        while(true){
            System.out.println("1. Para obtener los reportes");
            System.out.println("2. Para actualizar precios");
            System.out.print("Ingresar opcion: ");
            
            seleccion = scanner.nextInt();
            switch(seleccion){
                case 1: 
                    generarReporte();
                    System.out.println("");
                    break;
                case 2:
                    actualizarPrecios(this.distribuidores);
                    System.out.println("");
                    break;
            }
        }
    }
    
    public void generarReporte(){
        String query = "Select * FROM venta_general";
        this.context = new ConexionBDServidor();
        
        try {
            context.selectBD(query);
        } catch (Exception e) {
            System.out.println("Conexión principal perdida, usando backup");
            backupContext.selectBD(query);
        }
    }
    
    public void actualizarPrecios(ArrayList<Socket> dist){
        int opcion;
        System.out.println("Seleccione combustible que quiere actualizar: ");
        System.out.println("1. Gasolina 93");
        System.out.println("2. Gasolina 95");
        System.out.println("3. Gasolina 97");
        System.out.println("4. Diesel");
        System.out.println("5. Kerosene");
        opcion = scanner.nextInt();
        
        System.out.println("Ingrese el precio");
        int precio = scanner.nextInt();
        String query;
        
        switch(opcion){
            case 1:
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Gas93'";
                System.out.println("Query: "+query);
                
                this.context = new ConexionBDServidor();                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                
                actualizarTodo(query,dist);
                break;
            case 2: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Gas95'";
                this.context = new ConexionBDServidor();                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                actualizarTodo(query,dist);
                break;
            case 3: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Gas97'";
                this.context = new ConexionBDServidor();                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                actualizarTodo(query,dist);
                break;
            case 4: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Diesel'";
                this.context = new ConexionBDServidor();                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                actualizarTodo(query,dist);
                break;
            case 5: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Kerosene'";
                this.context = new ConexionBDServidor();                
                try {
                    sincronizarBD();
                    context.insertUpdateBD(query);
                    backupContext.insertUpdateBD(query);

                } catch (Exception e) {
                    System.out.println("Conexión principal perdida, usando backup");
                    this.sincronia.add(query);
                    backupContext.insertUpdateBD(query);
                }
                actualizarTodo(query,dist);
                break;
        }
    }
    
    public boolean agregarSocket(Socket s){
        System.out.println("Se agrega conexión a distribuidor");
        return distribuidores.add(s);
    }
    
    public void actualizarTodo(String msj , ArrayList<Socket> dist){
        try{
            for(Socket s : dist){
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                System.out.println("Actualizacion : " + msj);
                dos.writeUTF(msj);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void sincronizarBD() {
        System.out.println("Sincronizar BD principal");
        //System.out.println("leng: " + this.sincronia.size());
        if(!this.sincronia.isEmpty()){
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
