package ies.jandula.launch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import ies.jandula.tesoroExceptions.TesoroExceptions;

public class Main 
{
    public static void main(String[] args) throws TesoroExceptions 
    {
        PrintWriter printWriter = null;

        try 
        {
            printWriter = new PrintWriter("Tesoro.csv");
            Scanner scanner = new Scanner(System.in);
            Scanner scannerCSV = new Scanner("Tesoro.csv");
            Random random = new Random();

            int dimensiones;

            do 
            {		
                System.out.println("Introduce las dimensiones (debe ser al menos 4): ");	
                dimensiones = scanner.nextInt();		
            } 
            while (dimensiones < 4);

            creacionTablero(printWriter, random, dimensiones);
        } 
        catch (FileNotFoundException fileNotFoundException) 
        {
        	String error = "Error: Fichero no encontrado";
            fileNotFoundException.printStackTrace();
            throw new TesoroExceptions(1, error, fileNotFoundException);
        } 
        finally 
        {
            if (printWriter != null) 
            {
                printWriter.close();
            }
        }
    }

	/**
	 * @param printWriter
	 * @param random
	 * @param dimensiones
	 */
	private static void creacionTablero(PrintWriter printWriter, Random random, int dimensiones) 
	{
		char[][] tablero = new char[dimensiones][dimensiones];	

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

		printWriter.println("M = " + tablero.length + " -> " + numTesoro + " T, " + numBombas + " B, " + numNada + " N");
		for (int i = 0; i < tablero.length; i++)
		{
		    for (int j = 0; j < tablero[i].length; j++) 
		    {
		        printWriter.print(tablero[i][j]);
		        if (j < tablero[i].length - 1) 
		        {
		            printWriter.print(","); 
		        }
		    }
		    printWriter.println();
		}

		System.out.println("Guardados exitosamente");
		printWriter.flush();
	}
}
