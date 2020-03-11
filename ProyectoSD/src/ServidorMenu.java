
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
    private ArrayList<Socket> sucursales;
    private ServerSocket servidor;
    private Scanner sc;
    private ConexionBDServidor context;
    
    public ServidorMenu(ServerSocket s){
        this.servidor = s;
        this.sucursales = new ArrayList<>();
        this.sc = new Scanner(System.in);
        this.context = new ConexionBDServidor();
    }

    @Override
    public void run() {
        int seleccion;
        while(true){
            System.out.println("1. Para obtener los reportes");
            System.out.println("2. Para actualizar precios");
            System.out.println("3. Para salir");
            System.out.print("Ingresar opcion: ");
            
            seleccion = sc.nextInt();
            switch(seleccion){
                case 1:
                    generarReporte();
                    break;
                case 2:
                    //Actualizar precio
                    break;
                case 3:
                    //Salir
            }
        }
    }
    
    public void generarReporte(){
        String query = "Select * FROM venta_general";
        context.selectBD(query);
    }
    
    public void actualizarPrecios(ArrayList<Socket> sucursales){
        
    }
    
    public boolean agregarSocket(Socket s){
        System.out.println("Se agrega conexión a sucursal...");
        return sucursales.add(s);
    }
    
    
}
