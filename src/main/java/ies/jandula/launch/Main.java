package ies.jandula.launch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ies.jandula.tesoroExceptions.TesoroExceptions;

public class Main 
{
    private static final String TABLERO_INICIAL = "TableroInicial.csv";
    private static final String HISTORICO_MOVIMIENTOS = "HistoricoMovimientos.csv";
    private static final String TABLERO_ACTUAL = "TableroActual.csv";

    private static List<List<Character>> table = new ArrayList<>(); // Tablero actual
    private static List<String> historicoMovimientos = new ArrayList<>(); // Historial de movimientos

    public static void main(String[] args) throws TesoroExceptions 
    {
        PrintWriter tableroInicialPrintWriter = null;
        PrintWriter historicoMovimientosPrintWriter = null;
        PrintWriter tableroActualPrintWriter = null;
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        try 
        {
            tableroInicialPrintWriter = new PrintWriter(TABLERO_INICIAL);
            historicoMovimientosPrintWriter = new PrintWriter(HISTORICO_MOVIMIENTOS);
            tableroActualPrintWriter = new PrintWriter(TABLERO_ACTUAL);

            int dimensiones;
            boolean continuarJuego = true;

            do 
            {
                System.out.println("Introduce las dimensiones (debe ser al menos 4): ");
                dimensiones = scanner.nextInt();
            } 
            while (dimensiones < 4);

            creacionTablero(tableroInicialPrintWriter, random, dimensiones);

            while (continuarJuego) 
            {
                if (!continuarJuego()) 
                {
                    break;
                }

                introducirCordenada(scanner);

                // Actualizar el historial de movimientos
                guardarMovimiento();
            }

            // Guardar el tablero actual y el historial de movimientos al final del juego
            guardarTablero(tableroActualPrintWriter);
            guardarHistoricoMovimientos(historicoMovimientosPrintWriter);
        } 
        catch (FileNotFoundException fileNotFoundException) 
        {
            String error = "Error: Fichero no encontrado";
            fileNotFoundException.printStackTrace();
            throw new TesoroExceptions(1, error, fileNotFoundException);
        } 
        finally 
        {
            if (tableroInicialPrintWriter != null) 
            {
                tableroInicialPrintWriter.close();
            }
            if (historicoMovimientosPrintWriter != null) 
            {
                historicoMovimientosPrintWriter.close();
            }
            if (tableroActualPrintWriter != null) 
            {
                tableroActualPrintWriter.close();
            }
            scanner.close();
        }
    }

    private static void creacionTablero(PrintWriter tableroPrintWriter, Random random, int dimensiones) 
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

        System.out.println("Guardado el tablero inicial exitosamente");
        tableroPrintWriter.flush();
    }

    private static boolean continuarJuego() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Quieres continuar el juego? (R para continuar, cualquier tecla para salir)");
        String respuesta = scanner.nextLine();
        scanner.close();
        return respuesta.equalsIgnoreCase("R");
    }

    private static void introducirCordenada(Scanner scanner) 
    {
        char celda;
        do 
        {
            System.out.println("Elige una posición del tablero con formato (fila,columna): ");
            String posicion = scanner.nextLine();
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
            } 
            else if (celda == 'B') 
            {
                System.out.println("Bomba encontrada: ¡Has perdido!");
            }
        } while (celda != 'B' && celda != 'T');
    }

    private static void guardarMovimiento() 
    {
        StringBuilder movimiento = new StringBuilder();
        for (List<Character> row : table) 
        {
            for (char celda : row) 
            {
                movimiento.append(celda);
            }
            movimiento.append(",");
        }
        historicoMovimientos.add(movimiento.toString());
    }

    private static void guardarTablero(PrintWriter tableroActualPrintWriter) 
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

    private static void guardarHistoricoMovimientos(PrintWriter historicoMovimientosPrintWriter) 
    {
        for (String movimiento : historicoMovimientos) 
        {
            historicoMovimientosPrintWriter.println(movimiento);
        }
        System.out.println("Guardado el historial de movimientos exitosamente");
        historicoMovimientosPrintWriter.flush();
    }
}
