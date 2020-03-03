
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zerling
 */
public class Client
{
    public static void main(String[] args) throws IOException
    {
        String ip = "localhost";
        int port = 10012;
        
        Scanner scanner = new Scanner(System.in);

        
        Socket sc = new Socket(ip, port);
        DataInputStream in = new DataInputStream(sc.getInputStream());
        DataOutputStream out = new DataOutputStream(sc.getOutputStream());
        
        int opcion = -1;

        while(opcion != 0)
        {
            System.out.println("Menu de fiestas");
            System.out.println("1. Ingresar persona");
            System.out.println("2. Listar persona");
            System.out.println("0. Salir");

            System.out.println("Elegir opcion");

            opcion = scanner.nextInt();
            //out.writeInt(opcion);

            scanner.nextLine();
            //out.writeInt(opcion);
            
        //Si quiere enviar un nombre
            if(opcion == 1)
            {
                out.writeInt(opcion);

                String nombre = "";
                

                System.out.println("Ingresar nombre de persona que va a la fiesta:");
                nombre = scanner.nextLine();
                //System.out.println("writting in to server");
                out.writeUTF(nombre);
            }
            //si quiere consultar la lista
            if(opcion == 2)
            {
                out.writeInt(opcion);
                System.out.println("opcion enviada al servidor");
                String nombre = "";
                int size = in.readInt();
                //size=1;
                System.out.println("tamaño lista: "+size);
                
                System.out.println("Nombre de los asistentes: ");
                
                for (int i = 0; i < size; i++)
                {
                    nombre = in.readUTF();
                    System.out.println(nombre);
                }
                System.out.println("");
            }
        }
    }
}
