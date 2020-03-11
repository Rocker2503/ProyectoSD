
import java.io.DataInputStream;
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
 * @author Nicolas
 */
public class Surtidor {
    
    public static void main(String[] args) {
        
        String ipNico = "25.48.255.90";
        String ipJuan = "25.49.16.34";
        String ipAlvaro = "25.49.55.58";
        ArrayList<String> ventas = new ArrayList<String>();
        
        int precio93 = 600;
        int precio95 = 650;
        int precio97 = 700;
        int diesel = 450;
        int kerosene = 540;
        
        String ip = "25.49.55.58";
        int port = 69;
                
        try{
            Socket socket = new Socket(ip,port);
            System.out.println("info socket: " + socket.getInetAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            while(true){
                String inSocket = in.readUTF();
                System.out.println("Desde el Distribuidor llega: " + inSocket);
                
                String con = "Conectado";
                out.writeUTF(con);
                
                Scanner scan = new Scanner(System.in);
                
                System.out.println("Ingrese una opcion");
                System.out.println("1. Para realizar una venta");
                System.out.println("2. Enviar Ventas");
                System.out.println("0. Para apagar la maquina");
                int opcion = scan.nextInt();
                while(opcion < 0 || opcion > 2){
                    System.out.println("Ingrese una opcion permitida");
                    opcion = scan.nextInt();
                }
                if(opcion == 1){
                    System.out.println("Elija el tipo de combustible");
                    System.out.println("1. para gasolina 93");
                    System.out.println("2. para gasolina 95");
                    System.out.println("3. para gasolina 97");
                    System.out.println("4. para diesel");
                    System.out.println("5. para kerosene");
                    int gas = scan.nextInt();
                    while(gas < 0 || gas > 5){
                        System.out.println("Ingrese una opcion permitida");
                        gas = scan.nextInt();
                    }
                    System.out.println("Ingrese la cantidad de litros");
                    int litros = scan.nextInt();
                    while(litros < 0){
                        System.out.println("Ingrese una cantidad permitida de litros a vender");
                        litros = scan.nextInt();
                    }
                    
                    LocalDate fecha = LocalDate.now();
                    String hoy = fecha.toString();                    
                    String lt = Integer.toString(litros);
                    String total,query;
                    switch (gas){
                        case 1:
                            total = Integer.toString(precio93 * litros);
                            query = "Insert INTO venta(fecha, tipo_combustible,litros,total) VALUES(" + hoy + ", Gas93, " + lt + "," + total + ")";
                            ventas.add(query);
                            break;
                        case 2:    
                            total = Integer.toString(litros * precio95);
                            query = "Insert INTO venta(fecha, tipo_combustible,litros,total) VALUES(" + hoy + ", Gas95, " + lt + "," + total + ")";
                            ventas.add(query);
                            break;
                        case 3:
                            total = Integer.toString(litros * precio97);
                            query = "Insert INTO venta(fecha, tipo_combustible,litros,total) VALUES(" + hoy + ", Gas97, " + lt + "," + total + ")";
                            ventas.add(query);
                        case 4:
                            total = Integer.toString(litros * diesel);
                            query = "Insert INTO venta(fecha, tipo_combustible,litros,total) VALUES(" + hoy + ", Diesel, " + lt + "," + total + ")";
                            ventas.add(query);
                        case 5:
                            total = Integer.toString(litros * kerosene);
                            query = "Insert INTO venta(fecha, tipo_combustible,litros,total) VALUES(" + hoy + ", Kerosene, " + lt + "," + total + ")";
                            ventas.add(query);
                    }
                            
                }
                else if(opcion == 2){
                    for(String s : ventas){
                        out.writeUTF(s);
                    }
                    ventas.clear();
                }
                else{
                    System.out.println("Apagando surtidor...");
                    break;
                }         
            }
        }
        catch(IOException e){
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
