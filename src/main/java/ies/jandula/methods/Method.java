package ies.jandula.methods;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Method 
{	
	private List<List<Character>> table = new ArrayList<>(); 

	public void creacionTablero(PrintWriter tableroPrintWriter, Random random, int dimensiones) 
	{
	    char[][] tablero = new char[dimensiones][dimensiones]; 
	
	    char tesoro = 'T';
	    char bomba = 'B';
	    char nada = 'N';
	
	    int numBombas = (dimensiones - 1) * dimensiones;
	    int contBombas = (dimensiones - 1) * dimensiones;
	    int numNada = 0; 
	    int numTesoro = 0;
	
	    for (int i = 0; i < tablero.length; i++)
	    {
	        List<Character> row = new ArrayList<>();
	        for (int j = 0; j < tablero[i].length; j++)
	        {
	            tablero[i][j] = nada;
	            row.add(nada); 
	            numNada++;
	        }
	        table.add(row); 
	    }
	
	    while (contBombas > 0)
	    {           
	        int fila = random.nextInt(dimensiones);
	        int columna = random.nextInt(dimensiones);
	
	        if (tablero[fila][columna] == nada) 
	        { 
	            tablero[fila][columna] = bomba;
	            table.get(fila).set(columna, bomba); // Actualizar la celda en el tablero
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
	            table.get(fila).set(columna, tesoro); // Actualizar la celda en el tablero
	            numTesoro++;
	            numNada--;
	        }
	    }
	
	    tableroPrintWriter.println("M = " + dimensiones + " -> " + numTesoro + " T, " + numBombas + " B, " + numNada + " N");
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
	}

	public void introducirCordenada() 
	{
	    Scanner sp = new Scanner(System.in);
	
	    char celda;
	    do
	    {
	        System.out.println("Elige una posición del tablero con formato (fila,columna): ");
	        String posicion = sp.nextLine();
	        String[] split = posicion.split(",") ;
	
	        int fila = Integer.parseInt(split[0]) - 1; 
	        int columna = Integer.parseInt(split[1]) - 1; 
	
	        celda = table.get(fila).get(columna);
	
	        if(celda == 'N') 
	        {
	            System.out.println("Celda seleccionada: N, puedes seguir jugando");
	        } 
	        else if(celda == 'T') 
	        {
	            System.out.println("Tesoro encontrado: ¡Has ganado!");
	        } 
	        else if(celda == 'B') 
	        {
	            System.out.println("Bomba encontrada: ¡Has perdido!");
	        }
	    } while(celda != 'B' && celda != 'T');
	
	    sp.close();
	}

}
