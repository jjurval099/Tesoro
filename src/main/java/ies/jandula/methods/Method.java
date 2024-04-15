package ies.jandula.methods;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Method 
{	
	public final String TABLERO_INICIAL = "TableroInicial.csv";
	public final String HISTORICO_MOVIMIENTOS = "HistoricoMovimientos.csv";
	public final String TABLERO_ACTUAL = "TableroActual.csv";

	public List<List<Character>> table = new ArrayList<>(); 
	public List<String> historicoMovimientos = new ArrayList<>();

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
                table.get(fila).set(columna, bomba); 
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
                table.get(fila).set(columna, tesoro); 
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

        System.out.println("Guardado el tablero inicial exitosamente");
        tableroPrintWriter.flush();
    }

    public boolean continuarJuego() 
    {
        Scanner scannerContinuar = new Scanner(System.in);
        System.out.println("¿Quieres continuar el juego? (R para continuar, cualquier tecla para salir)");
        String respuesta = scannerContinuar.nextLine();
        
        if(!respuesta.equalsIgnoreCase("R"))
        {
        	return false;
        }
        
        return true;
    }

    public boolean introducirCordenada(Scanner scannerContinuar)
    {
        char celda;

        do 
        {
            System.out.println("Elige una posición del tablero con formato (fila,columna): ");
            String posicion = scannerContinuar.nextLine();
            String[] split = posicion.split(",");

            int fila = Integer.parseInt(split[0]) - 1;
            int columna = Integer.parseInt(split[1]) - 1;

            celda = table.get(fila).get(columna);

            if (celda == 'N') 
            {
                System.out.println("Celda seleccionada: N, puedes seguir jugando");
            } 
            else if (celda == 'T')
            {
                System.out.println("Tesoro encontrado: ¡Has ganado!");
                return false; 
            } 
            else if (celda == 'B')
            {
                System.out.println("Bomba encontrada: ¡Has perdido!");
                return false;
            }

            guardarMovimiento(fila, columna); 
        } while (celda != 'B' && celda != 'T');

        return true; 
    }

    public void guardarMovimiento(int fila, int columna) 
    {
        String movimiento = (fila + 1) + "," + (columna + 1);
        historicoMovimientos.add(movimiento);
    }

    public void guardarTablero(PrintWriter tableroActualPrintWriter) 
    {
        for (List<Character> row : table) 
        {
            for (char celda : row) 
            {
                tableroActualPrintWriter.print(celda);
                tableroActualPrintWriter.print(",");
            }
            tableroActualPrintWriter.println();
        }
        System.out.println("Guardado el tablero actual exitosamente");
        tableroActualPrintWriter.flush();
    }

    public void guardarHistoricoMovimientos(PrintWriter historicoMovimientosPrintWriter) 
    {
        for (String movimiento : historicoMovimientos) 
        {
            historicoMovimientosPrintWriter.println(movimiento);
        }
        System.out.println("Guardado el historial de movimientos exitosamente");
        historicoMovimientosPrintWriter.flush();
    }

}
