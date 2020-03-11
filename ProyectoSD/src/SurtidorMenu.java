
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
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
public class SurtidorMenu extends Thread{
        
    Socket sucursal;
    Scanner scanner;
    int precio93;
    int precio95;
    int precio97;
    int diesel;
    int kerosene;
    
    public SurtidorMenu(Socket s, int precio93, int precio95, int precio97, int diesel, int kerosene){
        this.sucursal = s;
        this.scanner = new Scanner(System.in);
        this.precio93 = precio93;
        this.precio95 = precio95;
        this.precio97 = precio97;
        this.diesel = diesel;
        this.kerosene = kerosene;
    }    
    
    @Override
    public void run(){
        int opcion;
        int litros;
        try{
            DataOutputStream dos = new DataOutputStream(sucursal.getOutputStream());
            while(!sucursal.isClosed()){
                System.out.println("Realizar una venta");
                System.out.println("Seleccione el tipo de combustible");
                System.out.println("1. Gasolina 93");
                System.out.println("2. Gasolina 95");
                System.out.println("3. Gasolina 97");
                System.out.println("4. Diesel");
                System.out.println("5. Kerosene");
                
                opcion = scanner.nextInt();
                
                System.out.println("Ingrese la cantidad de litros a cargar");
                
                litros = scanner.nextInt();
                
                 LocalDate fecha = LocalDate.now();
                 String hoy = fecha.toString();                    
                 String lt = Integer.toString(litros);
                 String msj;
                 
                 switch(opcion){
                     case 1: 
                        msj = "Gas93" + " " + lt + " " + Integer.toString(this.precio93) + " " + hoy;
                        System.out.println("msj: " + msj);
                        dos.writeUTF(msj);
                        break;
                     case 2:
                        msj = "Gas95" + " " + lt + " " + Integer.toString(this.precio95) + " " + hoy; 
                        System.out.println("msj: " + msj);
                        dos.writeUTF(msj);
                        break;
                     case 3:
                        msj = "Gas97" + " " + lt + " " + Integer.toString(this.precio95) + " " + hoy; 
                        System.out.println("msj: " + msj);
                        dos.writeUTF(msj);
                        break;
                     case 4:
                        msj = "Diesel" + " " + lt + " " + Integer.toString(this.diesel) + " " + hoy; 
                        System.out.println("msj: " + msj);
                        dos.writeUTF(msj);
                        break;
                     case 5:
                        msj = "Kerosene" + " " + lt + " " + Integer.toString(this.kerosene)+ " " + hoy; 
                        System.out.println("msj: " + msj);
                        dos.writeUTF(msj);
                        break;
                 }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
