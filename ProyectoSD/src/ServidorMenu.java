
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
    
    public ServidorMenu(ServerSocket s){
        this.servidor = s;
        this.distribuidores = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.context = new ConexionBDServidor();
        this.backupContext = new ConexionBDBackup();
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
                    break;
                case 2:
                    actualizarPrecios(this.distribuidores);
                    break;
            }
        }
    }
    
    public void generarReporte(){
        String query = "Select * FROM venta_general";
        context.selectBD(query);
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
                context.insertUpdateBD(query);
                actualizarTodo(query,dist);
                break;
            case 2: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Gas95'";
                context.insertUpdateBD(query);
                actualizarTodo(query,dist);
                break;
            case 3: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Gas97'";
                context.insertUpdateBD(query);
                actualizarTodo(query,dist);
                break;
            case 4: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Diesel'";
                context.insertUpdateBD(query);
                actualizarTodo(query,dist);
                break;
            case 5: 
                query = "UPDATE precios SET precio = " + Integer.toString(precio) + " WHERE tipo_combustible = 'Kerosene'";
                context.insertUpdateBD(query);
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
    
}
