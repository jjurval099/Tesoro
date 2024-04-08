package ies.jandula.launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import ies.jandula.tesoroExceptions.TesoroExceptions;

public class Main 
{
    public static void main(String[] args) throws TesoroExceptions 
    {
        PrintWriter tableroPrintWriter = null;
        PrintWriter posicionesPrintWriter = null;

        try 
        {
            tableroPrintWriter = new PrintWriter("Tablero.csv");
            posicionesPrintWriter = new PrintWriter("Posiciones.csv");
            Scanner scanner = new Scanner(System.in);
            Random random = new Random();
            
            File file = new File("Posiciones.csv");            
            Scanner scannerCSV = new Scanner(file);

            int dimensiones;

            do 
            {       
                System.out.println("Introduce las dimensiones (debe ser al menos 4): ");    
                dimensiones = scanner.nextInt();      
            } 
            while (dimensiones < 4);

            ArrayList<String> filaColumna = creacionTablero(tableroPrintWriter, random, dimensiones, posicionesPrintWriter);            
            
//--------------------------------------------------------------------------------------------
            
            scannerCSV.nextLine();
            
            while(scannerCSV.hasNextLine())
            {            
                String linea = scanner.nextLine();

                if (!linea.isEmpty()) 
                {
                    String[] datos = linea.split(","); 
                    int fila = Integer.parseInt(datos[0]);
                    int columna = Integer.parseInt(datos[1]);                    
                }
            }
            
            boolean juegoTerminado = false;
            do 
            {
                int filaUsuario;
                int columnaUsuario;

                System.out.println("Introduce posición del tablero: ");

                System.out.println("Introduce la fila");
                filaUsuario = scanner.nextInt();
                System.out.println("Introduce la columna");
                columnaUsuario = scanner.nextInt();

                // Verifica si la coordenada del usuario corresponde al tesoro o a una bomba
                char casilla = filaColumna.get(filaUsuario * dimensiones + columnaUsuario).charAt(0);
                if (casilla == 'T') 
                {
                    System.out.println("Tesoro encontrado");
                    juegoTerminado = true;
                } 
                else if (casilla == 'B') 
                {
                    System.out.println("¡Boom!");
                    juegoTerminado = true;
                }
                else 
                {
                    System.out.println("No has encontrado nada en esta posición");
                }

            }
            while (!juegoTerminado);
            
        } 
        catch (FileNotFoundException fileNotFoundException) 
        {
            String error = "Error: Fichero no encontrado";
            fileNotFoundException.printStackTrace();
            throw new TesoroExceptions(1, error, fileNotFoundException);
        } 
        finally 
        {
            if (tableroPrintWriter != null) 
            {
                tableroPrintWriter.close();
            }
            if (posicionesPrintWriter != null) 
            {
                posicionesPrintWriter.close();
            }
        }
    }

    private static ArrayList<String> creacionTablero(PrintWriter tableroPrintWriter, Random random, int dimensiones, PrintWriter posicionesPrintWriter) 
    {
        char[][] tablero = new char[dimensiones][dimensiones];  
        ArrayList<String> filaColumna = new ArrayList<>(); 

        char tesoro = 'T';
        char bombas = 'B';
        char nada = 'N';

        int numBombas = (dimensiones - 1) * dimensiones;
        int contBombas = (dimensiones - 1) * dimensiones;
        int numNada = 0; 
        int numTesoro = 0;

        for (int i = 0; i < tablero.length; i++)
        {
            for (int j = 0; j < tablero[i].length; j++)
            {
                tablero[i][j] = nada;
                numNada++;
                // Agrega la fila y la columna a la lista
                filaColumna.add(i + "," + j); 
            }
        }

        while (contBombas > 0)
        {           
            int fila = random.nextInt(dimensiones);
            int columna = random.nextInt(dimensiones);

            if (tablero[fila][columna] == nada) 
            { 
                tablero[fila][columna] = bombas;
                contBombas--;
                numNada--;
            }
        }

        while (numTesoro == 0)
        {           
            int fila = random.nextInt(dimensiones);
            int columna = random.nextInt(dimensiones);

            if (tablero[fila][columna] == nada) 
            { 
                tablero[fila][columna] = tesoro;
                numTesoro++;
                numNada--;
            }
        }

        // Escribir la lista filaColumna en el archivo
        posicionesPrintWriter.println("Fila,Columna");
        for (String filaCol : filaColumna) {
            posicionesPrintWriter.println(filaCol);
        }

        // Escribir el tablero en el archivo
        tableroPrintWriter.println("M = " + tablero.length + " -> " + numTesoro + " T, " + numBombas + " B, " + numNada + " N");
        for (int i = 0; i < tablero.length; i++)
        {
            for (int j = 0; j < tablero[i].length; j++) 
            {
                tableroPrintWriter.print(tablero[i][j]);
                if (j < tablero[i].length - 1) 
                {
                    tableroPrintWriter.print(","); 
                }
            }
            tableroPrintWriter.println();
        }

        System.out.println("Guardados exitosamente");
        tableroPrintWriter.flush();
        posicionesPrintWriter.flush();
        
        // Devolucion de la lista con las posiciones de fila y columna
        return filaColumna; 
    }
}

